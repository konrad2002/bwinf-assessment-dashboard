import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { ApiService } from './api.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AssessmentEventDTO } from '../model/assessment-event.dto';

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
    const params: Record<string, string> = {};
    if (options.from) params['from'] = options.from;
    if (options.to) params['to'] = options.to;
    if (options.taskId) params['taskId'] = options.taskId;
    if (options.assessorId) params['assessorId'] = options.assessorId;
    if (options.page) params['page'] = String(options.page);
    if (options.pageSize) params['pageSize'] = String(options.pageSize);
    return this.api.getWithParams(this.baseUrl, '', params);
  }
}
