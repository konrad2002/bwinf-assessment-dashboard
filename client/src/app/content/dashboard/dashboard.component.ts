import {ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, OnInit} from '@angular/core';
import {AssessmentsService} from '../../core/service/assessments.service';
import {OverallProgressDTO, TaskProgressDTO} from '../../core/model/progress.dto';
import {timer} from 'rxjs';
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
  private readonly cdr: ChangeDetectorRef = inject(ChangeDetectorRef);

  private updateRate = 1000; // Poll every 1 second (1000ms)

  overallProgress?: OverallProgressDTO;
  taskProgress: TaskProgressDTO[] = [];

  ngOnInit(): void {
    console.log("DashboardComponent ngOnInit - Setting up polling stream");

    timer(0, this.updateRate).subscribe(_ => {
      this.assessmentService.getOverallProgress().subscribe({
        next: data => {
          this.overallProgress = data;
          this.cdr.detectChanges();
        }
      });

      this.assessmentService.getTaskProgressForAll().subscribe({
        next: data => {
          this.taskProgress = data;
          this.cdr.detectChanges();
        }
      })
    })
  }
}
