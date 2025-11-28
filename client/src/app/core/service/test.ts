import { Injectable } from '@angular/core';
import {BaseService} from './base.service';
import {ApiService} from './api.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TestService extends BaseService {

  constructor(
    private api: ApiService
  ) {
    super("TestService", "http://localhost:8080/");
  }

  public getTestData(): Observable<any> {
    return this.api.get(this.baseUrl, "test");
  }
}
