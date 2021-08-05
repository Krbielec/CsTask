import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatron } from '../patron.model';

@Component({
  selector: 'jhi-patron-detail',
  templateUrl: './patron-detail.component.html',
})
export class PatronDetailComponent implements OnInit {
  patron: IPatron | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patron }) => {
      this.patron = patron;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
