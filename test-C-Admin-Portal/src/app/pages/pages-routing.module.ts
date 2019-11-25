import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PagesComponent } from './pages.component';
import {UploadFileComponent} from "./upload-file/upload-file.component";
import {TeilnehmerlisteComponent} from "./teilnehmerliste/teilnehmerliste.component";
import { AufgabenblaetteComponent } from './aufgabenblaette/aufgabenblaette.component';

const routes: Routes = [{
  path: '',
  component: PagesComponent,
  children: [{
    path: 'upload-file',
    component: UploadFileComponent,

  },{
    path: 'teilnehmerliste',
    component: TeilnehmerlisteComponent,
  },{
    path: 'aufgabenblaetter',
    component: AufgabenblaetteComponent,
  }
  ],

}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {
}
