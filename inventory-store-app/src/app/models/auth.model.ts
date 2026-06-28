export interface LoginRequest {
  email: string;
  password?: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password?: string;
  organisationName?: string;
}

export interface AuthResponse {
  token: string;
  name: string;
  email: string;
  role: string;
  organisationId?: string;
}

export interface User {
  name: string;
  email: string;
  role: string;
}
