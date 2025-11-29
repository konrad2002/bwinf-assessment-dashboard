import {inject, Injectable} from '@angular/core';
import {BaseService} from './base.service';
import {ApiService} from './api.service';
import {forkJoin, Observable} from 'rxjs';
import {AssessmentEventDTO} from '../model/assessment-event.dto';
import {OverallProgressDTO, TaskProgressDTO} from '../model/progress.dto';
import {TaskType} from '../model/task-type.enum';

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

  public getOverallProgress(): Observable<OverallProgressDTO> {
    return this.api.get(this.baseUrl, `progress/${1}/overall`)
  }

  public getTaskProgress(type: TaskType, id: number): Observable<TaskProgressDTO> {
    return this.api.get(this.baseUrl, `progress/${1}/type/${type}/tasks/${id}`);
  }

  public getTaskProgressForAll(): Observable<TaskProgressDTO[]> {
    let tasks: {type: TaskType, id: number}[] = [
      {type: TaskType.JWINF, id: 1},
      {type: TaskType.JWINF, id: 2},
      {type: TaskType.BWINF, id: 1},
      {type: TaskType.BWINF, id: 2},
      {type: TaskType.BWINF, id: 3},
      {type: TaskType.BWINF, id: 4},
      {type: TaskType.BWINF, id: 5},
    ];

    return forkJoin(
      tasks.map(task =>
        this.api.get(this.baseUrl, `progress/${1}/type/${task.type}/tasks/${task.id}`)
      )
    );

    //return this.api.get(this.baseUrl, `progress/${1}/all`);
  }

}
