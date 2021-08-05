jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InventoryService } from '../service/inventory.service';
import { IInventory, Inventory } from '../inventory.model';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';

import { InventoryUpdateComponent } from './inventory-update.component';

describe('Component Tests', () => {
  describe('Inventory Management Update Component', () => {
    let comp: InventoryUpdateComponent;
    let fixture: ComponentFixture<InventoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let inventoryService: InventoryService;
    let bookService: BookService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InventoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InventoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InventoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      inventoryService = TestBed.inject(InventoryService);
      bookService = TestBed.inject(BookService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Book query and add missing value', () => {
        const inventory: IInventory = { id: 456 };
        const book: IBook = { id: 40730 };
        inventory.book = book;

        const bookCollection: IBook[] = [{ id: 42582 }];
        spyOn(bookService, 'query').and.returnValue(of(new HttpResponse({ body: bookCollection })));
        const additionalBooks = [book];
        const expectedCollection: IBook[] = [...additionalBooks, ...bookCollection];
        spyOn(bookService, 'addBookToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ inventory });
        comp.ngOnInit();

        expect(bookService.query).toHaveBeenCalled();
        expect(bookService.addBookToCollectionIfMissing).toHaveBeenCalledWith(bookCollection, ...additionalBooks);
        expect(comp.booksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const inventory: IInventory = { id: 456 };
        const book: IBook = { id: 86511 };
        inventory.book = book;

        activatedRoute.data = of({ inventory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(inventory));
        expect(comp.booksSharedCollection).toContain(book);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const inventory = { id: 123 };
        spyOn(inventoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ inventory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: inventory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(inventoryService.update).toHaveBeenCalledWith(inventory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const inventory = new Inventory();
        spyOn(inventoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ inventory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: inventory }));
        saveSubject.complete();

        // THEN
        expect(inventoryService.create).toHaveBeenCalledWith(inventory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const inventory = { id: 123 };
        spyOn(inventoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ inventory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(inventoryService.update).toHaveBeenCalledWith(inventory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBookById', () => {
        it('Should return tracked Book primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBookById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
