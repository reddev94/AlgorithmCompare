import { ChartOptions, ChartType, ChartDataSets } from 'chart.js';
import { Label } from 'ng2-charts';

export class ChartData {
  barChartOptions: ChartOptions = {
    responsive: true,
  };
  barChartLabels: Label[];
  barChartType: ChartType = 'bar';
  barChartLegend = false;
  barChartPlugins = [];
  barChartData: ChartDataSets[];

  constructor(barChartLabels: Label[], barChartPlugins: [],  barChartData: ChartDataSets[]) {
    this.barChartLabels = barChartLabels;
    this.barChartPlugins = barChartPlugins;
    this.barChartData = barChartData;
  }
}
