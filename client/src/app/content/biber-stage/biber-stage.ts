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
    '(animationend)': 'handleAnimationEnd($event)'
  }
})
export class BiberStage {
  readonly biberCount = input<{id: number, corrector: CorrectorDto}[]>([]);
  readonly remove = output<number>();

  // simple lane assignment to avoid vertical overlap
  private lanes = new Map<number, number>();
  private readonly laneCount = 4;

  laneFor(id: number): number {
    if (!this.lanes.has(id)) {
      const next = (this.lanes.size % this.laneCount);
      this.lanes.set(id, next);
    }
    return this.lanes.get(id)!;
  }

  handleAnimationEnd(evt: AnimationEvent) {
    const target = evt.target as HTMLElement | null;
    if (!target) return;
    // only react to biber items
    if (!target.classList.contains('biber')) return;
    const idAttr = target.dataset['id'];
    const id = idAttr ? Number(idAttr) : NaN;
    if (!Number.isNaN(id)) {
      this.remove.emit(id);
    }
  }
}
