export interface Address {
  id?: number;
  addressLine1: string;
  addressLine2: string;
  postOffice: string;
  postalCode: string;
  countryId: number;
  divisionId: number;
  districtId: number;
  policeStationId: number;
}
export interface OfficeRequest {
  officeName: string;
  officeCode: string;
  phone: string;
  email: string;
  address: Address;
}
export interface OfficeResponse {
  id: number;
  officeName: string;
  officeCode: string;
  phone: string;
  email: string;
  address: Address;
}
