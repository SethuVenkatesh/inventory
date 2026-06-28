export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
  fieldErrors?: { [key: string]: string };
  status: number;
  timestamp: string;
}
