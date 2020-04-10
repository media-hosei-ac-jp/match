import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { NoticeService } from '../notice.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  notice;
  isAdmin : boolean = false;
  isStudent : boolean = false;
  isTeacher : boolean = false;
  editing = false;

  constructor(  private loginService: LoginService,
                private noticeService: NoticeService) {

    this.isAdmin = loginService.isAdmin();
    this.isStudent = loginService.isStudent();
    this.isTeacher = loginService.isTeacher();
  }

  ngOnInit() {
    //console.log('HomeComponent:ngOnInit');

    this.getNotice();
    // console.log(this.noticeData);
  }

  getNotice() {
    //console.log('HomeComponent:getNotice');
    this.noticeService.getNotice().subscribe(res=>{
      this.notice=res;
      //console.log(this.noticeData);
      //console.log(this.notice);
    });
  }

  saveNotice() {
    this.noticeService.putNotice(this.notice).subscribe(res=>{
      this.notice=res;
    });
    this.editing = false;
  }

}
