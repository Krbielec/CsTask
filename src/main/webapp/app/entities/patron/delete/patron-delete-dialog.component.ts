import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPatron } from '../patron.model';
import { PatronService } from '../service/patron.service';

@Component({
  templateUrl: './patron-delete-dialog.component.html',
})
export class PatronDeleteDialogComponent {
  patron?: IPatron;

  constructor(protected patronService: PatronService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.patronService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
