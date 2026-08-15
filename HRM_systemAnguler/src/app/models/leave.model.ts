export interface Leave {
  id?: number;
  startDate: string;
  endDate: string;
  totalDays: number;
  reason: string;
  status: string;
  decidedAt?: string;
  rejectionReason?: string;
  employeeId: number;
  employeeName?: string;
  leaveTypeId: number;
  leaveTypeName?: string;
}
