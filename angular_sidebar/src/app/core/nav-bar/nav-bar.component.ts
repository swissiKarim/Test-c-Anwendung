import { Component, OnInit } from '@angular/core';
import { GeneralService } from 'src/app/services/general.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['../allCss/allCssFile.css']
})
export class NavBarComponent implements OnInit {
isLogout = false;
  constructor(private generalService: GeneralService, private router: Router) { }

  ngOnInit() {
  }

  logout() {
    this.router.navigateByUrl('/lginPage');
    this.isLogout = true;
  }
}
