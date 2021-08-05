jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PatronService } from '../service/patron.service';
import { IPatron, Patron } from '../patron.model';

import { PatronUpdateComponent } from './patron-update.component';

describe('Component Tests', () => {
  describe('Patron Management Update Component', () => {
    let comp: PatronUpdateComponent;
    let fixture: ComponentFixture<PatronUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let patronService: PatronService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PatronUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PatronUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PatronUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      patronService = TestBed.inject(PatronService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const patron: IPatron = { id: 456 };

        activatedRoute.data = of({ patron });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(patron));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const patron = { id: 123 };
        spyOn(patronService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ patron });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: patron }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(patronService.update).toHaveBeenCalledWith(patron);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const patron = new Patron();
        spyOn(patronService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ patron });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: patron }));
        saveSubject.complete();

        // THEN
        expect(patronService.create).toHaveBeenCalledWith(patron);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const patron = { id: 123 };
        spyOn(patronService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ patron });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(patronService.update).toHaveBeenCalledWith(patron);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
