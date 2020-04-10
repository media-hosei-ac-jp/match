import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { API_URL } from './app.module';

@Injectable()
export class AdminService {
  private apiUrl = API_URL+'admin';
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true})

  constructor(private http: Http) { }

  login(uid) {
    const url: string = `${this.apiUrl}/login`;
    let params: URLSearchParams = new URLSearchParams();
    params.set('uid', uid);
    let options = this.options.merge({search: params})
    return this.http.get(url, options).map(res=>res.json());
  }
}
