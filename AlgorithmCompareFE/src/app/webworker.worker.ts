// /// <reference lib="webworker" />
// import { DoWork, ObservableWorker } from 'observable-webworker';
// import { Observable, asyncScheduler } from 'rxjs';
// import { map } from 'rxjs/operators';
// import { AlgorithmExecutionData } from './model/algorithm-data';
// import { subscribeOn } from 'rxjs/operators';
//
// @ObservableWorker()
// export class DemoWorker implements DoWork<AlgorithmExecutionData, AlgorithmExecutionData> {
//
//   public work(input$: Observable<AlgorithmExecutionData>): Observable<AlgorithmExecutionData> {
//     console.log(`Got message work` + input$);
//     return input$.pipe(
//       map(function(res){
//         var data: AlgorithmExecutionData = res;
//         return data;
//       })
//       )/*.pipe(subscribeOn(asyncScheduler))*/;
//   }
//
// }
