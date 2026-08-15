export enum JobStatus {
  OPEN = 'OPEN',
  CLOSED = 'CLOSED',
  DRAFT = 'DRAFT',
}
export interface JobPost {
  id?: number;
  title: string;
  description: string;
  requirements: string;
  location: string;
  minSalary: number;
  maxSalary: number;
  postedDate: string;
  deadline: string;
  status: JobStatus;
  departmentId: number;
  departmentName?: string;
}
