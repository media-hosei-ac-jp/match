import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { UserService } from '../user.service';
import { RecrService } from '../recr.service';
import { AppService } from '../app.service';
import { CommonService } from '../common.service';
import { UploadService } from '../upload.service';
import { SettingService } from '../setting.service';

// ゼミ応募画面用コンポーネントクラス
@Component({
  selector: 'app-apply',
  templateUrl: './apply.component.html',
  styleUrls: ['./apply.component.css']
})
export class ApplyComponent implements OnInit {

   settings;
   now = new Date();
   nowStr = this.commonService.formatDate(this.now, 'YYYY-MM-DD hh:mm');
   prMaxLength = 1200;

   user;
   sending = false;

  constructor(private loginService: LoginService,
              private userService: UserService,
              private appService: AppService,
              private recrService: RecrService,
              private commonService: CommonService,
              private settingService: SettingService
            //  private uploadService: UploadService,
            ) { }

  ngOnInit() {
    this.init();
  }

  init() {
    this.userService.reloadMe().subscribe(res=>{
      console.log(res);
      this.user = res;
      this.getSettings();
    });
  }

  getSettings() {
    this.settingService.getSetting().subscribe(s=>{
      this.settings = s;
      for(let i=0; i<s.length; i++) {
        s[i].tagArray = this.getProgressTagArray(s[i]);
        //console.log(s[i]);
        this.initSetting(s[i]);
      }
    });
  }

  initSetting(s) {
    this.appService.getActiveApps(s.id).subscribe(apps=>{
      s.apps = apps;

      if(!this.user.confirmed &&(apps.length < s.maximum && s.applicationStartDate<this.nowStr
        && this.nowStr < s.applicationEndDate)) {
        //init new app
        s.newApp = { recruitment: null};
        this.recrService.getActiveRecrs(s.id).subscribe(rs=>{
          s.recrs = rs.filter(e=>!e.confirmed);
        });
      }
    });

  }

  /// タグ取得
  getProgressTagArray(setting) {
    let action: string;

    if (setting.addRegistrationEndDate < this.nowStr) {
      action = "end"
    }
    else if (setting.selectionEndDate < this.nowStr) {
      action = "sel_end"
    }
    else if (setting.applicationEndDate < this.nowStr) {
      action = "appl_submitted"
    }
    else if (setting.applicationStartDate < this.nowStr) {
      action = 'appl_term';
    }
    else {
      action = "appl_before";
    }

    return this.commonService.getApplyProgressTagArray(action);
  }

  saveApp(app) {
    this.sending = true;
    console.log(app);
    this.appService.save(app).subscribe(res=>{
      console.log(res);
      alert('応募内容を保存しました。');
      this.init();
      this.sending = false;
    });
  }

  deleteApp(app) {
    this.sending = true;
    console.log(app);
    app.deleted = true;
    this.appService.save(app).subscribe(res=>{
      console.log(res);
      alert('応募を削除しました。')
      this.init();
      this.sending = false;
    });
  }

  confirmApp(app) {
    this.sending = true;
    this.appService.confirm(app).subscribe(res=>{
      console.log(res);
      alert('ゼミを確定しました。')
      this.init();
      this.sending = false;
    });
  }

  getRows(str, min) {
    let len = str?str.split('\n').length:1;
    return len>min?len:min;
  }
}
