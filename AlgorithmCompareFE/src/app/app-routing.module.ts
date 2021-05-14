import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AlgorithmcompareComponent } from './algorithmcompare/algorithmcompare.component';

const routes: Routes = [{ path: 'AlgorithmCompareHome', component: AlgorithmcompareComponent },
                        { path: '', redirectTo: '/AlgorithmCompareHome', pathMatch: 'full' }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
