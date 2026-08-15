export enum ApplicationStatus {
  APPLIED = 'APPLIED',
  UNDER_REVIEW = 'UNDER_REVIEW',
  SHORTLISTED = 'SHORTLISTED',
  INTERVIEWED = 'INTERVIEWED',
  REJECTED = 'REJECTED',
  HIRED = 'HIRED',
}
export interface ApplicationModel {
  id?: number;
  applicantId: number;
  applicantName?: string;
  jobPostId: number;
  jobTitle?: string;
  applyDate: string;
  status: ApplicationStatus;
}
