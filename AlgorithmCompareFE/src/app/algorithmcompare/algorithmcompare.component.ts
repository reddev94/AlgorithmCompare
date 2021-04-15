import { Component, OnInit, OnDestroy } from '@angular/core';
import { AlgorithmService } from '../service/algorithm-service';
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { UtilData } from '../model/util-data';
import { Subscriptions } from '../model/subscriptions';

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
      this.utilData = new UtilData(false, false, false);
      this.algorithmForm = this.fb.group({
          algorithmType1: '',
          algorithmType2: '',
          arrayLength: ''
        });
        this.subscriptions = new Subscriptions();
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
        },
        error : data => {
          console.log('Error during comunication');
        }
      });
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
    this.algorithmService.closeConnection();
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
