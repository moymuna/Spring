export enum EmployeeStatus {
  ACTIVE = 'ACTIVE',
  ON_LEAVE = 'ON_LEAVE',
  SUSPENDED = 'SUSPENDED',
  RESIGNED = 'RESIGNED',
  TERMINATED = 'TERMINATED',
}
export enum EmploymentType {
  FULL_TIME = 'FULL_TIME',
  PART_TIME = 'PART_TIME',
  CONTRACT = 'CONTRACT',
  INTERN = 'INTERN',
  FREELANCE = 'FREELANCE',
}
export enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER',
}
export enum Role {
  ADMIN = 'ADMIN',
  HR = 'HR',
  MANAGER = 'MANAGER',
  EMPLOYEE = 'EMPLOYEE',
  APPLICANT = 'APPLICANT',
}
export interface AddressRequest {
  addressLine1: string;
  addressLine2?: string;
  postOffice?: string;
  postalCode?: string;
  countryId: number;
  divisionId: number;
  districtId: number;
  policeStationId: number;
}
export interface AddressResponse {
  id: number;
  addressLine1: string;
  addressLine2: string;
  postOffice: string;
  postalCode: string;
  countryName: string;
  divisionName: string;
  districtName: string;
  policeStationName: string;
}
export interface EmployeeRequest {
  contractNo: string;
  joiningDate: Date | string;
  dateOfBirth: Date | string;
  status: EmployeeStatus;
  gender: Gender;
  bloodGroup: string;
  employeeCode: string;
  employmentType: EmploymentType;
  bankName?: string;
  bankBranch?: string;
  bankAccountName?: string;
  bankAccountNumber?: string;
  fullName: string;
  email: string;
  password?: string;
  role: Role;
  departmentId: number;
  designationId: number;
  officeId?: number;
  managerId?: number;
  presentAddress: AddressRequest;
  permanentAddress: AddressRequest;
}
export interface EmployeeResponse {
  id: number;
  contractNo: string;
  joiningDate: string;
  dateOfExit?: string;
  status: EmployeeStatus;
  dateOfBirth: string;
  gender: Gender;
  bloodGroup: string;
  employeeCode: string;
  employmentType: EmploymentType;
  bankName?: string;
  bankBranch?: string;
  bankAccountName?: string;
  bankAccountNumber?: string;
  image: string;
  departmentId: number;
  departmentName: string;
  designationId: number;
  designationTitle: string;
  officeId: number;
  officeName: string;
  presentAddress: AddressResponse;
  permanentAddress: AddressResponse;
  userId: number;
  fullName: string;
  email: string;
  role: string;
  managerId?: number;
  managerName?: string;
}
export interface Department {
  id: number;
  departmentName: string;
  code: string;
  departmentHeadId?: number;
  departmentHeadName?: string;
  officeId?: number;
  officeName?: string;
}
export interface Designation {
  id: number;
  title: string;
  level: string;
  departmentId: number;
  departmentName: string;
}
export interface Office {
  id: number;
  officeName: string;
  officeCode: string;
  phone: string;
  email: string;
}
export interface Country {
  id: number;
  countryName: string;
  code: string;
  phoneCode: string;
}
export interface Division {
  id: number;
  name: string;
  nameBN: string;
  countryId: number;
  countryName: string;
}
export interface District {
  id: number;
  districtsName: string;
  nameBN: string;
  districtCode: string;
  divisionId: number;
  divisionName: string;
}
export interface PoliceStation {
  id: number;
  name: string;
  nameBn: string;
  postalCode: string;
  districtId: number;
  districtName: string;
  divisionId: number;
  divisionName: string;
  countryId: number;
  countryName: string;
}
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
