# Assessment Dashboard API

This document outlines the proposed API endpoints and DTOs used by the Angular client to display assessment progress, rates, and live events.

## Conventions
- Base URL: `/api`
- All timestamps are ISO 8601 in UTC.
- IDs are strings.
- Pagination uses `page` (1-based) and `pageSize` query params when relevant.

## Endpoints

### GET `/api/progress/overall`
Returns overall progress across all tasks and per-task breakdown.

Response: `OverallProgressDTO`

### GET `/api/progress/tasks/:taskId`
Returns progress for a single task.

Response: `TaskProgressDTO`

### GET `/api/rates`
Returns overall and per-task assessment rates for a given time range.

Query params:
- `from`: ISO timestamp (required)
- `to`: ISO timestamp (required)
- `bucket`: `minute` | `hour` | `day` (default: `hour`)
- `tasks`: optional comma-separated task IDs to include; if omitted, all tasks

Response: `AssessmentRateResponseDTO`

### GET `/api/rates/tasks/:taskId`
Returns rate series for a single task.

Query params:
- `from`, `to`, `bucket` as above

Response: `AssessmentRateSeriesDTO`

### GET `/api/events`
Server-Sent Events (SSE) endpoint streaming live assessment completion events.

Headers:
- `Accept: text/event-stream`

Event Types:
- `assessment.completed` with payload `AssessmentEventDTO`
- `heartbeat` with payload `{ timestamp: string }`

Message envelope: `LiveEventEnvelopeDTO<AssessmentEventDTO>`

### GET `/api/assessments`
List historical assessment events (useful for fallback when SSE is disconnected).

Query params:
- `from`, `to` (optional): ISO timestamps
- `taskId` (optional)
- `assessorId` (optional)
- `page`, `pageSize` (optional)

Response:
```
{
  items: AssessmentEventDTO[];
  page: number;
  pageSize: number;
  totalItems: number;
}
```

## DTO References
Interfaces are defined under `src/app/core/model/`:
- `assessment-event.dto.ts`: `AssessmentEventDTO`, `LiveEventEnvelopeDTO`
- `progress.dto.ts`: `TaskProgressDTO`, `OverallProgressDTO`
- `assessment-rate.dto.ts`: `RatePointDTO`, `AssessmentRateSeriesDTO`, `AssessmentRateResponseDTO`

## Notes
- SSE is preferred for live updates; WebSocket can be offered at `/api/ws` mirroring the same envelope.
- Rate computation is performed server-side to avoid heavy client aggregation.
- Percentages are precomputed on the backend for consistency across clients.
