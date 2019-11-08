import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadserviceService {

  constructor(private http: HttpClient) { }

  pushFileToStorage(file: File, newAufgabenblatt: string, currency:String): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();

    formdata.append('file', file);
    const req = new HttpRequest('POST', 'http://localhost:8088/Aufgabenblatt?folder='+newAufgabenblatt+'&subfolder='+currency, formdata,  {
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
