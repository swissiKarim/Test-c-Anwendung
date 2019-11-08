import {Component, OnInit} from '@angular/core';
import {UploadFileService} from '../../services/upload-file.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-test-ergibnes',
  templateUrl: './test-ergibnes.component.html',
  styleUrls: ['../allCss/allCssFile.css']
})
export class TestErgibnesComponent implements OnInit {


  resp: String;
  showSpinner = false;
  constructor(private uploadService: UploadFileService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    setTimeout(() => { this.uploadService.getTestErgibnes().toPromise().then(data2 => {
      this.resp = data2.body;
console.log(this.resp);
    });
    this.showSpinner = true;
      }, 10000);
  }


}
