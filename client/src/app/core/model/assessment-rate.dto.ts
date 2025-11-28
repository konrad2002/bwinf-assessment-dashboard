// DTOs describing assessment rate time-series for overall and per task

export interface RatePointDTO {
  // ISO 8601 UTC timestamp bucket (e.g., minute or hour)
  timestamp: string;
  // Number of completed assessments in this bucket
  count: number;
  // Cumulative completed assessments up to and including this bucket
  cumulativeCount: number;
  // Optional rate per hour computed server-side for convenience
  ratePerHour?: number;
}

export interface AssessmentRateSeriesDTO {
  // When null or omitted, represents overall across all tasks
  taskId?: string | null;
  taskName?: string;
  bucketSize: 'minute' | 'hour' | 'day';
  points: RatePointDTO[];
  // Time range covered by the series (ISO 8601 UTC)
  range: { from: string; to: string };
}

export interface AssessmentRateResponseDTO {
  overall: AssessmentRateSeriesDTO;
  byTask: AssessmentRateSeriesDTO[];
  generatedAt: string; // ISO 8601 UTC
}
