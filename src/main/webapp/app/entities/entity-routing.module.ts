import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'book',
        data: { pageTitle: 'Books' },
        loadChildren: () => import('./book/book.module').then(m => m.BookModule),
      },
      {
        path: 'patron',
        data: { pageTitle: 'Patrons' },
        loadChildren: () => import('./patron/patron.module').then(m => m.PatronModule),
      },
      {
        path: 'inventory',
        data: { pageTitle: 'Inventories' },
        loadChildren: () => import('./inventory/inventory.module').then(m => m.InventoryModule),
      },
      {
        path: 'rental',
        data: { pageTitle: 'Rentals' },
        loadChildren: () => import('./rental/rental.module').then(m => m.RentalModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
