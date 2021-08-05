import * as dayjs from 'dayjs';

export interface IPatron {
  id?: number;
  name?: string;
  dateOfBirth?: dayjs.Dayjs;
  phoneNumber?: string;
}

export class Patron implements IPatron {
  constructor(public id?: number, public name?: string, public dateOfBirth?: dayjs.Dayjs, public phoneNumber?: string) {}
}

export function getPatronIdentifier(patron: IPatron): number | undefined {
  return patron.id;
}
