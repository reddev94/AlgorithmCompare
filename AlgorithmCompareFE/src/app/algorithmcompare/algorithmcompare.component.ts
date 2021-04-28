import { Component, OnInit, OnDestroy } from '@angular/core';
import { AlgorithmService } from '../service/algorithm-service';
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { UtilData } from '../model/util-data';
import { Subscriptions } from '../model/subscriptions';
import { ExecutionData } from '../model/execution-data';
import { ChartData } from '../model/chart-data';
import { Observable, forkJoin } from 'rxjs';
import { Label } from 'ng2-charts';
import { ChartDataSets } from 'chart.js';

@Component({
  selector: 'app-algorithmcompare',
  templateUrl: './algorithmcompare.component.html',
  styleUrls: ['./algorithmcompare.component.css']
})
export class AlgorithmcompareComponent implements OnInit, OnDestroy {
  subscriptions: Subscriptions;
  algorithmForm: FormGroup;
  utilData: UtilData;
  chartData: ChartData;

  constructor(private algorithmService: AlgorithmService, private fb: FormBuilder) {
      this.resetData();
  }

  ngOnInit(): void {
    this.getAvailableAlgorithms();
  }

  getAvailableAlgorithms(): void {
    this.subscriptions.availableAlgorithmSub$ = this.algorithmService.getAvailableAlgorithms()
      .subscribe({
        next: data => {
             console.log(data);
             this.utilData.algorithms = data.availableAlgorithms;
        },
        error: data => {
          console.log('Error during comunication');
        }
      });
  }

  generateArray(): void {
    this.subscriptions.generateArraySub$ = this.algorithmService.generateArray(this.algorithmForm.controls['arrayLength'].value)
      .subscribe({
        next: data => {
          console.log(data);
          this.utilData.array = data.array;
        },
        error: data => {
          console.log('Error during comunication');
        }
      });
  }

  prepareVisualizationArray(arrayIdentifier: number): void {
    this.subscriptions.executeAlgorithmSub$ = this.algorithmService.executeAlgorithm(this.algorithmForm.controls['algorithmType'+arrayIdentifier].value, this.utilData.array)
      .subscribe({
        next: data => {
          console.log(data);
          if(arrayIdentifier == 1) {
            console.log('saving idRequester and maxExecutionTime for array1');
            this.utilData.firstArrayIdRequester = data.idRequester;
            this.utilData.firstArrayMaxExecutionTime = data.maxExecutionTime;
            this.utilData.isFirstArrayCompleted = true;
          } else if(arrayIdentifier == 2) {
          console.log('saving idRequester and maxExecutionTime for array2');
            this.utilData.secondArrayIdRequester = data.idRequester;
            this.utilData.secondArrayMaxExecutionTime = data.maxExecutionTime;
            this.utilData.isSecondArrayCompleted = true;
          }
        },
        error: data => {
          console.log('Error during comunication');
        }
      });
  }

  startVisualization1(): void {
    console.log('calling getExecutionData for array1');
    this.subscriptions.getExecutionDataSub1$ = this.algorithmService.getExecutionData(this.utilData.firstArrayIdRequester)
      .subscribe({
        next: data => {
          console.log('response data array1');
          console.log(data);
          var element = new ExecutionData();
          element.array = data.array;
          element.moveExecutionTime = data.moveExecutionTime;
          this.utilData.firstArrayExecutionData.push(element);
          var labels: Label[] = [];
          for(var i=0; i<data.array.length; i++) {
            labels.push(data.array[i]+'');
          }
          this.chartData.barChartLabels = labels;
          var barChartData: ChartDataSets[] = [{ data: data.array, label: 'Array1' }];
          this.chartData.barChartData = barChartData;
        },
        error: data => {
          console.log('Error during comunication');
        }
      });
  }

    startVisualization2(): void {
      console.log('calling getExecutionData for array2');
      this.subscriptions.getExecutionDataSub2$ = this.algorithmService.getExecutionData(this.utilData.secondArrayIdRequester)
        .subscribe({
          next: data => {
            console.log('response data array2');
            console.log(data);
            var element = new ExecutionData();
            element.array = data.array;
            element.moveExecutionTime = data.moveExecutionTime;
            this.utilData.secondArrayExecutionData.push(element);
            var labels: Label[] = [];
            for(var i=0; i<data.array.length; i++) {
              labels.push(data.array[i]+'');
            }
            this.chartData.barChartLabels = labels;
            var barChartData: ChartDataSets[] = [{ data: data.array, label: 'Array1' }];
            this.chartData.barChartData = barChartData;
          },
          error: data => {
            console.log('Error during comunication');
          }
        });
    }

  deleteExecutionData(): void {
    var deleteSub1$;
    var deleteSub2$;
    if(this.utilData && this.utilData.firstArrayIdRequester) {
      deleteSub1$ = this.algorithmService.deleteExecutionData(this.utilData.firstArrayIdRequester).subscribe({
        next: data => {
          console.log(data);
          deleteSub1$.unsubscribe();
        },
        error: data => {
          console.log('Error during comunication');
        }
      });
    }
    if(this.utilData && this.utilData.secondArrayIdRequester) {
      deleteSub2$ = this.algorithmService.deleteExecutionData(this.utilData.secondArrayIdRequester).subscribe({
        next: data => {
          console.log(data);
          deleteSub2$.unsubscribe();
        },
        error: data => {
          console.log('Error during comunication');
        }
      });
    }
  }

  activateSecondArraySlot(): void {
    this.utilData.isSecondArrayActive = true;
  }

  onSubmit(buttonType: string): void {
    if(buttonType==="prepareArray1") {
      this.prepareVisualizationArray(1);
    } else if(buttonType==="prepareArray2") {
      this.prepareVisualizationArray(2);
    } else if(buttonType==="run") {
      this.utilData.isRunning = true;
      if(this.utilData.isSecondArrayActive) {
        this.startVisualization1();
        this.startVisualization2();
      } else {
        this.startVisualization1();
      }
    }
  }

  ngOnDestroy(): void {
    this.closeAllConnection();
  }

  resetData(): void {
    console.log('initializing data');
    this.closeAllConnection();
    var algorithms;
    if(this.utilData) {
      algorithms = this.utilData.algorithms;
    }
    this.chartData = new ChartData([], [], []);
    this.utilData = new UtilData(false, false, false, false, [], []);
    this.utilData.algorithms = algorithms;
    this.algorithmForm = this.fb.group({
      algorithmType1: '',
      algorithmType2: '',
      arrayLength: ''
    });
    this.subscriptions = new Subscriptions();
  }

  private closeAllConnection(): void {
    console.log('closing open connection');
    this.deleteExecutionData();
    if(this.subscriptions) {
      if ( this.subscriptions.availableAlgorithmSub$ ){
        this.subscriptions.availableAlgorithmSub$.unsubscribe();
      }
      if ( this.subscriptions.generateArraySub$ ) {
        this.subscriptions.generateArraySub$.unsubscribe();
      }
      if(this.subscriptions.executeAlgorithmSub$) {
        this.subscriptions.executeAlgorithmSub$.unsubscribe();
      }
      if(this.subscriptions.getExecutionDataSub1$) {
        this.subscriptions.getExecutionDataSub1$.unsubscribe();
      }
      if(this.subscriptions.getExecutionDataSub2$) {
        this.subscriptions.getExecutionDataSub2$.unsubscribe();
      }
    }
  }

}
