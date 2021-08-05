jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RentalService } from '../service/rental.service';
import { IRental, Rental } from '../rental.model';
import { IPatron } from 'app/entities/patron/patron.model';
import { PatronService } from 'app/entities/patron/service/patron.service';
import { IInventory } from 'app/entities/inventory/inventory.model';
import { InventoryService } from 'app/entities/inventory/service/inventory.service';

import { RentalUpdateComponent } from './rental-update.component';

describe('Component Tests', () => {
  describe('Rental Management Update Component', () => {
    let comp: RentalUpdateComponent;
    let fixture: ComponentFixture<RentalUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let rentalService: RentalService;
    let patronService: PatronService;
    let inventoryService: InventoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RentalUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RentalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RentalUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      rentalService = TestBed.inject(RentalService);
      patronService = TestBed.inject(PatronService);
      inventoryService = TestBed.inject(InventoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Patron query and add missing value', () => {
        const rental: IRental = { id: 456 };
        const patron: IPatron = { id: 24850 };
        rental.patron = patron;

        const patronCollection: IPatron[] = [{ id: 27032 }];
        spyOn(patronService, 'query').and.returnValue(of(new HttpResponse({ body: patronCollection })));
        const additionalPatrons = [patron];
        const expectedCollection: IPatron[] = [...additionalPatrons, ...patronCollection];
        spyOn(patronService, 'addPatronToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ rental });
        comp.ngOnInit();

        expect(patronService.query).toHaveBeenCalled();
        expect(patronService.addPatronToCollectionIfMissing).toHaveBeenCalledWith(patronCollection, ...additionalPatrons);
        expect(comp.patronsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Inventory query and add missing value', () => {
        const rental: IRental = { id: 456 };
        const inventory: IInventory = { id: 25090 };
        rental.inventory = inventory;

        const inventoryCollection: IInventory[] = [{ id: 85933 }];
        spyOn(inventoryService, 'query').and.returnValue(of(new HttpResponse({ body: inventoryCollection })));
        const additionalInventories = [inventory];
        const expectedCollection: IInventory[] = [...additionalInventories, ...inventoryCollection];
        spyOn(inventoryService, 'addInventoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ rental });
        comp.ngOnInit();

        expect(inventoryService.query).toHaveBeenCalled();
        expect(inventoryService.addInventoryToCollectionIfMissing).toHaveBeenCalledWith(inventoryCollection, ...additionalInventories);
        expect(comp.inventoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const rental: IRental = { id: 456 };
        const patron: IPatron = { id: 86367 };
        rental.patron = patron;
        const inventory: IInventory = { id: 85231 };
        rental.inventory = inventory;

        activatedRoute.data = of({ rental });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(rental));
        expect(comp.patronsSharedCollection).toContain(patron);
        expect(comp.inventoriesSharedCollection).toContain(inventory);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const rental = { id: 123 };
        spyOn(rentalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ rental });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: rental }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(rentalService.update).toHaveBeenCalledWith(rental);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const rental = new Rental();
        spyOn(rentalService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ rental });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: rental }));
        saveSubject.complete();

        // THEN
        expect(rentalService.create).toHaveBeenCalledWith(rental);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const rental = { id: 123 };
        spyOn(rentalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ rental });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(rentalService.update).toHaveBeenCalledWith(rental);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPatronById', () => {
        it('Should return tracked Patron primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPatronById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackInventoryById', () => {
        it('Should return tracked Inventory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInventoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
