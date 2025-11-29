// DTOs describing progress statistics per task and overall

import {TaskType} from './task-type.enum';

export interface TaskProgressDTO {
  taskId: string;
  taskName?: TaskType;
  totalSubmissions: number;
  firstAssessmentsDone: number;
  secondAssessmentsDone: number;
  totalAssessmentsDone: number;
  firstMissing: number;
  secondMissing: number;
  totalMissing: number;
  percentages: {
    first: number; // 0..100
    second: number; // 0..100
    total: number; // 0..100
  };
  // Last time the backend updated these stats (ISO 8601 UTC)
  updatedAt: string;
}

export interface OverallProgressDTO {
  totalTasks: number;
  totalSubmissions: number;
  firstAssessmentsDone: number;
  secondAssessmentsDone: number;
  totalAssessmentsDone: number;
  firstMissing: number;
  secondMissing: number;
  totalMissing: number;
  percentages: {
    first: number;
    second: number;
    total: number;
  };
  byTask: TaskProgressDTO[];
  updatedAt: string;
}

export namespace TaskProgressDTO {
  export function getTaskName(t: TaskProgressDTO): string {
    return ((t.taskName == TaskType.BWINF) ? 'A' : 'J') + t.taskId;
  }
}
