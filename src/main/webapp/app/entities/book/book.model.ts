export interface IBook {
  id?: number;
  title?: string;
  isbn?: string;
}

export class Book implements IBook {
  constructor(public id?: number, public title?: string, public isbn?: string) {}
}

export function getBookIdentifier(book: IBook): number | undefined {
  return book.id;
}
