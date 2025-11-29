import { Injectable, NgZone } from '@angular/core';
import { Observable, Subscriber } from 'rxjs';
import {EventSseDataDto} from '../model/event-sse-data.dto';

@Injectable({
  providedIn: 'root'
})
export class EventSourceService {
  private eventSource?: EventSource;

  constructor(private zone: NgZone) {}

  getEventSource(url: string): EventSource {
    return new EventSource(url);
  }

  connectToServerSentEvents(url: string): Observable<{data: string}> {
    this.eventSource = this.getEventSource(url);

    return new Observable((subscriber: Subscriber<{data: string}>) => {
      if (this.eventSource) {
        this.eventSource.onerror = error => {
          this.zone.run(() => subscriber.error(error));
        };

        this.eventSource.onmessage = msg => {
          this.zone.run(() => subscriber.next(msg));
        }
      }
    });
  }

  close(): void {
    if (!this.eventSource) {
      return;
    }

    this.eventSource.close();
    this.eventSource = undefined;
  }
}
