import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadFileService {

  constructor(private http: HttpClient) { }
  public sidebarWidth = 250;
  sidebarVisible = true;

  public getSidebarWidth(): number {
    return this.sidebarVisible ? this.sidebarWidth : 0;
  }
  pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();

    formdata.append('ulam', file);
    const req = new HttpRequest('POST', 'http://localhost:8088/uploadFile?AufgabeblattNum=auf1', formdata,  {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

  sendFileToTarget(filename: string): Observable<HttpResponse<string>> {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'text/csv; charset=utf-8');
    return this.http.get('http://localhost:8088/downloadFile/ulam.c', {
      headers: headers,
      observe: 'response',
      responseType: 'text'
    });
  }

  getTestErgibnes(): Observable<HttpResponse<string>>{
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'text/csv; charset=utf-8');
    return this.http.get('http://localhost:8088/getfilefrompath', {
      headers: headers,
      observe: 'response',
      responseType: 'text'
    });

  }


  pushFileToTestUmgebung(): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:8088/pushFileToRepo', {responseType: 'json'});
  }

  jenkinsBuildReq(): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:8088/buildJenkinsJob', {responseType: 'json'});
  }

  jenkinsConsolOutput(): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:8088/jenkinsConsolOutput', {responseType: 'json'});
  }
}
