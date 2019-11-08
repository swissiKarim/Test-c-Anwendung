import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';
import { LocalStorageService } from 'angular-2-local-storage';


@Injectable({
  providedIn: 'root'
})
export class TrafikserviceService { 
  constructor(private httpClient: HttpClient, private localStorageService: LocalStorageService) { }




listfiles(augabenblattN: String, subfolder: String, isDirectory: Boolean){
  let token = this.localStorageService.get("token") as string;
  let headers = new HttpHeaders();
  headers.set("Authorization", token);
  return this.httpClient.get('http://localhost:8088/filesList?augabenblattN='+ augabenblattN +'&subfolder='+subfolder+'&isDirectory='+isDirectory);
}

deletfiles(filename: String, folder: String, subfolder: String){
  return this.httpClient.get('http://localhost:8088/deletfilefromaufgabenblatt?filename='+filename+'&folder='+folder+'&subfolder='+subfolder);
}

}
