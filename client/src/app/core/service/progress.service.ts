import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { ApiService } from './api.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { OverallProgressDTO, TaskProgressDTO } from '../model/progress.dto';

@Injectable({ providedIn: 'root' })
export class ProgressService extends BaseService {
  private readonly api = inject(ApiService);

  constructor() {
    super('ProgressService', '/api/progress/');
  }

  public getOverallProgress(): Observable<OverallProgressDTO> {
    return this.api.get(this.baseUrl, 'overall');
  }

  public getTaskProgress(taskId: string): Observable<TaskProgressDTO> {
    return this.api.get(this.baseUrl + 'tasks/', encodeURIComponent(taskId));
  }
}
