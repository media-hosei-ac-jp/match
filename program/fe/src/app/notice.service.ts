import { Injectable } from '@angular/core';
import { API_URL } from './app.module';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class NoticeService {

  private apiUrl = API_URL+'notice';
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true});

  constructor(private http: Http) { }

  // お知らせ取得
  getNotice() {
    const url: string = `${this.apiUrl}/getContent`;
    return this.http.get(url, this.options).map(res=>{
      return res.text();});
  }

  putNotice(content) {
    const url: string = `${this.apiUrl}/putContent`;
    return this.http.post(url, content, this.options).map(res=>{
        return res.text();});
  }
}
