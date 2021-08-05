jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPatron, Patron } from '../patron.model';
import { PatronService } from '../service/patron.service';

import { PatronRoutingResolveService } from './patron-routing-resolve.service';

describe('Service Tests', () => {
  describe('Patron routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PatronRoutingResolveService;
    let service: PatronService;
    let resultPatron: IPatron | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PatronRoutingResolveService);
      service = TestBed.inject(PatronService);
      resultPatron = undefined;
    });

    describe('resolve', () => {
      it('should return IPatron returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPatron = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPatron).toEqual({ id: 123 });
      });

      it('should return new IPatron if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPatron = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPatron).toEqual(new Patron());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPatron = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPatron).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
