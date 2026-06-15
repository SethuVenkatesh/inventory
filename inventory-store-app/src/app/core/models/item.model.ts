export interface Item {
  id?: string;
  organisationId: string;
  name: string;
  rate: number;
  currency?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string;
}
