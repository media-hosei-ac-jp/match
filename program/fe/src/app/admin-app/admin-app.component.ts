import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RecrService } from '../recr.service';
import { AppService } from '../app.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-admin-app',
  templateUrl: './admin-app.component.html',
  styleUrls: ['./admin-app.component.css']
})
export class AdminAppComponent implements OnInit {
  app;
  recrs;
  sending;

  constructor(private route: ActivatedRoute,
              private recrService: RecrService,
              private appService: AppService,
              private userService: UserService) { }

  ngOnInit() {
    let id = this.route.snapshot.params['id'];
    if(id) {
      this.getApp(id);
    }
  }

  getApp(id) {
    this.appService.get4A(id).subscribe(r=>{
      this.app=r;
      this.getRecrs(r.recruitment.setting.id);
    });
  }

  saveApp(app) {
    this.sending = true;
    this.appService.save4A(app).subscribe(r=>{
      this.app = r;
      alert('応募情報を更新しました。');
      this.sending=false;
    });
  }

  getRecrs(id) {
    this.recrService.getAllBySettingId4A(id).subscribe(r=>this.recrs=r);
  }

  getRows(str, min) {
    let len = str?str.split('\n').length:1;
    return len>min?len:min;
  }

  confirmApp(confirmed) {
    this.sending = true;
    let u = this.app.owner;
    u.confirmed = confirmed;
    this.userService.save4A(u).subscribe(r=>{
      let a = this.app;
      a.confirmed = confirmed;
      this.appService.save4A(a).subscribe(r2=>{
        this.app = r2;
        alert('確定状態を変更しました。');
        this.sending = false;
      });
    });
  }

  deleteApp(deleted) {
    this.sending = true;
    this.app.deleted = deleted;
    this.appService.save4A(this.app).subscribe(r=>{
      this.app = r;
      alert('削除状態を変更しました。');
      this.sending = false;
    });
  }

  set1stPassed(passed) {
    this.sending = true;
    this.app.addPassed = null;
    this.app.passed = passed;
    this.appService.save4A(this.app).subscribe(r=>{
      this.app = r;
      alert('合否を変更しました。');
      this.sending = false;
    });
  }

  setAddPassed(passed) {
    this.sending = true;
    this.app.passed = passed;
    this.app.addPassed = passed;
    this.appService.save4A(this.app).subscribe(r=>{
      this.app = r;
      alert('合否を変更しました。');
      this.sending = false;
    });
  }

  sendResultMail(id) {
    this.sending = true;
    this.appService.sendResultMail(id).subscribe(r=>{
      alert('選考結果メールを送信しました。');
      this.sending = false;
    });
  }

}
