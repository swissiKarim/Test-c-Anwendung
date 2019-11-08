import { Component, OnInit, ElementRef, Renderer2 } from '@angular/core';
import { GeneralService } from '../services/general.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  constructor(private renderer: Renderer2,
              private el: ElementRef,
              private generalService: GeneralService) {}

  ngOnInit() {
  }

}
