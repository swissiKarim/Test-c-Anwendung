import { Component, Input, OnInit } from '@angular/core';

import { NbMenuService, NbSidebarService } from '@nebular/theme';
import { AnalyticsService } from '../../../@core/utils';
import { LayoutService } from '../../../@core/utils';
import { LoginService } from '../../../pages/services/login.service';
import { LoginComponent } from '../../../login/login.component';
import {Router} from "@angular/router";
@Component({
  selector: 'ngx-header',
  styleUrls: ['./header.component.scss'],
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  @Input() position = 'normal';


  userMenu = [{ title: 'Profile' }, { title: 'Log out' }];

  constructor(private sidebarService: NbSidebarService,
              private menuService: NbMenuService,
              private analyticsService: AnalyticsService,
              private layoutService: LayoutService,
              private loginService: LoginService,
              private logincomponent: LoginComponent,  private router: Router) {
  }

  ngOnInit() {
    
   
  }

  isLoggedIn() {
    if (localStorage.getItem('my-app.token')) {
      return true;
    }
    return false;
  }
logout(){
  
  localStorage.removeItem('my-app.token');
  this.router.navigate(["/auth"]);
}
  toggleSidebar(): boolean {
    this.sidebarService.toggle(true, 'menu-sidebar');
    this.layoutService.changeLayoutSize();

    return false;
  }

  toggleSettings(): boolean {
    this.sidebarService.toggle(false, 'settings-sidebar');

    return false;
  }

  goToHome() {
    this.menuService.navigateHome();
  }

  startSearch() {
    this.analyticsService.trackEvent('startSearch');
  }
}
