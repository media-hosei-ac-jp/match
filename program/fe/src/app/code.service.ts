import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import { Router } from '@angular/router';
import { API_URL } from './app.module';

///
/// コードAPI用サービス.
///
@Injectable()
export class CodeService {

  // 内部変数群.
  private apiUrl = API_URL+'code';
  private headers = new Headers({'Content-Type': 'application/json'});
  private options = new RequestOptions({ headers: this.headers, withCredentials: true});

  // コンストラクタ.
  constructor(private router: Router, private http: Http) { }

  // コードリスト取得.
  public getCodeList(divide) {
    const url: string = `${this.apiUrl}?cd=${divide}`;
    let myParams = new URLSearchParams();
    myParams.append('divide', divide);

    let options = new RequestOptions({ headers: this.headers, params: myParams, withCredentials: true });

    return this.http.get(url, this.options).map(res=>res.json());
  }

}
