import {GlobalProgressDto} from './global-progress.dto';
import {TaskProgressDto} from './task-progress.dto';

export interface CombinedProgressDataDto {
  globalProgressDataPoint: GlobalProgressDto
  taskProgressDataPointDtos: TaskProgressDto[];
}
