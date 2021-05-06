import { Subscription } from 'rxjs';

export class Subscriptions {
  availableAlgorithmSub$: Subscription;
  generateArraySub$: Subscription;
  executeAlgorithmSub$: Subscription;
  visualizationsSub$: Subscription;

  constructor() {
  }
}
