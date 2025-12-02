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
            console.log('SSE event received:', data);

            const progressData: EventSseDataDto = JSON.parse(data.data);

            this.overall = progressData.combinedProgressDataPointDto.globalProgressDataPoint.progressDataPointDto;
            this.tasks = progressData.combinedProgressDataPointDto.taskProgressDataPointDtos;

            this.dummy.set(this.dummy() + 1);
            const newBiber = {
              id: this.dummy(),
              corrector: progressData.correctorDto
            };
            console.log('Adding new biber:', newBiber);
            // Create new array reference for OnPush change detection
            this.bibers = [...this.bibers, newBiber];
            console.log('Total bibers now:', this.bibers.length);
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
    console.log('Removing biber with id:', id, 'Current bibers:', this.bibers.length);
    const idx = this.bibers.findIndex(b => b.id === id);
    if (idx >= 0) {
      // Create new array reference for OnPush change detection
      this.bibers = this.bibers.filter(b => b.id !== id);
      console.log('Biber removed. Remaining:', this.bibers.length);
      this.cdr.detectChanges();
    } else {
      console.warn('Biber id not found:', id);
    }
  }
}
