import { Injectable } from '@angular/core';
import { API_URL } from './app.module';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import { Router } from '@angular/router';

@Injectable()
export class AppService {
  private apiUrl = API_URL+'app';
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true});

  constructor(private router: Router, private http: Http) { }

  /// 応募情報取得
  getApplication() {
    const url: string = `${this.apiUrl}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  save(app) {
    const url: string = `${this.apiUrl}`;
    return this.http.post(url, app, this.options).map(res=>res.json());
  }

  confirm(app) {
    const url: string = `${this.apiUrl}/confirm`;
    return this.http.post(url, app, this.options).map(res=>res.json());
  }

  private extractData(res: Response) {
   let body;
   if(res.text()) {
     body = res.json();
   }
   return body || {};
  }

  getActiveApps(settingId) {
    const url: string = `${this.apiUrl}/getActiveApps/${settingId}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  getAppsByRecrId(recrId) {
    const url: string = `${this.apiUrl}/getAppsByRecrId/${recrId}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  getAddAppsByRecrId(recrId) {
    const url: string = `${this.apiUrl}/getAddAppsByRecrId/${recrId}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  /* for admin */
  getAllByRecrId4A(id) {
    const url: string = `${this.apiUrl}/getAllByRecrId4A/${id}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  get4A(id) {
    const url: string = `${this.apiUrl}/get4A/${id}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  save4A(app) {
    const url: string = `${this.apiUrl}/save4A`;
    return this.http.post(url, app, this.options).map(res=>res.json());
  }

  sendResultMail(id) {
    const url: string = `${this.apiUrl}/sendResultMail/${id}`;
    return this.http.get(url, this.options).map(this.extractData);
  }

  getAllConfirmed4A() {
    const url: string = `${this.apiUrl}/getAllConfirmed4A`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  addNewAppByUid4A(rid, uid) {
    const url: string = `${this.apiUrl}/addNewAppByUid4A/${rid}/${uid}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  setConfirmedAllApps(sid) {
    const url: string = `${this.apiUrl}/setConfirmedAllApps/${sid}`;
    return this.http.get(url, this.options).map(this.extractData);
  }
}
