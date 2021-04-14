import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { tap, map, catchError, retry } from 'rxjs/operators';
import { AlgorithmAvailable } from '../model/algorithm-available';

@Injectable({
  providedIn: 'root'
})
export class AlgorithmService {
  private BASE_URL = 'http://localhost:8080';
  private AVAILABLE_ALGORITHM_URL = this.BASE_URL + '/blocking/getAlgorithms';

  constructor(private http: HttpClient) {
  }

  public getAvailableAlgorithms(): Observable<AlgorithmAvailable> {
    console.log('Call to available algorithm rest api');
    return this.http.get<AlgorithmAvailable>(this.AVAILABLE_ALGORITHM_URL);
                      //.subscribe((data: AlgorithmAvailable) => this.availableAlgorithmsResponse = { ...data });
//     this.http.get<AlgorithmAvailable>(this.AVAILABLE_ALGORITHM_URL)
//       .subscribe((data: AlgorithmAvailable) => this.availableAlgorithmsResponse = {
//         availableAlgorithms: data.availableAlgorithms as string[],
//         resultCode:  data.resultCode as number,
//         resultDescription: data.resultDescription as string,
//       });
  }

private handleError(error: HttpErrorResponse) {
  if (error.error instanceof ErrorEvent) {
    // A client-side or network error occurred. Handle it accordingly.
    console.error('An error occurred:', error.error.message);
  } else {
    // The backend returned an unsuccessful response code.
    // The response body may contain clues as to what went wrong.
    console.error(
      `Backend returned code ${error.status}, ` +
      `body was: ${error.error}`);
  }
  // Return an observable with a user-facing error message.
  return throwError(
    'Something bad happened; please try again later.');
}

}
