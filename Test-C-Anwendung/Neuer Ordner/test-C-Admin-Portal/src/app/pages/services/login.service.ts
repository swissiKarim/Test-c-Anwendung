import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoginCredential} from "../models/login-cridential";
import 'rxjs/add/operator/map';
import "rxjs/add/operator/catch";
import 'rxjs/add/observable/throw';
import {environment} from "../../../environments/environment";
import {LocalStorageService} from 'angular-2-local-storage';
import {Token} from "../../login/Token";
import { Observable } from 'rxjs/Rx'

@Injectable()
export class LoginService {

  constructor(private http: HttpClient, private localStorageService: LocalStorageService) {
  }

  login(loginCredential: LoginCredential): Observable<any> {
    var body = JSON.stringify(loginCredential);
    
    let headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log(headers);
    return this.http.post(environment.baseUrl + '/auth', body, {
      headers: headers,
      responseType: 'json',
      observe: 'body'
     
    }).map(response => {
      let token = response as Token;
      this.localStorageService.set("token", token.token);
      return token;
      
    }).catch(err => {
      return Observable.throw(err);
    
    });
  }
 
  getCurrentUser(): Observable<string> {
    let token = localStorage.getItem('my-app.token') as string;
    let headers = new HttpHeaders();
    headers.set("Authorization", token);
    return this.http.get(environment.baseUrl + "/getusercurrent", {
      observe: 'body',
      responseType: 'text',
      headers: headers
    }).map(resp => resp).catch(err => Observable.throw(err));

    
  }

}
