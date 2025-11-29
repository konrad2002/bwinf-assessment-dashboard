import {Component, input} from '@angular/core';
import {TaskProgressDto} from '../../core/model/task-progress.dto';
import {ProgressDataDto} from '../../core/model/progress-data.dto';
import {ProgressBar} from '../progress-bar/progress-bar';
import {TaskProgressDTO} from '../../core/model/progress.dto';
import getTaskName = TaskProgressDTO.getTaskName;
import {TaskType} from '../../core/model/task-type.enum';

@Component({
  selector: 'app-progress-view',
  imports: [
    ProgressBar
  ],
  templateUrl: './progress-view.html',
  styleUrl: './progress-view.scss',
})
export class ProgressView {
  readonly overall = input<ProgressDataDto>({} as ProgressDataDto);
  readonly tasks = input<TaskProgressDto[]>([]);
  protected readonly TaskProgressDTO = TaskProgressDTO;
  protected readonly getTaskName = getTaskName;
  protected readonly TaskType = TaskType;
}
