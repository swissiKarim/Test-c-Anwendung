import { NgModule } from '@angular/core';
import { NgxEchartsModule } from 'ngx-echarts';
import { PagesComponent } from './pages.component';
import { PagesRoutingModule } from './pages-routing.module';
import { ThemeModule } from '../@theme/theme.module';
import { DragDropDirective } from './drag-drop.directive';
import { UploadFileComponent } from './upload-file/upload-file.component';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import { TreeModule } from 'angular-tree-component';
const PAGES_COMPONENTS = [
  PagesComponent,
];

@NgModule({
  imports: [
    PagesRoutingModule,
    ThemeModule,
    Ng2SmartTableModule,
    TreeModule,
    NgxEchartsModule,
    ThemeModule
  ],
  declarations: [
    ...PAGES_COMPONENTS,
    DragDropDirective,
    UploadFileComponent,
  ],
})
export class PagesModule {
}
