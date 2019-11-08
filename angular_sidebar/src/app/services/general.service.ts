import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GeneralService {

  public sidebarWidth = 250;
  sidebarVisible = true;
  public msg = 'blaaaaaaaaaaaa';

  constructor() {}


  public toggleSidebar() {
    this.sidebarVisible = ! this.sidebarVisible;
  }

  public getSidebarWidth(): number {
    return this.sidebarVisible ? this.sidebarWidth : 0;
  }

}
