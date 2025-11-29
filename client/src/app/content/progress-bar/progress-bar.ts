import { ChangeDetectionStrategy, Component, computed, input, signal } from '@angular/core';
import {DecimalPipe} from '@angular/common';

@Component({
  selector: 'app-progress-bar',
  imports: [
    DecimalPipe
  ],
  templateUrl: './progress-bar.html',
  styleUrl: './progress-bar.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    style: 'display:block; width:100%'
  }
})
export class ProgressBar {
  readonly value = input<number>(0);
  readonly max = input<number | undefined>(undefined);
  readonly percentage = input<number | undefined>(undefined);
  readonly color = input<string>('#3b82f6');

  private readonly safeClamp = (val: number) => Math.max(0, Math.min(100, val));

  readonly percent = computed(() => {
    const pct = this.percentage();
    if (typeof pct === 'number' && !Number.isNaN(pct)) {
      return this.safeClamp(pct);
    }

    const max = this.max();
    const value = this.value();
    if (typeof max === 'number' && max > 0) {
      return this.safeClamp((value / max) * 100);
    }
    return this.safeClamp(value);
  });
}
