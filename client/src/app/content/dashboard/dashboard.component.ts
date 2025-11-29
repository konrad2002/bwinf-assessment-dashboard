import {ChangeDetectionStrategy, Component, inject, OnInit, OnDestroy, signal} from '@angular/core';
import {AssessmentsService} from '../../core/service/assessments.service';
import {OverallProgressDTO, TaskProgressDTO} from '../../core/model/progress.dto';
import {timer, forkJoin, Subscription} from 'rxjs';
import {switchMap, shareReplay} from 'rxjs/operators';
import {AsyncPipe, DecimalPipe} from '@angular/common';
import {NgApexchartsModule} from 'ng-apexcharts';
import {TaskType} from '../../core/model/task-type.enum';
import getTaskName = TaskProgressDTO.getTaskName;

@Component({
  selector: 'app-dashboard',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './dashboard.component.html',
  imports: [
    AsyncPipe,
    DecimalPipe,
    NgApexchartsModule
  ],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  private readonly assessmentService = inject(AssessmentsService);

  private updateRate = 1000 * 10; // Poll every 1 second (1000ms)

  // Signals for reactive state
  overallProgress = signal<OverallProgressDTO | undefined>(undefined);
  taskProgress = signal<TaskProgressDTO[]>([]);

  // Chart data signals (avoid re-creating arrays to minimize reflow)
  completionSeriesSignal = signal<number[]>([0, 0]);
  completionLabels = ['Done', 'Missing'];

  // Stable chart options to avoid re-creation and layout jumps
  completionChartOptions = {
    chart: { type: 'donut', animations: { enabled: false }, height: 320 },
    legend: { position: 'bottom' },
    colors: ['#22c55e', '#ef4444'],
    title: { text: 'Overall Completion' },
    tooltip: { theme: 'dark' as const },
    responsive: [{ breakpoint: 1024, options: { chart: { height: 280 }, legend: { position: 'bottom' } } }]
  } as const;

  submissionsSeriesSignal = signal<{ name: string; data: number[] }[]>([{ name: 'Submissions', data: [] }]);
  submissionsCategoriesSignal = signal<string[]>([]);

  submissionsChartOptions = {
    chart: { type: 'bar', animations: { enabled: false }, height: 360, toolbar: { show: false } },
    plotOptions: { bar: { horizontal: false, columnWidth: '55%' } },
    dataLabels: { enabled: false },
    colors: ['#3b82f6'],
    grid: { borderColor: 'var(--border)' },
    title: { text: 'Submissions by Task' },
    tooltip: { theme: 'dark' as const }
  } as const;

  private pollSub?: Subscription;

  ngOnInit(): void {
    console.log('DashboardComponent ngOnInit - Setting up consolidated polling');

    const poll$ = timer(0, this.updateRate).pipe(
      switchMap(() => forkJoin({
        overall: this.assessmentService.getOverallProgress(),
        tasks: this.assessmentService.getTaskProgressForAll()
      })),
      shareReplay(1)
    );

    this.pollSub = poll$.subscribe(({ overall, tasks }) => {
      this.overallProgress.set(overall);
      this.taskProgress.set(tasks);
      // Update chart data signals atomically
      this.completionSeriesSignal.set([overall.totalAssessmentsDone, overall.totalMissing]);
      this.submissionsSeriesSignal.set([{ name: 'Submissions', data: tasks.map(t => t.totalSubmissions) }]);
      this.submissionsCategoriesSignal.set(tasks.map(t => getTaskName(t)));
    });
  }

  ngOnDestroy(): void {
    this.pollSub?.unsubscribe();
  }

  protected readonly getTaskName = getTaskName;
}
