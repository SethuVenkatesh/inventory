export interface Item {
  id?: string;
  name: string;
  rate: number;
  currency?: string;
  createdAt?: string;
  createdBy?: string;
  updatedAt?: string;
  updatedBy?: string;
}


export interface ItemHistory {
  id?: string;
  organisationId: string;
  itemId: string;

  oldName?: string;
  newName?: string;

  oldRate?: number;
  newRate?: number;

  oldCurrency?: string;
  newCurrency?: string;

  createdAt?: string;
  changedAt?: string;
  createdBy?: string;
  updatedAt?: string;
  updatedBy?: string;
}