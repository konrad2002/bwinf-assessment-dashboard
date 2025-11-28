import {ChangeDetectionStrategy, Component, inject, OnInit} from '@angular/core';
import { RouterLink } from '@angular/router';
import { ContentService } from '../content.service';
import { MetricTileComponent } from '../widgets/metric-tile/metric-tile.component';
import { LineChartComponent } from '../widgets/line-chart/line-chart.component';
import { DataTableComponent } from '../widgets/data-table/data-table.component';
import { ItemListComponent } from '../widgets/item-list/item-list.component';
import {TestService} from '../../core/service/test';

@Component({
  selector: 'app-dashboard',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, MetricTileComponent, LineChartComponent, DataTableComponent, ItemListComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  readonly content = inject(ContentService);

  test: string = "";

  constructor(
    private testService: TestService
  ) {
  }

  ngOnInit() {
    console.log("DashboardComponent ngOnInit");
    this.testService.getTestData().subscribe({
        next: data => {
          this.test = data;
        }
      }
    )
  }
}
