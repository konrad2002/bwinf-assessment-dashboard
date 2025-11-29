import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  input,
  OnDestroy,
  OnInit,
  signal
} from '@angular/core';
import {AssessmentsService} from '../../core/service/assessments.service';

import {ProgressDataDto} from '../../core/model/progress-data.dto';
import {TaskProgressDto} from '../../core/model/task-progress.dto';
import {ProgressView} from '../progress-view/progress-view';
import {SubscriptionLike} from 'rxjs';
import {EventSourceService} from '../../core/service/events.service';
import {ProgressBar} from '../progress-bar/progress-bar';
import {BiberStage} from '../biber-stage/biber-stage';
import {CorrectorDto} from '../../core/model/corrector.dto';
import {EventSseDataDto} from '../../core/model/event-sse-data.dto';

@Component({
  selector: 'app-dashboard-old',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './dashboard.component.html',
  imports: [
    ProgressView,
    ProgressBar,
    BiberStage
  ],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  private assessmentService = inject(AssessmentsService);
  private cdr = inject(ChangeDetectorRef);
  private eventService = inject(EventSourceService)

  dummy = signal<number>(5);

  bibers: { id: number, corrector: CorrectorDto }[] = [];

  private eventSourceSubscription?: SubscriptionLike;

  overall?: ProgressDataDto
  tasks: TaskProgressDto[] = [];

  ngOnInit(): void {
    this.assessmentService.getTaskProgressForAll().subscribe({
      next: data => {
        this.overall = data.globalProgressDataPoint.progressDataPointDto;
        this.tasks = data.taskProgressDataPointDtos;
        this.cdr.detectChanges();
      }
    });

    const url = 'http://localhost:8081/api/event/sse';

    this.eventSourceSubscription = this.eventService.connectToServerSentEvents(url)
      .subscribe({
          next: data => {
            console.log(data);

            const progressData: EventSseDataDto = JSON.parse(data.data);

            this.overall = progressData.combinedProgressDataPointDto.globalProgressDataPoint.progressDataPointDto;
            this.tasks = progressData.combinedProgressDataPointDto.taskProgressDataPointDtos;

            this.dummy.set(this.dummy() + 1)
            this.bibers.push({
              id: this.dummy(),
              corrector: progressData.correctorDto
            });
            this.cdr.detectChanges();
          },
          error: error => {
            //handle error
          }
        }
      );
  }

  ngOnDestroy() {
    if (this.eventSourceSubscription)
      this.eventSourceSubscription.unsubscribe();
    this.eventService.close();
  }

  onBiberDone(id: number) {
    const idx = this.bibers.findIndex(b => b.id === id);
    if (idx >= 0) {
      this.bibers.splice(idx, 1);
      this.cdr.detectChanges();
    }
  }
}
