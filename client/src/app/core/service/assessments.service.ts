import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { ApiService } from './api.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AssessmentEventDTO } from '../model/assessment-event.dto';
import {HttpParams} from '@angular/common/http';
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

}
