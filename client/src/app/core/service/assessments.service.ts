import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { ApiService } from './api.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AssessmentEventDTO } from '../model/assessment-event.dto';
import {HttpParams} from '@angular/common/http';

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
    super('AssessmentsService', '/api/assessments');
  }

  public list(
    options: {
      from?: string;
      to?: string;
      taskId?: string;
      assessorId?: string;
      page?: number;
      pageSize?: number;
    } = {}
  ): Observable<AssessmentsListResponseDTO> {
    const params: HttpParams = new HttpParams();
    if (options.from) params.set('from', options.from);
    if (options.to) params.set('to', options.to);
    if (options.taskId) params.set('taskId', options.taskId);
    if (options.assessorId) params.set('assessorId', options.assessorId);
    if (options.page) params.set('page', options.page);
    if (options.pageSize) params.set('pageSize', options.pageSize);

    return this.api.get(this.baseUrl, '', params);
  }
}
