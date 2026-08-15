export enum EducationLevel {
  SSC = 'SSC',
  HSC = 'HSC',
  BACHELORS = 'BACHELORS',
  MASTERS = 'MASTERS',
  PHD = 'PHD',
}
export enum ExperienceLevel {
  FRESHER = 'FRESHER',
  JUNIOR = 'JUNIOR',
  MID = 'MID',
  SENIOR = 'SENIOR',
}
export interface Applicant {
  id?: number;
  name: string;
  email: string;
  phone: string;
  address: string;
  education: EducationLevel[];
  experience: ExperienceLevel[];
  skills: string;
  cvPath: string;
  password?: string;
}
