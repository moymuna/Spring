export interface LoginRequest {
  email: string;
  password: string;
}
export interface LoginResponse {
  id: number;
  token: string;
  refreshToken?: string;
  tokenType: string;
  userId: number;
  name: string;
  fullName: string;
  email: string;
  phone: string;
  role: 'ADMIN' | 'EMPLOYEE' | 'HR' | 'MANAGER' | 'APPLICANT';
}
export interface ForgotPasswordRequest {
  email: string;
}
export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}
