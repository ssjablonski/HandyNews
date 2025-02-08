import { UserAddressData } from './userAddressData.model';

export interface RegisterData {
  readonly firstName: string;
  readonly lastName: string;
  readonly email: string;
  readonly password: string;
  readonly dateOfBirth: string;
  readonly phoneNumber: string;
  readonly address: UserAddressData[];
}
