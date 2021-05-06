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
  firstArrayExecutionData: number[];
  secondArrayExecutionData: number[];

  constructor(isFirstArrayCompleted: boolean, isSecondArrayCompleted: boolean, isSecondArrayActive: boolean, isRunning: boolean) {
    this.isFirstArrayCompleted = isFirstArrayCompleted;
    this.isSecondArrayCompleted = isSecondArrayCompleted;
    this.isSecondArrayActive = isSecondArrayActive;
    this.isRunning = isRunning;
  }
}
