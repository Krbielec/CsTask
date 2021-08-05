import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PatronDetailComponent } from './patron-detail.component';

describe('Component Tests', () => {
  describe('Patron Management Detail Component', () => {
    let comp: PatronDetailComponent;
    let fixture: ComponentFixture<PatronDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PatronDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ patron: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PatronDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PatronDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load patron on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.patron).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
