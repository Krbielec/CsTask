import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPatron, Patron } from '../patron.model';
import { PatronService } from '../service/patron.service';

@Injectable({ providedIn: 'root' })
export class PatronRoutingResolveService implements Resolve<IPatron> {
  constructor(protected service: PatronService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPatron> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((patron: HttpResponse<Patron>) => {
          if (patron.body) {
            return of(patron.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Patron());
  }
}
