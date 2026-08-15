export enum InterviewResult {
  PASS = 'PASS',
  FAIL = 'FAIL',
  PENDING = 'PENDING',
}
export interface InterviewModel {
  id?: number;
  applicationId: number;
  applicantName?: string;
  interviewerId: number;
  interviewerName?: string;
  interviewDate: string;
  feedback?: string;
  result: InterviewResult;
}
