import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPatron, Patron } from '../patron.model';

import { PatronService } from './patron.service';

describe('Service Tests', () => {
  describe('Patron Service', () => {
    let service: PatronService;
    let httpMock: HttpTestingController;
    let elemDefault: IPatron;
    let expectedResult: IPatron | IPatron[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PatronService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        dateOfBirth: currentDate,
        phoneNumber: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOfBirth: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Patron', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOfBirth: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.create(new Patron()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Patron', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
            phoneNumber: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Patron', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Patron()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Patron', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
            phoneNumber: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Patron', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPatronToCollectionIfMissing', () => {
        it('should add a Patron to an empty array', () => {
          const patron: IPatron = { id: 123 };
          expectedResult = service.addPatronToCollectionIfMissing([], patron);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(patron);
        });

        it('should not add a Patron to an array that contains it', () => {
          const patron: IPatron = { id: 123 };
          const patronCollection: IPatron[] = [
            {
              ...patron,
            },
            { id: 456 },
          ];
          expectedResult = service.addPatronToCollectionIfMissing(patronCollection, patron);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Patron to an array that doesn't contain it", () => {
          const patron: IPatron = { id: 123 };
          const patronCollection: IPatron[] = [{ id: 456 }];
          expectedResult = service.addPatronToCollectionIfMissing(patronCollection, patron);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(patron);
        });

        it('should add only unique Patron to an array', () => {
          const patronArray: IPatron[] = [{ id: 123 }, { id: 456 }, { id: 90096 }];
          const patronCollection: IPatron[] = [{ id: 123 }];
          expectedResult = service.addPatronToCollectionIfMissing(patronCollection, ...patronArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const patron: IPatron = { id: 123 };
          const patron2: IPatron = { id: 456 };
          expectedResult = service.addPatronToCollectionIfMissing([], patron, patron2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(patron);
          expect(expectedResult).toContain(patron2);
        });

        it('should accept null and undefined values', () => {
          const patron: IPatron = { id: 123 };
          expectedResult = service.addPatronToCollectionIfMissing([], null, patron, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(patron);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
