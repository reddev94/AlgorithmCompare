export class UtilData {
  isFirstArrayCompleted: boolean;
  isSecondArrayCompleted: boolean;
  isSecondArrayActive: boolean;
  firstArrayMaxExecutionTime: number;
  secondArrayMaxExecutionTime: number;
  firstArrayIdRequester: string;
  secondArrayIdRequester: string;
  algorithms: string[];
  array: number[];

  constructor(isFirstArrayCompleted: boolean, isSecondArrayCompleted: boolean, isSecondArrayActive: boolean) {
  this.isFirstArrayCompleted = isFirstArrayCompleted;
  this.isSecondArrayCompleted = isSecondArrayCompleted;
  this.isSecondArrayActive = isSecondArrayActive;
  }
}
