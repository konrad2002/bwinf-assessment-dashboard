import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { Observable } from 'rxjs';
import { LiveEventEnvelopeDTO, AssessmentEventDTO } from '../model/assessment-event.dto';

@Injectable({ providedIn: 'root' })
export class EventsService extends BaseService {
  constructor() {
    super('EventsService', '/api/events');
  }

  // Streams assessment completion events via SSE.
  // Consumers should unsubscribe to close the connection.
  public streamAssessments(): Observable<AssessmentEventDTO> {
    return new Observable<AssessmentEventDTO>((subscriber) => {
      const url = this.baseUrl; // SSE endpoint
      const es = new EventSource(url);

      const onMessage = (evt: MessageEvent) => {
        try {
          const data = JSON.parse(evt.data) as LiveEventEnvelopeDTO<AssessmentEventDTO>;
          if (data.type === 'assessment.completed' && data.payload) {
            subscriber.next(data.payload);
          }
        } catch (e) {
          // Ignore malformed messages, but do not error the stream
        }
      };

      const onError = () => {
        // Surface error to consumer and close
        subscriber.error(new Error('SSE connection error'));
        es.close();
      };

      es.onmessage = onMessage;
      es.onerror = onError;

      return () => {
        es.close();
      };
    });
  }
}
