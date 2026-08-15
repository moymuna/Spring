export enum Role {
  ADMIN = 'ADMIN',
  HR = 'HR',
  EMPLOYEE = 'EMPLOYEE',
  APPLICANT = 'APPLICANT',
  MANAGER = 'MANAGER',
}
export interface User {
  id?: number;
  fullName: string;
  email: string;
  password?: string;
  role: Role;
  enabled: boolean;
  accountLocked: boolean;
  photoPath?: string;
  signaturePath?: string;
}
