export interface LeaveType {
  id?: number;
  name: string;
  maxDaysPerYear: number;
  maxCarryForwardDays?: number;
  paid: boolean;
  description: string;
}
