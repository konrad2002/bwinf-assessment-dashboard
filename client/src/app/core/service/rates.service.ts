import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { ApiService } from './api.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AssessmentRateResponseDTO, AssessmentRateSeriesDTO } from '../model/assessment-rate.dto';
import {HttpParams} from '@angular/common/http';

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
    const params: HttpParams = new HttpParams();
    params.set('from', from);
    params.set('to', to);
    params.set('bucket', bucket);

    if (tasks && tasks.length) {
      params.set('task', tasks.join(','));
    }
    return this.api.get(this.baseUrl, '', params);
  }

  public getTaskRate(
    taskId: string,
    from: string,
    to: string,
    bucket: BucketSize = 'hour'
  ): Observable<AssessmentRateSeriesDTO> {
    const params: HttpParams = new HttpParams();
    params.set('from', from);
    params.set('to', to);
    params.set('bucket', bucket);
    return this.api.get(this.baseUrl + '/tasks/', encodeURIComponent(taskId), params);
  }
}
