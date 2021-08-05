import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInventory, Inventory } from '../inventory.model';
import { InventoryService } from '../service/inventory.service';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';

@Component({
  selector: 'jhi-inventory-update',
  templateUrl: './inventory-update.component.html',
})
export class InventoryUpdateComponent implements OnInit {
  isSaving = false;

  booksSharedCollection: IBook[] = [];

  editForm = this.fb.group({
    id: [],
    book: [null, Validators.required],
  });

  constructor(
    protected inventoryService: InventoryService,
    protected bookService: BookService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventory }) => {
      this.updateForm(inventory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventory = this.createFromForm();
    if (inventory.id !== undefined) {
      this.subscribeToSaveResponse(this.inventoryService.update(inventory));
    } else {
      this.subscribeToSaveResponse(this.inventoryService.create(inventory));
    }
  }

  trackBookById(index: number, item: IBook): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(inventory: IInventory): void {
    this.editForm.patchValue({
      id: inventory.id,
      book: inventory.book,
    });

    this.booksSharedCollection = this.bookService.addBookToCollectionIfMissing(this.booksSharedCollection, inventory.book);
  }

  protected loadRelationshipsOptions(): void {
    this.bookService
      .query()
      .pipe(map((res: HttpResponse<IBook[]>) => res.body ?? []))
      .pipe(map((books: IBook[]) => this.bookService.addBookToCollectionIfMissing(books, this.editForm.get('book')!.value)))
      .subscribe((books: IBook[]) => (this.booksSharedCollection = books));
  }

  protected createFromForm(): IInventory {
    return {
      ...new Inventory(),
      id: this.editForm.get(['id'])!.value,
      book: this.editForm.get(['book'])!.value,
    };
  }
}
