import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { ApiService } from './api.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AssessmentRateResponseDTO, AssessmentRateSeriesDTO } from '../model/assessment-rate.dto';

type BucketSize = 'minute' | 'hour' | 'day';

@Injectable({ providedIn: 'root' })
export class RatesService extends BaseService {
  private readonly api = inject(ApiService);

  constructor() {
    super('RatesService', '/api/rates');
  }

  public getRates(
    from: string,
    to: string,
    bucket: BucketSize = 'hour',
    tasks?: string[]
  ): Observable<AssessmentRateResponseDTO> {
    const params: Record<string, string> = {
      from,
      to,
      bucket,
    };
    if (tasks && tasks.length) {
      params['tasks'] = tasks.join(',');
    }
    return this.api.getWithParams(this.baseUrl, '', params);
  }

  public getTaskRate(
    taskId: string,
    from: string,
    to: string,
    bucket: BucketSize = 'hour'
  ): Observable<AssessmentRateSeriesDTO> {
    const params: Record<string, string> = { from, to, bucket };
    return this.api.getWithParams(this.baseUrl + '/tasks/', encodeURIComponent(taskId), params);
  }
}
