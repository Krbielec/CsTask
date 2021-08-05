import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRental, Rental } from '../rental.model';
import { RentalService } from '../service/rental.service';
import { IPatron } from 'app/entities/patron/patron.model';
import { PatronService } from 'app/entities/patron/service/patron.service';
import { IInventory } from 'app/entities/inventory/inventory.model';
import { InventoryService } from 'app/entities/inventory/service/inventory.service';

@Component({
  selector: 'jhi-rental-update',
  templateUrl: './rental-update.component.html',
})
export class RentalUpdateComponent implements OnInit {
  isSaving = false;

  patronsSharedCollection: IPatron[] = [];
  inventoriesSharedCollection: IInventory[] = [];

  editForm = this.fb.group({
    id: [],
    rentalDate: [null, [Validators.required]],
    returnDate: [],
    patron: [],
    inventory: [],
  });

  constructor(
    protected rentalService: RentalService,
    protected patronService: PatronService,
    protected inventoryService: InventoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rental }) => {
      this.updateForm(rental);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rental = this.createFromForm();
    if (rental.id !== undefined) {
      this.subscribeToSaveResponse(this.rentalService.update(rental));
    } else {
      this.subscribeToSaveResponse(this.rentalService.create(rental));
    }
  }

  trackPatronById(index: number, item: IPatron): number {
    return item.id!;
  }

  trackInventoryById(index: number, item: IInventory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRental>>): void {
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

  protected updateForm(rental: IRental): void {
    this.editForm.patchValue({
      id: rental.id,
      rentalDate: rental.rentalDate,
      returnDate: rental.returnDate,
      patron: rental.patron,
      inventory: rental.inventory,
    });

    this.patronsSharedCollection = this.patronService.addPatronToCollectionIfMissing(this.patronsSharedCollection, rental.patron);
    this.inventoriesSharedCollection = this.inventoryService.addInventoryToCollectionIfMissing(
      this.inventoriesSharedCollection,
      rental.inventory
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patronService
      .query()
      .pipe(map((res: HttpResponse<IPatron[]>) => res.body ?? []))
      .pipe(map((patrons: IPatron[]) => this.patronService.addPatronToCollectionIfMissing(patrons, this.editForm.get('patron')!.value)))
      .subscribe((patrons: IPatron[]) => (this.patronsSharedCollection = patrons));

    this.inventoryService
      .query()
      .pipe(map((res: HttpResponse<IInventory[]>) => res.body ?? []))
      .pipe(
        map((inventories: IInventory[]) =>
          this.inventoryService.addInventoryToCollectionIfMissing(inventories, this.editForm.get('inventory')!.value)
        )
      )
      .subscribe((inventories: IInventory[]) => (this.inventoriesSharedCollection = inventories));
  }

  protected createFromForm(): IRental {
    return {
      ...new Rental(),
      id: this.editForm.get(['id'])!.value,
      rentalDate: this.editForm.get(['rentalDate'])!.value,
      returnDate: this.editForm.get(['returnDate'])!.value,
      patron: this.editForm.get(['patron'])!.value,
      inventory: this.editForm.get(['inventory'])!.value,
    };
  }
}
