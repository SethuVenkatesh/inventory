export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  organisationName?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  name: string;
  email: string;
  role: string;
  organisationId: string;
  organisationName: string;
}