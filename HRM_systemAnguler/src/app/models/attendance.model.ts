export interface Attendance {
  id?: number;
  date: string;
  checkInTime?: string;
  checkOutTime?: string;
  workedHours?: number;
  status: string;
  employeeId: number;
  employeeName?: string;
}
