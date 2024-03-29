import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { AlgorithmService } from '../service/algorithm-service';
import { FormBuilder, Validators, FormGroup, FormControl } from "@angular/forms";
import { UtilData } from '../model/util-data';
import { Subscriptions } from '../model/subscriptions';
import { Observable, forkJoin, merge } from 'rxjs';
import { AlgorithmExecutionData } from '../model/algorithm-data';
import { ArrayCanvasData } from '../model/array-canvas-data';
import { ExecutionData } from '../model/execution-data';
import { ArrayInfo } from '../model/algorithm-data';

@Component({
  selector: 'app-algorithmcompare',
  templateUrl: './algorithmcompare.component.html',
  styleUrls: ['./algorithmcompare.component.css']
})
export class AlgorithmcompareComponent implements OnInit, OnDestroy {
  subscriptions: Subscriptions;
  algorithmForm: FormGroup;
  utilData: UtilData;
  arrayCanvasData: ArrayCanvasData;
  @ViewChild('array1Graph') array1Graph: ElementRef;
  @ViewChild('array2Graph') array2Graph: ElementRef;
  array1GraphIsVisible: boolean;
  array2GraphIsVisible: boolean;
  arrayGraphWidth: number;
  arrayGraphHeight: number;
  isGenerateArrayDisabled: boolean = true;

  constructor(private algorithmService: AlgorithmService, private fb: FormBuilder) {
      this.algorithmForm = this.fb.group({
        algorithmType1: new FormControl({ value: '', disabled: false }),
        algorithmType2: new FormControl({ value: '', disabled: false }),
        arrayLength: ''
      });
      this.resetData();
  }

  ngOnInit(): void {
    this.getAvailableAlgorithms();
  }

  ngAfterViewInit() {
    this.initializeCanvas();
  }

