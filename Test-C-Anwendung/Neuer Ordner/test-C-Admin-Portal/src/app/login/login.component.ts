import {Component, OnInit, Injectable} from '@angular/core';
import {Router} from "@angular/router";
import {LocalStorageService} from "angular-2-local-storage";
import {HttpErrorResponse} from "@angular/common/http";
import { LoginCredential } from '../pages/models/login-cridential';
import { LoginService } from '../pages/services/login.service';
import { MustMatch } from '../_helpers/must-match.validator';
import { FormGroup, Validators, FormBuilder, FormControl } from '@angular/forms';
@Injectable({
  providedIn: 'root',
})
@Component({
  selector: 'ngx-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  
  loginCredential: LoginCredential;
  message: string = "";
  loginForm: FormGroup;
  submitted = false;
  constructor(private loginService: LoginService, private router: Router, private localStorageService: LocalStorageService, private formBuilder: FormBuilder) {
  }
  user: string;
  ngOnInit() {
    this.loginCredential = new LoginCredential();
    if (localStorage.getItem('my-app.token') !== null) {
      this.router.navigate(["/pages"]);
    }

    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
  });
  }

  get f() { return this.loginForm.controls; }

  

  login() {
    this.loginCredential.username =this.f.username.value;
    this.loginCredential.password =this.f.password.value;
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
        return;
    }
    this.loginService.login(this.loginCredential).subscribe((data) => {
        console.log("Authorization is success :", this.localStorageService.get<string>('my-app.token') !== null)
        this.router.navigate(["/pages/upload-file"]);
      }, err => {
        let error = err as HttpErrorResponse;
        if (error.status === 401) {
          this.message = "Error";
        }
      }
    )
  }

  

}
