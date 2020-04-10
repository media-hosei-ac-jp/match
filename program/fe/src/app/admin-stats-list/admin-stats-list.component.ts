import { Component, OnInit } from '@angular/core';
import { SettingService } from '../setting.service';
import { AppService } from '../app.service';
import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-admin-stats-list',
  templateUrl: './admin-stats-list.component.html',
  styleUrls: ['./admin-stats-list.component.css']
})
export class AdminStatsListComponent implements OnInit {
  settings$;
  sending = false;

  constructor(private settingService: SettingService,
              private appService: AppService) { }

  ngOnInit() {
    this.settings$ = this.settingService.getSetting();
  }

  download() {
    this.sending = true;
    let fileName = '配属結果';
    this.appService.getAllConfirmed4A().subscribe(res=>{
      //console.log(res);
      let data = [];
      data.push(['学生証番号','姓','名','メールアドレス','配属先ゼミ','合格した選考期間','応募日時'])
      res.forEach(app=>{
        let a = [];
        a.push(app.owner.uid);
        a.push(app.owner.familyName);
        a.push(app.owner.givenName);
        a.push(app.owner.email);
        a.push(app.recruitment.seminar.title);
        a.push(app.recruitment.setting.title);
        a.push(app.createDate);
        data.push(a);
      });
      //console.log(data);
      this.save(fileName, data);
      this.sending = false;
    });


  }

  s2ab(s) {
    let buf = new ArrayBuffer(s.length);
    let view = new Uint8Array(buf);
    for (let i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
  }


  save(fileName, data) {
    /* generate worksheet */
    let ws: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(data);
    /* generate workbook and add the worksheet */
    let wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, '全体'); //Sheet1

    /* save to file */
    const wbout: string = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
    FileSaver.saveAs(new Blob([this.s2ab(wbout)]), fileName+'.xlsx');
  }

}