  getAvailableAlgorithms(): void {
    this.subscriptions.availableAlgorithmSub$ = this.algorithmService.getAvailableAlgorithms()
      .subscribe({
        next: data => {
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
          this.utilData.array = data.array;
          this.arrayGraphWidth = 20*data.array.length;
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
          if(arrayIdentifier == 1) {
            this.utilData.firstArrayIdRequester = data.idRequester;
            this.utilData.firstArrayMaxExecutionTime = data.maxExecutionTime;
            this.utilData.isFirstArrayCompleted = true;
          } else if(arrayIdentifier == 2) {
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

  startVisualization(arrayIdentifier: number): void {
    let obs;
    if(arrayIdentifier == 1) {
      obs = this.algorithmService.getExecutionData(this.utilData.firstArrayIdRequester, 1, this.utilData.firstArrayMaxExecutionTime);
    } else if (arrayIdentifier == 2) {
      obs = merge(this.algorithmService.getExecutionData(this.utilData.firstArrayIdRequester, 1, this.utilData.firstArrayMaxExecutionTime), this.algorithmService.getExecutionData(this.utilData.secondArrayIdRequester, 2, this.utilData.secondArrayMaxExecutionTime));
    }
    this.subscriptions.visualizationsSub$ = obs
      .subscribe({
        next: data => {
          var arrayIdentifier = data.arrayIdentifier;
          if(data.executionStatus!=0) {
            let executionData = new ExecutionData();
            executionData.array = data.array;
            if(arrayIdentifier == 1) {
              this.utilData.firstArrayExecutionData = executionData;
              this.drawArrayCanvas(1, false);
            } else if (arrayIdentifier == 2) {
              this.utilData.secondArrayExecutionData = executionData;
              this.drawArrayCanvas(2, false);
            }
          } else {
            this.completeArrayCanvas(arrayIdentifier);
          }
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
      this.array1GraphIsVisible = true;
      this.drawArrayCanvas(1, true);
      this.isGenerateArrayDisabled = true;
      this.algorithmForm.controls['algorithmType1'].disable();
    } else if(buttonType==="prepareArray2") {
      this.prepareVisualizationArray(2);
      this.array2GraphIsVisible = true;
      this.drawArrayCanvas(2, true);
      this.algorithmForm.controls['algorithmType2'].disable();
    } else if(buttonType==="run") {
      this.utilData.isRunning = true;
      if(this.utilData.isSecondArrayActive) {
        this.startVisualization(2);
      } else {
        this.startVisualization(1);
      }
    }
  }

  ngOnDestroy(): void {
    this.closeAllConnection();
  }

  resetAction(): void {
    this.resetData();
    this.clearCanvas();
    this.algorithmForm.controls['algorithmType1'].enable();
    this.algorithmForm.controls['algorithmType2'].enable();
    this.algorithmForm.controls['algorithmType1'].setValue('');
    this.algorithmForm.controls['algorithmType2'].setValue('');
    this.algorithmForm.controls['arrayLength'].setValue('');
  }

  resetData(): void {
    console.log('initializing data');
    this.closeAllConnection();
    var algorithms;
    if(this.utilData) {
      algorithms = this.utilData.algorithms;
    }
    this.utilData = new UtilData(false, false, false, false, new ExecutionData(), new ExecutionData());
    this.utilData.algorithms = algorithms;
    this.subscriptions = new Subscriptions();
    this.array1GraphIsVisible = false;
    this.array2GraphIsVisible = false;
    this.arrayGraphWidth = 1;
    this.arrayGraphHeight = 250;
    this.isGenerateArrayDisabled = false;
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
      if(this.subscriptions.visualizationsSub$) {
        this.subscriptions.visualizationsSub$.unsubscribe();
      }
    }
  }

  initializeCanvas(): void {
    this.arrayCanvasData = new ArrayCanvasData();
    this.arrayCanvasData.array1GraphCanvas = this.array1Graph;
    this.arrayCanvasData.array2GraphCanvas = this.array2Graph;
    this.arrayCanvasData.xCanvas = (this.arrayCanvasData.array1GraphCanvas.nativeElement as HTMLCanvasElement).width;
    this.arrayCanvasData.yCanvas = (this.arrayCanvasData.array1GraphCanvas.nativeElement as HTMLCanvasElement).height;
    let res1 = (this.arrayCanvasData.array1GraphCanvas.nativeElement as HTMLCanvasElement).getContext('2d');
    if (res1) {
      this.arrayCanvasData.array1ContextCanvas = res1;
    }
    let res2 = (this.arrayCanvasData.array2GraphCanvas.nativeElement as HTMLCanvasElement).getContext('2d');
    if (res2) {
      this.arrayCanvasData.array2ContextCanvas = res2;
    }
  }

  clearCanvas(): void {
    this.arrayCanvasData.array1ContextCanvas.clearRect(0, 0, this.arrayCanvasData.xCanvas, this.arrayCanvasData.yCanvas);
    this.arrayCanvasData.array2ContextCanvas.clearRect(0, 0, this.arrayCanvasData.xCanvas, this.arrayCanvasData.yCanvas);
  }

  completeArrayCanvas(arrayIdentifier: number): void {
    let executionData = new ExecutionData();
    if(arrayIdentifier == 1) {
      executionData.array = this.utilData.firstArrayExecutionData.array;
      this.drawBarChart(this.arrayCanvasData.array1ContextCanvas, executionData, true);
      this.addCompletedTitle(this.arrayCanvasData.array1ContextCanvas, 'COMPLETED');
    } else if(arrayIdentifier == 2) {
      executionData.array = this.utilData.secondArrayExecutionData.array;
      this.drawBarChart(this.arrayCanvasData.array2ContextCanvas, executionData, true);
      this.addCompletedTitle(this.arrayCanvasData.array2ContextCanvas, 'COMPLETED');
    }
  }

  addCompletedTitle(context, title): void {
    context.font = '14px sans-serif';
    context.fillStyle = 'yellow';
    context.fillText(title, this.arrayGraphWidth/2-70, this.arrayGraphHeight/2-100);
  }

  drawArrayCanvas(arrayIdentifier: number, firstDraw: boolean): void {
    if(arrayIdentifier == 1) {
      this.arrayCanvasData.array1ContextCanvas.fillStyle='#262a33';
      this.arrayCanvasData.array1ContextCanvas.fillRect(0, 0, this.arrayGraphWidth, this.arrayGraphHeight);
      if(firstDraw) {
        let executionData = new ExecutionData();
        executionData.array = new Array(this.utilData.array.length);
        for (let index = 0; index < this.utilData.array.length; index++) {
            let element = new ArrayInfo();
            element.value = this.utilData.array[index];
            element.color = '#36b5d8';
            executionData.array[index] = element;
        }
        this.drawBarChart(this.arrayCanvasData.array1ContextCanvas, executionData, false);
      } else {
        this.drawBarChart(this.arrayCanvasData.array1ContextCanvas, this.utilData.firstArrayExecutionData, false);
      }
      this.addTitleToChart(this.arrayCanvasData.array1ContextCanvas, 'Sorting with ' + this.algorithmForm.controls['algorithmType1'].value);
    } else if(arrayIdentifier == 2) {
      this.arrayCanvasData.array2ContextCanvas.fillStyle='#262a33';
      this.arrayCanvasData.array2ContextCanvas.fillRect(0, 0, this.arrayGraphWidth, this.arrayGraphHeight);
      if(firstDraw) {
        let executionData = new ExecutionData();
        executionData.array = new Array(this.utilData.array.length);
        for (let index = 0; index < this.utilData.array.length; index++) {
            let element = new ArrayInfo();
            element.value = this.utilData.array[index];
            element.color = '#36b5d8';
            executionData.array[index] = element;
        }
        this.drawBarChart(this.arrayCanvasData.array2ContextCanvas, executionData, false);
      } else {
        this.drawBarChart(this.arrayCanvasData.array2ContextCanvas, this.utilData.secondArrayExecutionData, false);
      }
      this.addTitleToChart(this.arrayCanvasData.array2ContextCanvas, 'Sorting with ' + this.algorithmForm.controls['algorithmType2'].value);
    }
  }

  addTitleToChart(context, title){
    context.font = '14px sans-serif';
    context.fillStyle = 'white';
    context.fillText(title, this.arrayGraphWidth/2-100, 12);
  }

  addColumnName(context, name, xpos, ypos){
    context.font = '10px sans-serif';
    context.fillStyle = 'white';
    context.fillText(name, xpos, ypos);
  }

  drawBarChart(context, executionData, completed) {
      for (let element = 0; element < executionData.array.length; element++) {
          if(completed) {
              // shade of green color
              context.fillStyle = "#00FF00";
          } else {
              // custom color
              context.fillStyle = executionData.array[element].color;
          }
          context.fillRect(5 + element * 20, this.arrayGraphHeight - executionData.array[element].value * 2 - 13, 10, executionData.array[element].value * 2);
          this.addColumnName(context, executionData.array[element].value, 5 + element * 20, this.arrayGraphHeight - 2);
      }
  }

}
