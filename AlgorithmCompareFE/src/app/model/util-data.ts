import { ExecutionData } from '../model/execution-data';

export class UtilData {
  isFirstArrayCompleted: boolean;
  isSecondArrayCompleted: boolean;
  isSecondArrayActive: boolean;
  isRunning: boolean;
  firstArrayMaxExecutionTime: number;
  secondArrayMaxExecutionTime: number;
  firstArrayIdRequester: string;
  secondArrayIdRequester: string;
  algorithms: string[];
  array: number[];
  firstArrayExecutionData: ExecutionData[];
  secondArrayExecutionData: ExecutionData[];

  constructor(isFirstArrayCompleted: boolean, isSecondArrayCompleted: boolean, isSecondArrayActive: boolean, isRunning: boolean, firstArrayExecutionData: ExecutionData[], secondArrayExecutionData: ExecutionData[]) {
    this.isFirstArrayCompleted = isFirstArrayCompleted;
    this.isSecondArrayCompleted = isSecondArrayCompleted;
    this.isSecondArrayActive = isSecondArrayActive;
    this.isRunning = isRunning;
    this.firstArrayExecutionData = firstArrayExecutionData;
    this.secondArrayExecutionData = secondArrayExecutionData;
  }
}
