import { Component, OnInit } from '@angular/core';
import { AlgorithmService } from '../service/algorithm-service';
import { AlgorithmAvailable } from '../model/algorithm-available';

@Component({
  selector: 'app-algorithmcompare',
  templateUrl: './algorithmcompare.component.html',
  styleUrls: ['./algorithmcompare.component.css']
})
export class AlgorithmcompareComponent implements OnInit {
  //availableAlgorithmSub$: Subscription;
  availableAlgorithmsResponse: AlgorithmAvailable;

  constructor(private algorithmService: AlgorithmService) {
  }

  ngOnInit(): void {
    this.getAvailableAlgorithms();
  }

  getAvailableAlgorithms(): void {
    this.algorithmService.getAvailableAlgorithms().subscribe(data => this.availableAlgorithmsResponse = data);
  }
}
