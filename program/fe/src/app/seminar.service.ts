import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import { Router } from '@angular/router';
import { API_URL } from './app.module';

// SeminarController用サービス
@Injectable()
export class SeminarService {

  private apiUrl = API_URL+'seminar';
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true});

  constructor(private router: Router, private http: Http) { }

  getSeminar() {
    const url: string = `${this.apiUrl}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  putSeminar(seminarData) {
    const url: string = `${this.apiUrl}`;
    return this.http.put(url, seminarData, this.options).map(res=>res.json());
  }

  private extractData(res: Response) {
     let body;
     if(res.text()) {
       body = res.json();
     }
     return body || {};
   }


}
