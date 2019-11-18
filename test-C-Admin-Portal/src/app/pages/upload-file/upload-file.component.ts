import { Component, Input} from '@angular/core';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadserviceService } from '../services/uploadservice.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'ngx-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.scss'],
})
export class UploadFileComponent {
  spinner:boolean = false;
  files: any = [];
  selectedFiles: FileList;
  currentFileUpload: File;
  @Input() selectedCurrency: string = '/';
  constructor(private uploadService: UploadserviceService, private fb: FormBuilder) { }

  uploadFile(event) {
    for (let index = 0; index < event.length; index++) {
      const element = event[index];
    this.selectedFiles = event;
    this.currentFileUpload = this.selectedFiles.item(index);
    this.files.push(element);
   
  }

   
  }

  pushtoserve(newAufgabenblatt , index){
    this.uploadService.pushFileToStorage(this.files[index],  newAufgabenblatt, this.selectedCurrency).subscribe(event => {
      console.log(this.files[index].name);
    });
    this.spinner = true;
    setTimeout(() => this.deleteAttachment(index), 3000);
   
    this.selectedFiles = undefined;
   
  }

  deleteAttachment(index) {
    this.files.splice(index, 1);
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
 

  }

