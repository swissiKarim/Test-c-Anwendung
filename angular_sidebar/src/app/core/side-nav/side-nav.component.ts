import { Component, OnInit } from '@angular/core';
import { GeneralService } from 'src/app/services/general.service';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['../allCss/allCssFile.css']
})
export class SideNavComponent implements OnInit {

  constructor(private generalService: GeneralService) { }

  ngOnInit() {
  }

}
