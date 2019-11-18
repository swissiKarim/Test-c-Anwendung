import { Component, OnInit } from '@angular/core';

import { MENU_ITEMS } from './pages-menu';
import { Router } from '@angular/router';
import { LocalStorageService } from 'angular-2-local-storage';

@Component({
  selector: 'ngx-pages',
  styleUrls: ['pages.component.scss'],
  template: `
    <ngx-sample-layout>
      <nb-menu [items]="menu"></nb-menu>
      <router-outlet></router-outlet>
    </ngx-sample-layout>
  `,
})
export class PagesComponent implements OnInit {

  menu = MENU_ITEMS;
  
  public constructor(private router: Router, private localStorageService: LocalStorageService) {

  }

  ngOnInit() {
    
    if (localStorage.getItem("my-app.token") == null){
      this.router.navigate(["/auth"]);
    }
  }
}
