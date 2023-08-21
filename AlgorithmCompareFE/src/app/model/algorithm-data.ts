export interface AlgorithmExecutionData {
  array: ArrayInfo[];
  moveExecutionTime: number;
  executionStatus: number;
  arrayIdentifier: number;
}

export class ArrayInfo {
  value: number;
  color: string;

  constructor() {
  }
}
