import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PatronComponent } from '../list/patron.component';
import { PatronDetailComponent } from '../detail/patron-detail.component';
import { PatronUpdateComponent } from '../update/patron-update.component';
import { PatronRoutingResolveService } from './patron-routing-resolve.service';

const patronRoute: Routes = [
  {
    path: '',
    component: PatronComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatronDetailComponent,
    resolve: {
      patron: PatronRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PatronUpdateComponent,
    resolve: {
      patron: PatronRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatronUpdateComponent,
    resolve: {
      patron: PatronRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(patronRoute)],
  exports: [RouterModule],
})
export class PatronRoutingModule {}
