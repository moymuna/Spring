export interface Training {
  id?: number;
  trainingTitle: string;
  startDate: string;
  endDate: string;
  employeeId?: number;
  employeeName?: string;
  departmentId: number;
  departmentName?: string;
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
  rejectionReason?: string;
}
