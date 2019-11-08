import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
// tslint:disable-next-line:import-spacing
import {UploaderViewComponent} from  '../app/core/uploader-view/uploader-view.component';
import {PageContextComponent} from './core/page-context/page-context.component';
import {TestErgibnesComponent} from './core/test-ergibnes/test-ergibnes.component';
import {LoginComponent} from './core/login/login.component';


const routes: Routes = [
  { path: 'uploadview', component: UploaderViewComponent},
  { path: 'dashboard', component: PageContextComponent},
  { path: 'testergibnes', component: TestErgibnesComponent},
  { path: 'lginPage', component: LoginComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {


}
