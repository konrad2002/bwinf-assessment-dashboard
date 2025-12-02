import {ChangeDetectionStrategy, Component, input, output} from '@angular/core';
import {Biber} from '../biber/biber';
import {CorrectorDto} from '../../core/model/corrector.dto';

@Component({
  selector: 'app-biber-stage',
  imports: [
    Biber
  ],
  templateUrl: './biber-stage.html',
  styleUrl: './biber-stage.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '(animationend)': 'handleAnimationEnd($event)',
    style: 'height: 200px'
  }
})
export class BiberStage {
  readonly biberCount = input<{id: number, corrector: CorrectorDto}[]>([]);
  readonly remove = output<number>();

  // number of vertical lanes visible at once
  readonly laneCount = 4;

  // small deterministic delay to reduce lockstep movement
  delayFor(id: number): number {
    const base = (id % 7) * 0.12; // 0..0.72s
    return Number(base.toFixed(2));
  }

  handleAnimationEnd(evt: AnimationEvent) {
    const target = evt.target as HTMLElement | null;
    if (!target) return;

    // Check if this is an app-biber element
    const biberElement = target.tagName.toLowerCase() === 'app-biber' ? target : null;
    if (!biberElement) return;

    // Only process 'walk' animation to avoid conflicts with other animations
    if (evt.animationName !== 'walk') return;

    const idAttr = biberElement.dataset['id'];
    const id = idAttr ? Number(idAttr) : NaN;
    if (!Number.isNaN(id)) {
      console.log('Biber animation ended, removing id:', id);
      this.remove.emit(id);
    }
  }
}
