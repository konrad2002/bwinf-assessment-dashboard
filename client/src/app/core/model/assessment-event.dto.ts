// DTOs describing assessment events coming from the backend
// These are API-facing types, not UI view models.

export type AssessmentType = 'first' | 'second';

export interface AssessmentEventDTO {
  id: string;
  submissionId: string;
  taskId: string;
  assessorId: string;
  assessmentType: AssessmentType;
  points: number;
  // ISO 8601 timestamp in UTC from server
  timestamp: string;
}

export interface LiveEventEnvelopeDTO<T = unknown> {
  type: 'assessment.completed' | 'heartbeat' | string;
  payload: T;
  // Server-sent event id or websocket message id
  eventId?: string;
  // ISO 8601 timestamp in UTC
  emittedAt: string;
}
