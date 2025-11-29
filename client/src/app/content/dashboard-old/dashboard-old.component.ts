import {ChangeDetectionStrategy, Component, inject, OnInit, OnDestroy, signal} from '@angular/core';
import {AssessmentsService} from '../../core/service/assessments.service';
import {TaskProgressDTO} from '../../core/model/progress.dto';
import {timer, forkJoin, Subscription} from 'rxjs';
import {switchMap, shareReplay} from 'rxjs/operators';
import {AsyncPipe, DecimalPipe} from '@angular/common';
import {NgApexchartsModule} from 'ng-apexcharts';
import getTaskName = TaskProgressDTO.getTaskName;
import {CombinedProgressDataDto} from '../../core/model/combined-progress-data.dto';

@Component({
  selector: 'app-dashboard-old-old',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './dashboard-old.component.html',
  imports: [
    DecimalPipe,
    NgApexchartsModule
  ],
  styleUrls: ['./dashboard-old.component.scss']
})
export class DashboardOldComponent implements OnInit, OnDestroy {
  private readonly assessmentService = inject(AssessmentsService);

  private updateRate = 1000 * 10; // Poll every 1 second (1000ms)

  // Signals for reactive state
  progress = signal<CombinedProgressDataDto | undefined>(undefined);

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
        progress: this.assessmentService.getTaskProgressForAll(),
      })),
      shareReplay(1)
    );

    this.pollSub = poll$.subscribe(({ progress }) => {
      this.progress.set(progress);
      // Update chart data signals atomically
      this.completionSeriesSignal.set([progress.globalProgressDataPoint.progressDataPointDto.onlyFirstDone + progress.globalProgressDataPoint.progressDataPointDto.done, progress.globalProgressDataPoint.progressDataPointDto.secondMissing + progress.globalProgressDataPoint.progressDataPointDto.firstMissing]);
      this.submissionsSeriesSignal.set([{ name: 'Submissions', data: progress.taskProgressDataPointDtos.map(t => t.progressDataPointDto.totalSubmissions) }]);
      this.submissionsCategoriesSignal.set(progress.taskProgressDataPointDtos.map(t => getTaskName(t)));
    });
  }

  getFirstPercentage() {
    return 100.0 * (this.progress()?.globalProgressDataPoint?.progressDataPointDto?.onlyFirstDone ?? 0) / (this.progress()?.globalProgressDataPoint?.progressDataPointDto?.totalSubmissions ?? 1)
  }

  getSecondPercentage() {
    return 100.0 * (this.progress()?.globalProgressDataPoint?.progressDataPointDto?.done ?? 0) / (this.progress()?.globalProgressDataPoint?.progressDataPointDto?.totalSubmissions ?? 1)
  }

  getOverallPercentage() {
    return 100.0 * ((this.progress()?.globalProgressDataPoint?.progressDataPointDto?.onlyFirstDone ?? 0) + (this.progress()?.globalProgressDataPoint?.progressDataPointDto?.done ?? 0)) / (this.progress()?.globalProgressDataPoint?.progressDataPointDto?.totalRequiredEvaluations ?? 1)
  }

  ngOnDestroy(): void {
    this.pollSub?.unsubscribe();
  }

  protected readonly getTaskName = getTaskName;
}
