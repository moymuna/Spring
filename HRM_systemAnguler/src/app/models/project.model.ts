export interface ProjectModel {
  id?: number;
  projectName: string;
  description: string;
  startDate: string;
  endDate: string;
  employeeId: number[];
  employeeName?: string[];
  officeId: number;
  officeName?: string;
}
