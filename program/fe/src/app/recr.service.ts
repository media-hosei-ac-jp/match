import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import { Router } from '@angular/router';
import { API_URL } from './app.module';

@Injectable()
export class RecrService {

  private apiUrl = API_URL+'recruit';
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true});

  constructor(private router: Router, private http: Http) { }
/*
  getRecr() {

    const url: string = `${this.apiUrl}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }
  getRecrList() {

    const url: string = `${this.apiUrl}` + '/list';
    return this.http.get(url, this.options).map(res=>res.json());
  }
*/

  getMyRecrs() {
    const url: string = `${this.apiUrl}` + '/getMyRecrs';
    return this.http.get(url, this.options).map(res=>res.json());
  }

  putRecr(recrData) {
    const url: string = `${this.apiUrl}`;
  //  return this.http.put(url, recrData, this.options).do(res => {
  //      if(res.status === 200) this.router.navigate(['/recr']);
  //    }).map(res=>res.json());
    return this.http.put(url, recrData, this.options).map(res => res.json());
  }

  confirmSelection(recr, apps, isAdditional) {
    const url: string = `${this.apiUrl}/confirmSelection`;
    let param = {
      recr: recr,
      apps: apps,
      additional: isAdditional
    };
    return this.http.post(url, JSON.stringify(param), this.options).map(res => res.json());
  }

  getAllStats() {
    const url: string = `${this.apiUrl}` + '/getAllStats';
    return this.http.get(url, this.options).map(res=>res.json());
  }

  getActiveRecrs(settingId) {
    const url: string = `${this.apiUrl}` + '/getActiveRecrs/'+settingId;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  updateLimits(rds) {
    const url: string = `${this.apiUrl}/updateLimits`;
    return this.http.post(url, rds, this.options).map(res => res.json());
  }


  /* for admin */
  getAllStats4A(id) {
    const url: string = `${this.apiUrl}` + `/getAllStats4A/${id}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  getAllBySettingId4A(id) {
    const url: string = `${this.apiUrl}` + `/getAllBySettingId4A/${id}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  get4A(id) {
    const url: string = `${this.apiUrl}` + `/get4A/${id}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  save4A(r) {
    const url: string = `${this.apiUrl}` + `/save4A`;
    return this.http.post(url, r, this.options).map(res=>res.json());
  }
}
