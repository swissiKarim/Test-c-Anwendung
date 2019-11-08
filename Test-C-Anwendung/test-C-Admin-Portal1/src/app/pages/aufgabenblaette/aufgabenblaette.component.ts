import { Component, OnInit } from '@angular/core';

import { TrafikserviceService } from '../services/trafikservice.service';
import {Uplaodedfile} from '../models/uplaodedfile'

@Component({
  selector: 'ngx-aufgabenblaette',
  templateUrl: './aufgabenblaette.component.html',
  styleUrls: ['./aufgabenblaette.component.scss']
})
export class AufgabenblaetteComponent implements OnInit {
  aufgabenblaette: any = [];
  files : Uplaodedfile[];
  map:Map<string, string>;
  nodes : any;
  index : number = -1;
  makefiles: any[] = [];
  pathmakefiles: any[] = [];
  hrcfilesfiles : any[] = [];
  htestfilesfiles: any[] = [];
  indexH : number = -1;
  indexSrc : number = -1;
  indexTest : number = -1;
  constructor(private TrafikService: TrafikserviceService,) { }

  ngOnInit() {
    this.getAufgabenblaette();
   
   
  }



  getAufgabenblaette(){
    
    this.TrafikService.listfiles("","",true).subscribe(
      (files: any[]) => {
        this.files = files;
        for (let file of this.files ) {
          this.aufgabenblaette.push(file.name);
         this.getAufgabenblamakefile(file.name);
         this.getSrcfiles(file.name);
         this.gettestfiles(file.name);
      }
      },
      (error) => console.log(error)
    );
    console.log(this.index);
  }
  
  getAufgabenblamakefile(aufgabenblattNm: String){
    
    let makefilescont: String[] = [];
    let pathmakefilescont: String[] = [];
   // let  makefiles: any[] = null;
    this.TrafikService.listfiles(aufgabenblattNm,"",false).subscribe(
      
      (files: any[]) => {
        
        this.files = files;
        
        for (let file of this.files ) {
          
          makefilescont.push(file.name);
          pathmakefilescont.push(file.path);
      }
      this.makefiles.push(makefilescont);
      this.pathmakefiles.push(pathmakefilescont);
     
      },
      (error) => console.log(error)
    );
    
    console.log(this.indexH);
  }

  getSrcfiles(aufgabenblattNm: String){
    
    let srcfiles: String[] = [];
   // let  makefiles: any[] = null;
   
    this.TrafikService.listfiles(aufgabenblattNm,"src",false).subscribe(
      
      (files: any[]) => {
        this.files = files;
        
        for (let file of this.files ) {
         
          srcfiles.push(file.name);
      }
      this.hrcfilesfiles.push(srcfiles);
      },
      (error) => console.log(error)
    );
    
  }

  gettestfiles(aufgabenblattNm: String){
    
    let testfiles: String[] = [];
   // let  makefiles: any[] = null;
 
    this.TrafikService.listfiles(aufgabenblattNm,"test",false).subscribe(
      
      (files: any[]) => {
        this.files = files;
        
        for (let file of this.files ) {
         
         testfiles.push(file.name);
      }
      this.htestfilesfiles.push(testfiles);
      },
      (error) => console.log(error)
    );
    
  }
  
  deleteAttachment(inx, ind) {

   this.TrafikService.deletfiles(this.makefiles[inx][ind], this.aufgabenblaette[inx],"").subscribe(
      
      () => {
        console.log("File is Removed");
      },
      (error) => console.log(error)
    );

   this.makefiles[inx].splice(ind, 1);
  }

  deleteAttachmentSrc(inx, ind) {
    this.TrafikService.deletfiles(this.hrcfilesfiles[inx][ind], this.aufgabenblaette[inx],"src").subscribe(
      
      () => {
        console.log("File is Removed");
      },
      (error) => console.log(error)
    );
    
    this.hrcfilesfiles[inx].splice(ind, 1);
  
  }

  deleteAttachmentTest(inx, ind) {
    this.TrafikService.deletfiles(this.htestfilesfiles[inx][ind], this.aufgabenblaette[inx],"test").subscribe(
      
      () => {
        console.log("File is Removed");
      },
      (error) => console.log(error)
    );
    
    this.htestfilesfiles[inx].splice(ind, 1);
  
  }

}
