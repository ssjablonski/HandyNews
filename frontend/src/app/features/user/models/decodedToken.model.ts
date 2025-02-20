export interface DecodedToken {
  readonly exp: number;
  readonly iat: number;
  readonly sub: string;
}
