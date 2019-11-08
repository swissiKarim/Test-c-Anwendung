import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { MainModule } from './main/main.module';
import { GeneralService } from './services/general.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSliderModule, MatCheckboxModule} from '@angular/material'
import {FormsModule, ReactiveFormsModule} from '@angular/forms';


@NgModule({
  declarations: [
    AppComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    MainModule,
    BrowserAnimationsModule,
    MatSliderModule, MatCheckboxModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [GeneralService],
  exports: [
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
