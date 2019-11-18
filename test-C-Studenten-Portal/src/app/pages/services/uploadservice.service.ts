import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadserviceService {

  constructor(private http: HttpClient) { }

  pushFileToStorage(file: File, AufgabenblattN: string): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();

    formdata.append('file', file);
    const req = new HttpRequest('POST', 'http://localhost:8088/Aufgabenblattlosung?aufgabenblattN='+AufgabenblattN, formdata,  {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

    // HttpClient API get() method => Fetch employees list
   /* getEmployees(): Observable<Employee> {
      return this.http.get<Employee>(this.apiURL + '/employees')
      .pipe(
        retry(1),
        catchError(this.handleError)
      )
    }*/

    
    
}
