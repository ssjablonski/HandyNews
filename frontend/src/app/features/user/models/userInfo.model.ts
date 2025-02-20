export interface UserInfo {
  readonly id: number;
  readonly firstName: string;
  readonly lastName: string;
  readonly email: string;
  readonly password: string;
  readonly dateOfBirth: string;
  readonly phoneNumber: string;
  readonly role: string;
  readonly address: {
    readonly id: number;
    readonly city: string;
    readonly zipcode: string;
    readonly houseNumber: string;
    readonly street: string;
  };
}
