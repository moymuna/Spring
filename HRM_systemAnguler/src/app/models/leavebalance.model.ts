export interface LeaveBalance {
  id?: number;
  year: number;
  totalEntitled: number;
  used: number;
  remaining?: number;
  employeeId: number;
  employeeName?: string;
  leaveTypeId: number;
  leaveTypeName?: string;
}
