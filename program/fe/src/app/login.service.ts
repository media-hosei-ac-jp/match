import { Injectable } from '@angular/core';
import { API_URL } from './app.module';
import { Headers, Http, RequestOptions, URLSearchParams } from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class LoginService {
  private auth: boolean;
  private user = {userId: null, roles: null, uid: null};
  private admin: boolean;

  private apiUrl = API_URL;
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true});

  constructor(private http: Http) { }

  setUser(user) {
    this.user = user;
  }

  getUser() {
    return this.user;
  }

  isAuth(): boolean {
    return this.user.userId!=null;
  }

  isAdmin(): boolean {
    return this.user.roles.some(r=>r=='ADMIN');
  }

  isStudent(): boolean {
    return this.user.roles.some(r=>r=='STUDENT');
  }

  isTeacher(): boolean {
    return this.user.roles.some(r=>r=='TEACHER');
  }

  // login(id, password) {
  //   const url: string = `${this.apiUrl}login`;
  //   let param = {
  //     id: id,
  //     password: password
  //   };
  //   return this.http.post(url, param, this.options).map(res=>res.json());
  // }
}
