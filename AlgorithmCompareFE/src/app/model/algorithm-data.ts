export interface AlgorithmExecutionData {
  array: number[];
  moveExecutionTime: number;
  swappedElementInfo: SwappedElementInfo[];
  executionStatus: number;
  arrayIdentifier: number;
}

export interface SwappedElementInfo {
  index: number;
  color: string;
}
