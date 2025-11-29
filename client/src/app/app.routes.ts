import { Routes } from '@angular/router';
import {DashboardOldComponent} from './content/dashboard-old/dashboard-old.component';
import {DashboardComponent} from './content/dashboard/dashboard.component';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', component: DashboardComponent },
	{ path: 'old', pathMatch: 'full', component: DashboardOldComponent },
];
