export interface Organisation {
  id?: string;
  name: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface APIResponse<T> {
  success: boolean;
  message: string;
  status: number;
  timestamp: string;
  data: T;
  fieldErrors?: { [key: string]: string };
}