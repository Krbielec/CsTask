import { IBook } from 'app/entities/book/book.model';

export interface IInventory {
  id?: number;
  book?: IBook;
}

export class Inventory implements IInventory {
  constructor(public id?: number, public book?: IBook) {}
}

export function getInventoryIdentifier(inventory: IInventory): number | undefined {
  return inventory.id;
}
