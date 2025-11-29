import {ChangeDetectionStrategy, Component, inject, OnInit, computed, signal} from '@angular/core';
import {AssessmentsService} from '../../core/service/assessments.service';
import {OverallProgressDTO, TaskProgressDTO} from '../../core/model/progress.dto';
import {timer} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {NgApexchartsModule} from 'ng-apexcharts';

@Component({
  selector: 'app-dashboard',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './dashboard.component.html',
  imports: [
    AsyncPipe,
    NgApexchartsModule
  ],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private readonly assessmentService = inject(AssessmentsService);

  private updateRate = 1000; // Poll every 1 second (1000ms)

  // Signals for reactive state
  overallProgress = signal<OverallProgressDTO | undefined>(undefined);
  taskProgress = signal<TaskProgressDTO[]>([]);

  // Derived state for charts
  completionSeries = computed(() => {
    const p = this.overallProgress();
    if (!p) return [0, 0];
    return [p.totalAssessmentsDone, p.totalMissing];
  });

  completionLabels = ['Done', 'Missing'];

  // Stable chart options to avoid re-creation and layout jumps
  completionChartOptions = {
    chart: { type: 'donut', animations: { enabled: false }, height: 320 },
    legend: { position: 'bottom' },
    colors: ['#22c55e', '#ef4444'],
    title: { text: 'Overall Completion' },
    responsive: [{ breakpoint: 1024, options: { chart: { height: 280 }, legend: { position: 'bottom' } } }]
  } as const;

  submissionsByTaskSeries = computed(() => {
    const tasks = this.taskProgress();
    return [{
      name: 'Submissions',
      data: tasks.map(t => t.totalSubmissions)
    }];
  });

  submissionsByTaskCategories = computed(() => this.taskProgress().map(t => t.taskName ?? t.taskId));

  submissionsChartOptions = {
    chart: { type: 'bar', animations: { enabled: false }, height: 360, toolbar: { show: false } },
    plotOptions: { bar: { horizontal: false, columnWidth: '55%' } },
    dataLabels: { enabled: false },
    colors: ['#3b82f6'],
    grid: { borderColor: 'var(--border)' },
    title: { text: 'Submissions by Task' }
  } as const;

  ngOnInit(): void {
    console.log("DashboardComponent ngOnInit - Setting up polling stream");

    timer(0, this.updateRate).subscribe(_ => {
      this.assessmentService.getOverallProgress().subscribe({
        next: data => {
          this.overallProgress.set(data);
        }
      });

      this.assessmentService.getTaskProgressForAll().subscribe({
        next: data => {
          this.taskProgress.set(data);
        }
      })
    })
  }
}
