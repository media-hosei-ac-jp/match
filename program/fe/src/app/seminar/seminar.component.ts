import { Component, OnInit, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { RequestOptions, Headers, Http } from '@angular/http';
import { Observable } from 'rxjs';

import { SeminarService } from '../seminar.service';
import { LoginService } from '../login.service';
import { UploadService } from '../upload.service';
import { API_URL } from '../app.module';

declare var jquery: any;
declare var $: any;

// ゼミ編集画面コンポーネント
@Component({
  selector: 'app-seminar',
  templateUrl: './seminar.component.html',
  styleUrls: ['./seminar.component.css']
})
export class SeminarComponent implements OnInit {
  fileApiUrl = API_URL+'file/download/';

  // コンポーネント使用バインド変数
  seminarData: any = { id: null, title: null, file: null };
  //seminar = {};
  //prFileData;
  err;
  msg: boolean = false;
  fileName = null;
  hdnFile = null;

  sending = false;

  //fileId = "#pdf_file";

  // コンストラクタ
  constructor(private seminarService: SeminarService,
    private loginService: LoginService,
    private uploadService: UploadService,
    private router: Router,
    private http: Http) {

  }

  // 初回ロジック
  ngOnInit() {
    this.getSeminar();
  }

  msgSubmitFile(seminarData) {
    if (seminarData.file == null) {
      alert('ゼミ紹介のPDFファイルが選択されていません。\n締切りまでに必ずアップロードしてください。')
    }
  }

  // ゼミ情報取得
  getSeminar() {
    this.seminarService.getSeminar().subscribe((seminarData) => {
      // 警告メッセージ
      this.msgSubmitFile(seminarData);
      this.seminarData = seminarData;
    });
  }

  // ゼミ情報設定
  setSeminar(seminarData) {
    if (seminarData.id == undefined) {
      return;
    }
    this.seminarData = seminarData;
  }


  // ゼミ情報更新
  putSeminar() {
    this.sending = true;
    //console.log(this.seminarData);

    let seminarData = this.seminarData;
    //seminarData.file = this.uploadService.setUploadFile(seminarData.file ? seminarData.file.id : null, seminarData.title + '_紹介.pdf', 'seminar_file');

    // 更新POST
    this.seminarService.putSeminar(seminarData).subscribe((res) => {
      this.setSeminar(res);
      this.err = null;
      // this.router.navigate(['/seminar']);
      // this.msg = true;
      alert('ゼミ情報を更新しました。');
      this.sending = false;
    }, (err) => {
      this.err = err;
      alert(this.err);
      this.sending = false;
    });

  }


  fileChange(event) {
    //this.uploadService.fileChange(imgfile, '#seminar_file', $('#title').val() + '_紹介.pdf', 'download');
    //if(!this.seminarData.file) {
    this.seminarData.file = { fileName: this.seminarData.title+'_紹介.pdf'};
    //}
    //console.log(event);
    let fileList: FileList = event.target.files;
    if(!fileList.length) return;

    let file=fileList[0];
    let reader= new FileReader();
    reader.onload = (src: any)=>{
        let fileUrl = src.target.result;
        console.log(src);
        let base64 = btoa(fileUrl);
        //$(this.fileId).val(base64);
        this.seminarData.file.content = base64;
      };

    reader.readAsBinaryString(file);
  }

  getRows(str, min) {
    let len = str ? str.split('\n').length : 1;
    return len > min ? len : min;
  }
}
