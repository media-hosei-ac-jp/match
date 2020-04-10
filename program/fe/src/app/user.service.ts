import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import { Router } from '@angular/router';
import { API_URL } from './app.module';

// UserController用サービス
@Injectable()
export class UserService {
  private apiUrl = API_URL+'user';
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true});

  constructor(private router: Router, private http: Http) { }


  logout() {
    const url: string = `${this.apiUrl}/me`;
    //return this.http.get(url, this.options).toPromise().then(res=>res.json().data);
    return this.http.get(url, this.options).map(res=>res.json());
  }

  me() {
    const url: string = `${this.apiUrl}/me`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  reloadMe() {
    const url: string = `${this.apiUrl}/reloadMe`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  getUser() {
    const url: string = `${this.apiUrl}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  putUser(userData) {
    const url: string = `${this.apiUrl}`;
    return this.http.put(url, userData, this.options).map(res=>res.json());
  }

  addStudents(users) {
    const url: string = `${this.apiUrl}/addStudents`;
    return this.http.post(url, users, this.options).map(res=>res.json());
  }

  addTeachers(users) {
    const url: string = `${this.apiUrl}/addTeachers`;
    return this.http.post(url, users, this.options).map(res=>res.json());
  }

  addAdmin(users) {
    const url: string = `${this.apiUrl}/addAdmin`;
    return this.http.post(url, users, this.options).map(res=>res.json());
  }

  private extractData(res: Response) {
     let body;
     if(res.text()) {
       body = res.json();
     }
     return body || {};
   }

   /* for admin */
   save4A(u) {
     const url: string = `${this.apiUrl}/save4A`;
     return this.http.post(url, u, this.options).map(res=>res.json());
   }

}
