import {Component, input} from '@angular/core';
import {Biber} from '../biber/biber';
import {CorrectorDto} from '../../core/model/corrector.dto';

@Component({
  selector: 'app-biber-stage',
  imports: [
    Biber
  ],
  templateUrl: './biber-stage.html',
  styleUrl: './biber-stage.scss',
})
export class BiberStage {
  readonly biberCount = input<{id: number, corrector: CorrectorDto}[]>([]);
}
