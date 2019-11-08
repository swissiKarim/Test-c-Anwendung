import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreComponent } from './core.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SideNavComponent } from './side-nav/side-nav.component';
import { PageContextComponent } from './page-context/page-context.component';
import { UploaderViewComponent } from './uploader-view/uploader-view.component';
import {AppRoutingModule} from '../app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import {TestErgibnesComponent} from './test-ergibnes/test-ergibnes.component';
import { NgxUiLoaderModule } from  'ngx-ui-loader';
import { NgCircleProgressModule } from 'ng-circle-progress';
import {LoginComponent} from './login/login.component';
@NgModule({
  imports: [
    CommonModule,
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    NgxUiLoaderModule,
    NgCircleProgressModule.forRoot({
      radius: 100,
      outerStrokeWidth: 16,
      innerStrokeWidth: 8,
      outerStrokeColor: "#78C000",
      innerStrokeColor: "#C7E596",
      animationDuration: 300,
    })
  ],
  declarations: [
    CoreComponent,
    NavBarComponent,
    SideNavComponent,
    PageContextComponent,
    UploaderViewComponent,
    LoginComponent,

    TestErgibnesComponent
  ],
  exports: [CoreComponent],
})
export class CoreModule { }
