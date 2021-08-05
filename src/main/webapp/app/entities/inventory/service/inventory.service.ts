import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInventory, getInventoryIdentifier } from '../inventory.model';

export type EntityResponseType = HttpResponse<IInventory>;
export type EntityArrayResponseType = HttpResponse<IInventory[]>;

@Injectable({ providedIn: 'root' })
export class InventoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/inventories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(inventory: IInventory): Observable<EntityResponseType> {
    return this.http.post<IInventory>(this.resourceUrl, inventory, { observe: 'response' });
  }

  update(inventory: IInventory): Observable<EntityResponseType> {
    return this.http.put<IInventory>(`${this.resourceUrl}/${getInventoryIdentifier(inventory) as number}`, inventory, {
      observe: 'response',
    });
  }

  partialUpdate(inventory: IInventory): Observable<EntityResponseType> {
    return this.http.patch<IInventory>(`${this.resourceUrl}/${getInventoryIdentifier(inventory) as number}`, inventory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInventory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInventory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInventoryToCollectionIfMissing(
    inventoryCollection: IInventory[],
    ...inventoriesToCheck: (IInventory | null | undefined)[]
  ): IInventory[] {
    const inventories: IInventory[] = inventoriesToCheck.filter(isPresent);
    if (inventories.length > 0) {
      const inventoryCollectionIdentifiers = inventoryCollection.map(inventoryItem => getInventoryIdentifier(inventoryItem)!);
      const inventoriesToAdd = inventories.filter(inventoryItem => {
        const inventoryIdentifier = getInventoryIdentifier(inventoryItem);
        if (inventoryIdentifier == null || inventoryCollectionIdentifiers.includes(inventoryIdentifier)) {
          return false;
        }
        inventoryCollectionIdentifiers.push(inventoryIdentifier);
        return true;
      });
      return [...inventoriesToAdd, ...inventoryCollection];
    }
    return inventoryCollection;
  }
}
