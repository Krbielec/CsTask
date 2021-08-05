import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PatronComponent } from './list/patron.component';
import { PatronDetailComponent } from './detail/patron-detail.component';
import { PatronUpdateComponent } from './update/patron-update.component';
import { PatronDeleteDialogComponent } from './delete/patron-delete-dialog.component';
import { PatronRoutingModule } from './route/patron-routing.module';

@NgModule({
  imports: [SharedModule, PatronRoutingModule],
  declarations: [PatronComponent, PatronDetailComponent, PatronUpdateComponent, PatronDeleteDialogComponent],
  entryComponents: [PatronDeleteDialogComponent],
})
export class PatronModule {}
