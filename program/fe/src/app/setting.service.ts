import { Injectable } from '@angular/core';
import { API_URL } from './app.module';
import { Headers, Http, RequestOptions } from '@angular/http';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class SettingService {
  private apiUrl = API_URL+'setting';
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true});

  private setting;

  constructor(private router: Router,
              private http: Http) { }

  getSetting() {
    const url: string = `${this.apiUrl}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }


  public saveSetting(setting) {
    const url: string = `${this.apiUrl}`;
    return this.http.post(url, setting, this.options).map(res=>res.json());
  }



}
