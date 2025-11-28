import { ChangeDetectionStrategy, Component, input } from '@angular/core';

@Component({
  selector: 'app-metric-tile',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    class: 'metric-tile',
    role: 'group',
    'aria-label': 'Metric'
  },
  templateUrl: './metric-tile.component.html',
  styleUrls: ['./metric-tile.component.scss']
})
export class MetricTileComponent {
  readonly label = input<string>('');
  readonly value = input<number>(0);
  readonly unit = input<string | undefined>(undefined);
  readonly trend = input<'up' | 'down' | 'flat' | undefined>(undefined);
}
