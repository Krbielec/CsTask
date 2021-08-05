import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPatron, Patron } from '../patron.model';
import { PatronService } from '../service/patron.service';

@Component({
  selector: 'jhi-patron-update',
  templateUrl: './patron-update.component.html',
})
export class PatronUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    dateOfBirth: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required]],
  });

  constructor(protected patronService: PatronService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patron }) => {
      this.updateForm(patron);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patron = this.createFromForm();
    if (patron.id !== undefined) {
      this.subscribeToSaveResponse(this.patronService.update(patron));
    } else {
      this.subscribeToSaveResponse(this.patronService.create(patron));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatron>>): void {
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

  protected updateForm(patron: IPatron): void {
    this.editForm.patchValue({
      id: patron.id,
      name: patron.name,
      dateOfBirth: patron.dateOfBirth,
      phoneNumber: patron.phoneNumber,
    });
  }

  protected createFromForm(): IPatron {
    return {
      ...new Patron(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
    };
  }
}
