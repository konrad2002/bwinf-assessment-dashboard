import {ChangeDetectionStrategy, Component, inject, OnInit} from '@angular/core';
import {AssessmentsService} from '../../core/service/assessments.service';
import {OverallProgressDTO} from '../../core/model/progress.dto';
import {map, Observable, of, switchMap, timer} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-dashboard',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './dashboard.component.html',
  imports: [
    AsyncPipe
  ],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private readonly assessmentService = inject(AssessmentsService);
  private updateRate = 1000; // Poll every 1 second (1000ms)

  overallProgress$!: Observable<OverallProgressDTO | undefined>;

  ngOnInit(): void {
    console.log("DashboardComponent ngOnInit - Setting up polling stream");

    this.overallProgress$ = timer(0, this.updateRate).pipe(
      switchMap(() => this.assessmentService.getOverallProgress().pipe(

        map(data => data as OverallProgressDTO),

        catchError(error => {
          console.error('Error fetching progress, stream continues:', error);
          return of({} as OverallProgressDTO);
        })
      ))
    );
  }
}
