import {inject, Injectable} from '@angular/core';
import {BaseService} from './base.service';
import {ApiService} from './api.service';
import {forkJoin, Observable} from 'rxjs';
import {AssessmentEventDTO} from '../model/assessment-event.dto';
import {OverallProgressDTO, TaskProgressDTO} from '../model/progress.dto';
import {TaskType} from '../model/task-type.enum';
import {GlobalProgressDto} from '../model/global-progress.dto';
import {CombinedProgressDataDto} from '../model/combined-progress-data.dto';

export interface AssessmentsListResponseDTO {
  items: AssessmentEventDTO[];
  page: number;
  pageSize: number;
  totalItems: number;
}

@Injectable({ providedIn: 'root' })
export class AssessmentsService extends BaseService {
  private readonly api = inject(ApiService);

  constructor() {
    super('AssessmentsService', 'http://localhost:8081/api/');
  }

  public getOverallProgress(): Observable<GlobalProgressDto> {
    return this.api.get(this.baseUrl, `progress/${1}/overall`)
  }

  public getTaskProgress(type: TaskType, id: number): Observable<TaskProgressDTO> {
    return this.api.get(this.baseUrl, `progress/${1}/type/${type}/tasks/${id}`);
  }

  public getTaskProgressForAll(): Observable<CombinedProgressDataDto> {

    return this.api.get(this.baseUrl, `progress/${1}/all`);
    //
    // let tasks: {type: TaskType, id: number}[] = [
    //   {type: TaskType.JWINF, id: 1},
    //   {type: TaskType.JWINF, id: 2},
    //   {type: TaskType.BWINF, id: 1},
    //   {type: TaskType.BWINF, id: 2},
    //   {type: TaskType.BWINF, id: 3},
    //   {type: TaskType.BWINF, id: 4},
    //   {type: TaskType.BWINF, id: 5},
    // ];
    //
    // return forkJoin(
    //   tasks.map(task =>
    //     this.api.get(this.baseUrl, `progress/${1}/type/${task.type}/tasks/${task.id}`)
    //   )
    // );

  }

}
