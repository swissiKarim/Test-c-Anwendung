import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class TrafikserviceService { 
  constructor(private httpClient: HttpClient) { }




listfiles(augabenblattN: String, subfolder: String, isDirectory: Boolean){
  return this.httpClient.get('http://localhost:8088/filesList?augabenblattN='+ augabenblattN +'&subfolder='+subfolder+'&isDirectory='+isDirectory);
}

deletfiles(filename: String, folder: String, subfolder: String){
  return this.httpClient.get('http://localhost:8088/deletfilefromaufgabenblatt?filename='+filename+'&folder='+folder+'&subfolder='+subfolder);
}

getTestErgibnes(augabenblattN: String): Observable<HttpResponse<any>>{
  let headers = new HttpHeaders();
  headers = headers.append('Accept', 'text/csv; charset=utf-8');
  return this.httpClient.get('http://localhost:8088/jenkinsBuildlog?aufgabenblattN='+augabenblattN, {
    headers: headers,
    observe: 'response',
    responseType: 'text'
  });

}




jenkinsBuild(AufgabenblattN: string): Observable<boolean> {
  return this.httpClient.get<boolean>('http://localhost:8088/jenkinsBuild?aufgabenblattN='+AufgabenblattN, {responseType: 'json'});
}

}
