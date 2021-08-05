import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInventory, Inventory } from '../inventory.model';

import { InventoryService } from './inventory.service';

describe('Service Tests', () => {
  describe('Inventory Service', () => {
    let service: InventoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IInventory;
    let expectedResult: IInventory | IInventory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InventoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Inventory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Inventory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Inventory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Inventory', () => {
        const patchObject = Object.assign({}, new Inventory());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Inventory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Inventory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInventoryToCollectionIfMissing', () => {
        it('should add a Inventory to an empty array', () => {
          const inventory: IInventory = { id: 123 };
          expectedResult = service.addInventoryToCollectionIfMissing([], inventory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(inventory);
        });

        it('should not add a Inventory to an array that contains it', () => {
          const inventory: IInventory = { id: 123 };
          const inventoryCollection: IInventory[] = [
            {
              ...inventory,
            },
            { id: 456 },
          ];
          expectedResult = service.addInventoryToCollectionIfMissing(inventoryCollection, inventory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Inventory to an array that doesn't contain it", () => {
          const inventory: IInventory = { id: 123 };
          const inventoryCollection: IInventory[] = [{ id: 456 }];
          expectedResult = service.addInventoryToCollectionIfMissing(inventoryCollection, inventory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(inventory);
        });

        it('should add only unique Inventory to an array', () => {
          const inventoryArray: IInventory[] = [{ id: 123 }, { id: 456 }, { id: 25090 }];
          const inventoryCollection: IInventory[] = [{ id: 123 }];
          expectedResult = service.addInventoryToCollectionIfMissing(inventoryCollection, ...inventoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const inventory: IInventory = { id: 123 };
          const inventory2: IInventory = { id: 456 };
          expectedResult = service.addInventoryToCollectionIfMissing([], inventory, inventory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(inventory);
          expect(expectedResult).toContain(inventory2);
        });

        it('should accept null and undefined values', () => {
          const inventory: IInventory = { id: 123 };
          expectedResult = service.addInventoryToCollectionIfMissing([], null, inventory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(inventory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
