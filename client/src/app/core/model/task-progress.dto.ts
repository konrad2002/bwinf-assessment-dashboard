import {TaskType} from './task-type.enum';
import {ProgressDataDto} from './progress-data.dto';

export interface TaskProgressDto {
  taskType: TaskType;
  taskNumber: number;
  progressDataPointDto: ProgressDataDto;
  createdAt: string;
}
