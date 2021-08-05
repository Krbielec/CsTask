import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPatron, getPatronIdentifier } from '../patron.model';

export type EntityResponseType = HttpResponse<IPatron>;
export type EntityArrayResponseType = HttpResponse<IPatron[]>;

@Injectable({ providedIn: 'root' })
export class PatronService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/patrons');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(patron: IPatron): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patron);
    return this.http
      .post<IPatron>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(patron: IPatron): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patron);
    return this.http
      .put<IPatron>(`${this.resourceUrl}/${getPatronIdentifier(patron) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(patron: IPatron): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patron);
    return this.http
      .patch<IPatron>(`${this.resourceUrl}/${getPatronIdentifier(patron) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPatron>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPatron[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPatronToCollectionIfMissing(patronCollection: IPatron[], ...patronsToCheck: (IPatron | null | undefined)[]): IPatron[] {
    const patrons: IPatron[] = patronsToCheck.filter(isPresent);
    if (patrons.length > 0) {
      const patronCollectionIdentifiers = patronCollection.map(patronItem => getPatronIdentifier(patronItem)!);
      const patronsToAdd = patrons.filter(patronItem => {
        const patronIdentifier = getPatronIdentifier(patronItem);
        if (patronIdentifier == null || patronCollectionIdentifiers.includes(patronIdentifier)) {
          return false;
        }
        patronCollectionIdentifiers.push(patronIdentifier);
        return true;
      });
      return [...patronsToAdd, ...patronCollection];
    }
    return patronCollection;
  }

  protected convertDateFromClient(patron: IPatron): IPatron {
    return Object.assign({}, patron, {
      dateOfBirth: patron.dateOfBirth?.isValid() ? patron.dateOfBirth.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((patron: IPatron) => {
        patron.dateOfBirth = patron.dateOfBirth ? dayjs(patron.dateOfBirth) : undefined;
      });
    }
    return res;
  }
}
