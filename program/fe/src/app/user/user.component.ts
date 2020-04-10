import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';

import {
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl
} from '@angular/forms';

//
// ユーザ画面用コンポーネントクラス
//
@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  // コンポーネント使用バインド変数
  userData = null;
  isStudent: boolean = false;
  err;
  msg: boolean = false;

  sending = false;

  dpOptions = {
    dateFormat: "yyyy-mm-dd",
    //monthLabels: { 1: '1月', 2: '2月', 3: '3月', 4: '4月', 5: '5月', 6: '6月', 7: '7月', 8: '8月', 9: '9月', 10: '10月', 11: '11月', 12: '12月' },
  }

  birthday;
  userForm: FormGroup;

  // コンストラクタ
  constructor(private userService: UserService,
              private loginService: LoginService,
              private router: Router) { }

  // 初回ロジック
  ngOnInit() {
    this.getUser();

    // jQueryプラグイン
    /*
    $('#birthday').datepicker({
      // 複数ある場合は , で区切る
      // データの入力形式 ( デフォルトは yy/mm/dd )
      dateFormat: 'yy-mm-dd',
          changeMonth: true,
          changeYear: true,
          showButtonPanel: true,
          yearRange: 'c-32:c',
          defaultDate: 0
    });

    // 検証(jQueryプラグイン)
    $('#user').h5Validate({
      errorClass:'error'
    });
    */

  }

  // ユーザデータ取得
  getUser() {
    this.userService.reloadMe().subscribe((userData)=>{
      this.userData=userData;
      this.birthday= "2017-10-27";
  //  console.log(this.birthday);

      this.loginService.setUser(userData);
      this.isStudent = this.loginService.isStudent();
      console.log(userData);
    });
  }

  // ユーザデータ設定
  setUser(userData) {
    if(userData.id==undefined) {
      return;
    }
    this.userData=userData;
  }

  // ユーザデータ更新
  putUser() {
  //  console.log(this.birthday)
    this.sending = true;

    this.userService.putUser(this.userData).subscribe((res)=>{
      this.setUser(res);
      this.err = null;
      // this.router.navigate(['/user']);
      this.msg = true;
      alert('ユーザデータを更新しました。');
      this.sending = false;
    },(err)=>{
      this.err = err;
      console.log(this.err);
      alert(this.err);
      this.sending = false;
    });
  }

/*
  OnKeyUp(event: any){
    // AjaxZip3.zip2addr(this,'','address','address');
  }
  setBirthday(user, $event) {
    console.log($event);
    user.birthday=$event+' 00:00';
  }
  */

  onDateChanged(event) {
    console.log(event);
    this.userData.birthday = event.formatted +' 00:00:00';
  }

}
