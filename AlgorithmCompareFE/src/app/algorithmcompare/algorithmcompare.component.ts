import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { AlgorithmService } from '../service/algorithm-service';
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { UtilData } from '../model/util-data';
import { Subscriptions } from '../model/subscriptions';
import { Observable, forkJoin, merge } from 'rxjs';
import { AlgorithmExecutionData } from '../model/algorithm-data';
import { ArrayCanvasData } from '../model/array-canvas-data';

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
  arrayGraphWidth = 1200;
  arrayGraphHeight = 260;


  constructor(private algorithmService: AlgorithmService, private fb: FormBuilder) {
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
          console.log(data);
          var arrayIdentifier = data.arrayIdentifier;
          console.log('response data array'+arrayIdentifier);
          if(arrayIdentifier == 1) {
            this.utilData.firstArrayExecutionData = data.array;
            this.drawArrayCanvas(1, false);
          } else if (arrayIdentifier == 2) {
            this.utilData.secondArrayExecutionData = data.array;
            this.drawArrayCanvas(2, false);
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
      this.array1GraphIsVisible = true;
      this.drawArrayCanvas(1, true);
    } else if(buttonType==="prepareArray2") {
      this.prepareVisualizationArray(2);
      this.array2GraphIsVisible = true;
      this.drawArrayCanvas(2, true);
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
  }

  resetData(): void {
    console.log('initializing data');
    this.closeAllConnection();
    var algorithms;
    if(this.utilData) {
      algorithms = this.utilData.algorithms;
    }
    this.utilData = new UtilData(false, false, false, false);
    this.utilData.algorithms = algorithms;
    this.algorithmForm = this.fb.group({
      algorithmType1: '',
      algorithmType2: '',
      arrayLength: ''
    });
    this.subscriptions = new Subscriptions();
    this.array1GraphIsVisible = false;
    this.array2GraphIsVisible = false;
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

  drawArrayCanvas(arrayIdentifier: number, firstDraw: boolean): void {
    if(arrayIdentifier == 1) {
      this.arrayCanvasData.array1ContextCanvas.fillStyle='#262a33';
      this.arrayCanvasData.array1ContextCanvas.fillRect(0, 0, this.arrayGraphWidth, this.arrayGraphHeight);
      if(firstDraw) {
        this.drawBarChart(this.arrayCanvasData.array1ContextCanvas, this.utilData.array);
      } else {
        this.drawBarChart(this.arrayCanvasData.array1ContextCanvas, this.utilData.firstArrayExecutionData);
      }
      this.addTitleToChart(this.arrayCanvasData.array1ContextCanvas, 'Array 1');
    } else if(arrayIdentifier == 2) {
      this.arrayCanvasData.array2ContextCanvas.fillStyle='#262a33';
      this.arrayCanvasData.array2ContextCanvas.fillRect(0, 0, this.arrayGraphWidth, this.arrayGraphHeight);
      if(firstDraw) {
        this.drawBarChart(this.arrayCanvasData.array2ContextCanvas, this.utilData.array);
      } else {
        this.drawBarChart(this.arrayCanvasData.array2ContextCanvas, this.utilData.secondArrayExecutionData);
      }
      this.addTitleToChart(this.arrayCanvasData.array2ContextCanvas, 'Array 2');
    }
  }

  addTitleToChart(context, title){
    context.font = '16px sans-serif';
    context.fillStyle = 'white';
    context.fillText(title, this.arrayGraphWidth/2-30, 15);
  }

  addColumnName(context,name,xpos,ypos){
    context.font = '12px sans-serif';
    context.fillStyle = 'white';
    context.fillText(name, xpos, ypos);
  }

  drawBarChart(context, array){
    for(let element=0; element<array.length; element++) {
      context.fillStyle = "#36b5d8";
      context.fillRect(5 + element*40, this.arrayGraphHeight-array[element]*2-20, 30, array[element]*2);
      this.addColumnName(context, array[element], 13 + element*40, this.arrayGraphHeight-5);
    }
  }

}
