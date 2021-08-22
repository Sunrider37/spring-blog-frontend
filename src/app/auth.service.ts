import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http'
import { RegisterPayload } from './auth/register/register-payload';
import { Observable } from 'rxjs';
import { LoginPayload } from './auth/login/login-payload';
import { JwtAuthResponse } from './auth/register-success/jwt-auth-response';
import {map} from 'rxjs/operators'
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
 
  private url = 'http://localhost:8080/api/auth/';

  constructor(private httpClient: HttpClient, private localStoraqeService: LocalStorageService) {
  }

  register(registerPayload: RegisterPayload): Observable<any> {
    return this.httpClient.post(this.url + 'signup', registerPayload, {
      responseType: 'text'
    });
  }

  login(loginPayload: LoginPayload): Observable<boolean> {
    return this.httpClient.post<JwtAuthResponse>(this.url + 'login', loginPayload).pipe(map(data => {
      this.localStoraqeService.store('authenticationToken', data.authenticationToken);
      this.localStoraqeService.store('username', data.username);
      return true;
    }));
  }

  isAuthenticated() : Boolean{
   return  this.localStoraqeService.retrieve('username') != null && 
   this.localStoraqeService.retrieve('authenticationToken') != null;
   
  }

  logout() {
    this.localStoraqeService.clear('authenticationToken');
    this.localStoraqeService.clear('username');
  }

}
