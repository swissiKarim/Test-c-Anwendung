import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {GeneralService} from '../../services/general.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-page-context',
  templateUrl: './page-context.component.html',
  styleUrls: ['../allCss/allCssFile.css']
})
export class PageContextComponent implements OnInit {


  constructor(private renderer: Renderer2,
              private el: ElementRef,
              private generalService: GeneralService) { }

  ngOnInit() {
  }

}
