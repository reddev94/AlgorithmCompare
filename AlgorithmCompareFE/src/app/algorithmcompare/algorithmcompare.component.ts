import { Component, OnInit, OnDestroy } from '@angular/core';
import { AlgorithmService } from '../service/algorithm-service';
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { UtilData } from '../model/util-data';
import { Subscriptions } from '../model/subscriptions';
import { ExecutionData } from '../model/execution-data';

@Component({
  selector: 'app-algorithmcompare',
  templateUrl: './algorithmcompare.component.html',
  styleUrls: ['./algorithmcompare.component.css']
})
export class AlgorithmcompareComponent implements OnInit, OnDestroy {
  subscriptions: Subscriptions;
  algorithmForm: FormGroup;
  utilData: UtilData;

  constructor(private algorithmService: AlgorithmService, private fb: FormBuilder) {
      this.resetData();
  }

  ngOnInit(): void {
    this.getAvailableAlgorithms();
  }

  getAvailableAlgorithms(): void {
    this.subscriptions.availableAlgorithmSub$ = this.algorithmService.getAvailableAlgorithms()
      .subscribe({
        next : data => {
             console.log(data);
             this.utilData.algorithms = data.availableAlgorithms;
        },
        error : data => {
          console.log('Error during comunication');
        }
      });
  }

  generateArray(): void {
    this.subscriptions.generateArraySub$ = this.algorithmService.generateArray(this.algorithmForm.controls['arrayLength'].value)
      .subscribe({
        next : data => {
          console.log(data);
          this.utilData.array = data.array;
        },
        error : data => {
          console.log('Error during comunication');
        }
      });
  }

  prepareVisualizationArray(arrayIdentifier: number): void {
    this.subscriptions.executeAlgorithmSub$ = this.algorithmService.executeAlgorithm(this.algorithmForm.controls['algorithmType'+arrayIdentifier].value, this.utilData.array)
      .subscribe({
        next : data => {
          console.log(data);
          if(arrayIdentifier == 1) {
            console.log('saving idRequester for array1');
            this.utilData.firstArrayIdRequester = data.idRequester;
          } else if(arrayIdentifier == 2) {
          console.log('saving idRequester for array2');
            this.utilData.secondArrayIdRequester = data.idRequester;
          }
          this.subscriptions.maxExecutionTimeSub$ = this.algorithmService.getMaxExecutionTime(data.idRequester)
            .subscribe({
              next : data => {
                console.log(data);
                if(arrayIdentifier == 1) {
                  console.log('saving maxExecutionTime for array1');
                  this.utilData.firstArrayMaxExecutionTime = data.maxExecutionTime;
                } else if(arrayIdentifier == 2) {
                  console.log('saving maxExecutionTime for array2');
                  this.utilData.secondArrayMaxExecutionTime = data.maxExecutionTime;
                }
              },
              error : data => {
                console.log('Error during comunication');
              }
            });
          this.algorithmService.getExecutionData(data.idRequester)
            .subscribe({
              next : data => {
                console.log(data);
                var element = new ExecutionData();
                element.array = data.array;
                element.moveExecutionTime = data.moveExecutionTime;
                if(arrayIdentifier == 1) {
                  console.log('get execution data for array1');
                  this.utilData.firstArrayExecutionData.push(element);
                } else if(arrayIdentifier == 2) {
                  console.log('get execution data for array2');
                  this.utilData.secondArrayExecutionData.push(element);
                }
              },
              error : data => {
                console.log('Error during comunication');
              }
            });
        },
        error : data => {
          console.log('Error during comunication');
        }
      });
  }

  deleteExecutionData(): void {
    var deleteSub1$;
    var deleteSub2$;
    if(this.utilData && this.utilData.firstArrayIdRequester) {
      deleteSub1$ = this.algorithmService.deleteExecutionData(this.utilData.firstArrayIdRequester).subscribe({
        next : data => {
          console.log(data);
          deleteSub1$.unsubscribe();
        },
        error : data => {
          console.log('Error during comunication');
        }
      });
    }
    if(this.utilData && this.utilData.secondArrayIdRequester) {
      deleteSub2$ = this.algorithmService.deleteExecutionData(this.utilData.secondArrayIdRequester).subscribe({
        next : data => {
          console.log(data);
          deleteSub2$.unsubscribe();
        },
        error : data => {
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
      this.utilData.isFirstArrayCompleted = true;
    } else if(buttonType==="prepareArray2") {
      this.prepareVisualizationArray(2);
      this.utilData.isSecondArrayCompleted = true;
    } else if(buttonType==="run") {

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
    this.utilData = new UtilData(false, false, false, [], []);
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
    this.algorithmService.closeConnection();
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
      if(this.subscriptions.maxExecutionTimeSub$) {
        this.subscriptions.maxExecutionTimeSub$.unsubscribe();
      }
    }
  }

}
