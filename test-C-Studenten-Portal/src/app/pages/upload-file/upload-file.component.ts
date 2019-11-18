import { Component, Input, OnInit} from '@angular/core';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadserviceService } from '../services/uploadservice.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TrafikserviceService } from '../services/trafikservice.service';

@Component({
  selector: 'ngx-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.scss'],
})
export class UploadFileComponent implements OnInit {
  spinner:boolean = false;
  files: any = [];
  selectedFiles: FileList;
  currentFileUpload: File;
  fileName: String;
  isFilehier:boolean = false;
  isUploded:boolean = false;
  resp: String = "";
  showSpinner: boolean = true;
  isdes: boolean = true;
  @Input() selectedCurrency: string = '/';
  constructor(private uploadService: UploadserviceService,private trafikeservice: TrafikserviceService, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.isdes = true;
  }

inisialise(){
  this.showSpinner = true;
  this.resp ="";
}

  uploadFile(event) {
    this.isFilehier = true;
    for (let index = 0; index < event.length; index++) {
    this.selectedFiles = event;
    this.currentFileUpload = this.selectedFiles.item(index);
    this.fileName = this.currentFileUpload.name;
    console.log(this.currentFileUpload);
  
   
  }

   
  }


  
  pushtoserve(newAufgabenblatt){
    this.uploadService.pushFileToStorage(this.currentFileUpload, newAufgabenblatt).subscribe(event => {
     // console.log(this.currentFileUpload.name);
    });
    this.spinner = true;
    setTimeout(() => this.deleteAttachment(), 3000);
    
    this.selectedFiles = undefined;
    this.isdes = false;
  }

  deleteAttachment() {
    this.currentFileUpload= null;
    this.isFilehier = false;
    this.spinner = false;
  }
  


  currencies: string[] = ['/', 'src', 'test'];

  changeCurrency(currency) {
    if (this.selectedCurrency !== currency) {
      this.selectedCurrency = currency;
      console.log(this.selectedCurrency);
    }
  }
  loadingLargeGroup = false;
  loadingMediumGroup = false;

  toggleLoadingLargeGroupAnimation() {
    this.loadingLargeGroup = true;

    setTimeout(() => this.loadingLargeGroup = false, 3000);
  }

  toggleLoadingMediumGroupAnimation() {
    this.loadingMediumGroup = true;

    setTimeout(() => this.loadingMediumGroup = false, 3000);
  }
 
  gettestergib(aufgabenblattN){
    this.trafikeservice.getTestErgibnes(aufgabenblattN).subscribe(data => {
     this.resp= data.body;
     });
  }

  jenkinsbuildf(aufgabenblattN){
    this.trafikeservice.jenkinsBuild(aufgabenblattN).toPromise().then(data=>{
      setTimeout(() => { 
        this.trafikeservice.getTestErgibnes(aufgabenblattN).subscribe(data => {
          this.resp= data.body;
          });
          this.showSpinner = false;
      }, 3000);

    });
  }

  }

