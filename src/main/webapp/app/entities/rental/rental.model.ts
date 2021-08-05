import * as dayjs from 'dayjs';
import { IPatron } from 'app/entities/patron/patron.model';
import { IInventory } from 'app/entities/inventory/inventory.model';

export interface IRental {
  id?: number;
  rentalDate?: dayjs.Dayjs;
  returnDate?: dayjs.Dayjs | null;
  patron?: IPatron | null;
  inventory?: IInventory | null;
}

export class Rental implements IRental {
  constructor(
    public id?: number,
    public rentalDate?: dayjs.Dayjs,
    public returnDate?: dayjs.Dayjs | null,
    public patron?: IPatron | null,
    public inventory?: IInventory | null
  ) {}
}

export function getRentalIdentifier(rental: IRental): number | undefined {
  return rental.id;
}
