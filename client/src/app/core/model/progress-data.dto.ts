export interface ProgressDataDto {
  open: number;           // number of tasks not started
  done: number;           // number of tasks fully evaluated
  onlyFirstDone: number;  // number of tasks with only first evaluation done
  inProgress: number;     // number of tasks currently claimed
  totalSubmissions: number; // total submissions per task
  totalRequiredEvaluations: number; // derived
  firstMissing: number;           // derived
  secondMissing: number;          // derived
}
