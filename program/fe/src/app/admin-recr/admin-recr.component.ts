import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RecrService } from '../recr.service';
import { AppService } from '../app.service';

@Component({
  selector: 'app-admin-recr',
  templateUrl: './admin-recr.component.html',
  styleUrls: ['./admin-recr.component.css']
})
export class AdminRecrComponent implements OnInit {
  recr;
  sending;
  apps$;
  newAppUid;

  constructor(private route: ActivatedRoute,
              private recrService: RecrService,
              private appService: AppService) { }

  ngOnInit() {
    this.init();
  }

  init() {
    let id = this.route.snapshot.params['id'];
    if(id) {
      this.getRecr(id);
      this.apps$ = this.appService.getAllByRecrId4A(id);
    }
  }

  getRecr(id) {
    this.recrService.get4A(id).subscribe(r=>this.recr=r);
  }

  saveRecr(r) {
    this.sending = true;
    this.recrService.save4A(r).subscribe(r=>{
      this.recr=r;
      this.sending = false;
      alert('募集情報を更新しました。');
    });
  }

  getRows(str, min) {
    let len = str?str.split('\n').length:1;
    return len>min?len:min;
  }
  addNewApp() {
    this.appService.addNewAppByUid4A(this.recr.id, this.newAppUid).subscribe(
      r=>{
        alert('新規追加に成功しました。')
        this.init();
      },
      err=>{
        alert('新規追加に失敗しました。')
      });
  }
}
