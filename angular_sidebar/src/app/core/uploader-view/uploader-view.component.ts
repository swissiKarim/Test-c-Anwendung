import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadFileService } from '../../services/upload-file.service';
import {Router} from '@angular/router';
@Component({
  selector: 'app-uploader-view',
  templateUrl: './uploader-view.component.html',
  styleUrls: ['../allCss/allCssFile.css']
})
export class UploaderViewComponent implements OnInit {

  isLoading = false;
  isjenkinsBuild = false;
  ispushed = false;
  isconsolOut = false;
  files: any = [];
  selectedFiles: FileList;
  currentFileUpload: File;
  progress: { percentage: number } = { percentage: 0 };
  filename: String = null;


  constructor(private uploadService: UploadFileService, private router: Router, private ngxService: NgxUiLoaderService) { }


  ngOnInit() {

  }

  nglaoder(){

    this.ngxService.start(); // start foreground spinner of the master loader with 'default' taskId
    // Stop the foreground loading after 5s
    setTimeout(() => {
      this.ngxService.stop(); // stop foreground spinner of the master loader with 'default' taskId
    }, 8000);

    // OR
    this.ngxService.startBackground('do-background-things');
    // Do something here...
    this.ngxService.stopBackground('do-background-things');

    this.ngxService.startLoader('loader-01'); // start foreground spinner of the loader "loader-01" with 'default' taskId
    // Stop the foreground loading after 5s
    setTimeout(() => {
      this.ngxService.stopLoader('loader-01'); // stop foreground spinner of the loader "loader-01" with 'default' taskId
    }, 8000);
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        console.log('File is completely uploaded!');
      }
    });

    this.selectedFiles = undefined;
  }

  totestumgeb() {
    this.nglaoder();
   this.uploadService.sendFileToTarget('ulam').toPromise().then(response => {
      this.filename = response.headers.get('filename');
      console.log('sendFileToTarget' + response.ok);
     this.uploadService.pushFileToTestUmgebung().toPromise().then(data => {
       this.ispushed = data;
       console.log('pushFileToTestUmgebung' + this.ispushed);
       this.uploadService.jenkinsBuildReq().toPromise().then(data1 => {
         this.isjenkinsBuild = data1;
         console.log('jenkinsBuildReq' + this.isjenkinsBuild);
         setTimeout(() => { this.uploadService.jenkinsConsolOutput().toPromise().then(data2 => {
           this.isconsolOut = data2;
           console.log('jenkinsConsolOutput' + this.isconsolOut);

             this.router.navigateByUrl('/testergibnes');

         });
         }, 6000);


      //------
       });
          });
   });






  }
  pushFileToTest() {
    this.uploadService.pushFileToTestUmgebung().toPromise().then(data => {
      this.ispushed = data;
      console.log(this.ispushed);
    });


  }
jenkinsBuld() {
  this.uploadService.jenkinsBuildReq().subscribe(() => {
    console.log('Build');
  });
}
  uploadFile(event) {
    for (let index = 0; index < event.length; index++) {
      const element = event[index];
      this.files.push(element.name);
    }

  }
  deleteAttachment(index) {
    this.files.splice(index, 1);
  }

}
