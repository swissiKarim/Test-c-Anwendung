import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PagesComponent } from './pages.component';
import {UploadFileComponent} from "./upload-file/upload-file.component";

const routes: Routes = [{
  path: '',
  component: PagesComponent,
  children: [{
    path: 'upload-file',
    component: UploadFileComponent,

  }
  ],

}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {
}
