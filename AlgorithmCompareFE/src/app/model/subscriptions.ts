import { Subscription } from 'rxjs';

export class Subscriptions {
  availableAlgorithmSub$: Subscription;
  generateArraySub$: Subscription;
  executeAlgorithmSub$: Subscription;
  getExecutionDataSub1$: Subscription;
  getExecutionDataSub2$: Subscription;

  constructor() {
  }
}
