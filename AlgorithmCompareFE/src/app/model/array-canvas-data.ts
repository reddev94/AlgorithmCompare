import { ViewChild, ElementRef } from '@angular/core';

export class ArrayCanvasData {
  array1GraphCanvas: ElementRef;
  array2GraphCanvas: ElementRef;
  array1ContextCanvas: CanvasRenderingContext2D;
  array2ContextCanvas: CanvasRenderingContext2D;
  xCanvas: number;
  yCanvas: number;

  constructor() {
  }
}
