export class AlgorithmExecutionData {
  array: number[];
  moveExecutionTime: number;
  resultCode: number;
  resultDescription: string;

  constructor( array: number[], moveExecutionTime: number, resultCode: number, resultDescription: string) {
      this.array = array;
      this.moveExecutionTime = moveExecutionTime;
      this.resultCode = resultCode;
      this.resultDescription = resultDescription;
    }
}
