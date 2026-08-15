export enum DocumentType {
  RESUME = 'RESUME',
  ID_PROOF = 'ID_PROOF',
  OFFER_LETTER = 'OFFER_LETTER',
  CONTRACT = 'CONTRACT',
  CERTIFICATE = 'CERTIFICATE',
  OTHER = 'OTHER',
}
export interface DocumentModel {
  id?: number;
  documentName: string;
  documentType: DocumentType;
  filePath: string;
  uploadedAt?: string;
  employeeId: number;
  employeeName?: string;
}
