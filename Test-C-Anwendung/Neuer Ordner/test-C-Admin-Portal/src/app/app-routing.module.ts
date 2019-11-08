import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { LoginComponent } from './login/login.component';
import {LocalStorageModule} from "angular-2-local-storage";
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { LoginService } from './pages/services/login.service';


const routes: Routes = [

  { path: '', redirectTo: '/auth', pathMatch: 'full' },
  {
    path: 'auth',
    component: LoginComponent,
    
  },
  { path: 'pages', loadChildren: 'app/pages/pages.module#PagesModule' },
];

const config: ExtraOptions = {
  useHash: true,
};

@NgModule({
  imports: [RouterModule.forRoot(routes, config), FormsModule,CommonModule, LocalStorageModule.forRoot({
    prefix: 'my-app',
    storageType: 'localStorage'
  }), 
  ReactiveFormsModule],
  exports: [RouterModule],
  declarations: [],
  providers: [LoginService]
})
export class AppRoutingModule {
}
