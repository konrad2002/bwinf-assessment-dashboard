import {CombinedProgressDataDto} from './combined-progress-data.dto';
import {CorrectorDto} from './corrector.dto';

export interface EventSseDataDto {
  taskType: TaskType;
  taskNumber: number;
  correctorDto: CorrectorDto;
  combinedProgressDataPointDto: CombinedProgressDataDto;
}
