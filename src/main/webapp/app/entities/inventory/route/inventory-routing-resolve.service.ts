import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInventory, Inventory } from '../inventory.model';
import { InventoryService } from '../service/inventory.service';

@Injectable({ providedIn: 'root' })
export class InventoryRoutingResolveService implements Resolve<IInventory> {
  constructor(protected service: InventoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInventory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inventory: HttpResponse<Inventory>) => {
          if (inventory.body) {
            return of(inventory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Inventory());
  }
}
