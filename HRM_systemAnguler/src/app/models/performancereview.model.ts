export enum ReviewStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  ACKNOWLEDGED = 'ACKNOWLEDGED',
}
export interface PerformanceReview {
  id?: number;
  reviewPeriodStart: string;
  reviewPeriodEnd: string;
  rating: number;
  strengths: string;
  areasForImprovement: string;
  comments: string;
  status: ReviewStatus;
  employeeId: number;
  employeeName?: string;
  reviewerId: number;
  reviewerName?: string;
}
