import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, of, throwError, asyncScheduler } from 'rxjs';
import { tap, map, catchError, retry, concatMap } from 'rxjs/operators';
import { AlgorithmAvailable } from '../model/algorithm-available';
import { GenerateArray } from '../model/generate-array';
import { ExecuteAlgorithm } from '../model/execute-algorithm';
import { DeleteData } from '../model/delete-data';
import { AlgorithmExecutionData } from '../model/algorithm-data';
import {EventSourcePolyfill} from 'ng-event-source';
import { subscribeOn, delay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AlgorithmService {
  private BASE_URL = 'http://localhost:8080';
  private AVAILABLE_ALGORITHM_URL = this.BASE_URL + '/blocking/getAlgorithms';
  private GENERATE_ARRAY_URL = this.BASE_URL + '/blocking/generateArray';
  private EXECUTE_ALGORITHM_URL = this.BASE_URL + '/reactive/executeAlgorithm';
  private MAX_EXECUTION_DATA_URL = this.BASE_URL + '/reactive/getMaxExecutionTime';
  private DELETE_DATA_URL = this.BASE_URL + '/reactive/deleteExecuteAlgorithmData';
  private GET_EXECUTION_DATA_URL = this.BASE_URL + '/reactive/getExecutionData';

  constructor(private http: HttpClient) {
  }

  public getAvailableAlgorithms(): Observable<AlgorithmAvailable> {
    console.log('Call to available algorithm rest api');
    return this.http.get<AlgorithmAvailable>(this.AVAILABLE_ALGORITHM_URL);
  }

  public generateArray(length: number): Observable<GenerateArray> {
    console.log('Call to generate array rest api');
    var param = { params: new HttpParams({fromString: "length="+length}) };
    return this.http.get<GenerateArray>(this.GENERATE_ARRAY_URL, param);
  }

  public executeAlgorithm(algorithmRequest: string, arrayRequest: number[]): Observable<ExecuteAlgorithm> {
    console.log('Call to execute algorithm rest api');
    var body = {algorithm: algorithmRequest, array: arrayRequest};
    return this.http.post<ExecuteAlgorithm>(this.EXECUTE_ALGORITHM_URL, body);
  }

  public deleteExecutionData(idRequester: string): Observable<DeleteData> {
    console.log('Call to delete data rest api');
    var param = { params: new HttpParams({fromString: "idRequester="+idRequester}) };
    return this.http.delete<DeleteData>(this.DELETE_DATA_URL, param);
  }

  public getExecutionData(idRequester: string, arrayId: number, maxMoveExecutionTime: number): Observable<AlgorithmExecutionData> {
    console.log('Call to get execution data rest api');
    return Observable.create((observer) => {
            var eventSource = new EventSourcePolyfill(this.GET_EXECUTION_DATA_URL+"?idRequester="+idRequester+"&maxMoveExecutionTime="+maxMoveExecutionTime, {headers: { 'Content-Type': 'text/event-stream'}});
    		    eventSource.onopen = (open) => {
    			    console.log('Opened connection');
    		    };
    	 	    eventSource.onmessage = async (event) => {
    			    let json = JSON.parse(event.data);
    			    var message = {
    			      array: json['array'],
    			      moveExecutionTime: json['moveExecutionTime'],
    			      resultCode: json['resultCode'],
    			      resultDescription: json['resultDescription'],
    			      arrayIdentifier: arrayId
    			    }
    			    if(message.resultCode!=0) {
    			      observer.next(message);
    			    } else {
    			      observer.complete();
    			      eventSource.close();
    			    }
    	      };
            eventSource.onerror = (error) => {
              console.log('Error, closing connection');
    			    eventSource.close();
    	      };
          })
          //.pipe(subscribeOn(asyncScheduler));
  }

}
