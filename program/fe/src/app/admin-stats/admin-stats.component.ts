import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RecrService } from '../recr.service';
import { AppService } from '../app.service';
import { CommonService } from '../common.service';
import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';
//import * as jsPDF from 'jspdf';

@Component({
  selector: 'app-admin-stats',
  templateUrl: './admin-stats.component.html',
  styleUrls: ['./admin-stats.component.css']
})
export class AdminStatsComponent implements OnInit {
  //stats$;
  stats;

  constructor(private route: ActivatedRoute,
              private recrService: RecrService,
              private appService: AppService,
              private commonService: CommonService) { }

  ngOnInit() {
    let id = this.route.snapshot.params['id'];
    if(id) {
      //this.stats$ = this.recrService.getAllStats4A(id);
      this.getStats(id);
    }
  }

  getStats(id) {
    this.recrService.getAllStats4A(id).subscribe(r=>{
      this.stats = r;
      //console.log(r);
      this.fetchAppInfo();
    });
  }

  sum(a) {
    return a.reduce((p,c)=>p+c);
  }

  appendArray(a, counts) {
    for(let i=1; i<=4; i++) {
      a.push(counts[i]);
    }
    a.push(this.sum(counts));

  }

  appendArray2(a, counts) {
    for(let i=1; i<=4; i++) {
      a.push(counts[i]);
    }
  }

  download() {
    let fileName = this.stats.setting.title+'_集計結果';

    let data1 = [];
    data1.push(['','1年生','2年生','3年生','4年生','合計'])

    const stats = this.stats;
    let a = ['応募者数'];
    this.appendArray(a, stats.applicantCounts);
    data1.push(a);
    a = ['初回合格者数'];
    this.appendArray(a, stats.passCounts);
    data1.push(a);
    a = ['追加合格者数'];
    this.appendArray(a, stats.addPassCounts);
    data1.push(a);
    a = ['初回ゼミ確定者数'];
    this.appendArray(a, stats.confirmCounts);
    data1.push(a);
    a = ['追加合格ゼミ確定者数'];
    this.appendArray(a, stats.addConfirmCounts);
    data1.push(a);

    let data2 = [];
    a = ['ゼミ名称','教職員番号'];
    const header = ['応募','初回合格','追加合格','初回確定','追加確定'];
    for(let i=0; i<header.length; i++) {
      for(let g=1; g<=4; g++) {
        a.push(header[i]+' '+g+'年');
      }
    }
    data2.push(a);

    for(let i=0; i<stats.rsdl.length; i++) {
      const rs = stats.rsdl[i];
      a = [rs.recr.seminar.title, rs.recr.seminar.owner.uid];

      this.appendArray2(a, rs.appCounts);
      this.appendArray2(a, rs.passCounts);
      this.appendArray2(a, rs.addPassCounts);
      this.appendArray2(a, rs.confirmCounts);
      this.appendArray2(a, rs.addConfirmCounts);
      data2.push(a);
    }



    this.save(fileName, data1, data2);
  }

  s2ab(s) {
    let buf = new ArrayBuffer(s.length);
    let view = new Uint8Array(buf);
    for (let i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
  }


  save(fileName, data1, data2) {
    /* generate worksheet */
    let ws: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(data1);
    /* generate workbook and add the worksheet */
    let wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, '全体'); //Sheet1

    //二つ目のシート追加
    ws = XLSX.utils.aoa_to_sheet(data2);
    /* generate workbook and add the worksheet */
    //wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, '詳細'); //Sheet1

    /* save to file */
    const wbout: string = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
    FileSaver.saveAs(new Blob([this.s2ab(wbout)]), fileName+'.xlsx');
  }

  downloadRecr() {

    let printContents, popupWin;
      printContents = document.getElementById('print-recr').innerHTML;
      popupWin = window.open('', '_blank', 'top=0,left=0,height=100%,width=auto');
      popupWin.document.open();
      popupWin.document.write(`
        <html>
          <head>
            <title>Print tab</title>
            <style>
              h2 {
                text-align: center;
              }

              h4 {
                text-align: center;
              }
              ul {
                border-style: solid;
                list-style-type: none;
              }

              ul li {
              //  border-style: solid;
              }
            </style>
          </head>
      <body onload="window.print();window.close()">${printContents}</body>
        </html>`
      );
      popupWin.document.close();
  }

  /* 応募情報を取得 */
  fetchAppInfo() {
    this.stats.rsdl.forEach(r=>{
        let recr = r.recr;
        this.appService.getAppsByRecrId(recr.id).subscribe(apps=>{
        //pr null対策
        apps.forEach(a=>{
          if(!a.pr) {
            a.pr = "";
          }
        });

        //console.log(apps);
        let tapps = apps.filter((element, index, array)=>{
          return element.passed!=null || !element.deleted;
        });
        recr.apps = tapps;

        //for print-sel
        let passedApps = []; //passedApps[grade][i];
        for(let i=1; i<=4; i++) {
          passedApps[i] = [];
          for(let j=0; j<apps.length; j++) {
            if(apps[j].passed && apps[j].owner.grade==i) {
              passedApps[i].push(apps[j]);
            }
          }
        }
        recr.passedApps = passedApps;

        //for print-sel-add
        let passedAddApps = []; //passedAddApps[grade][i];
        for(let i=1; i<=4; i++) {
          passedAddApps[i] = [];
          for(let j=0; j<apps.length; j++) {
            if(apps[j].passed && apps[j].addPassed && apps[j].owner.grade==i) {
              passedAddApps[i].push(apps[j]);
            }
          }
        }
        recr.passedAddApps = passedAddApps;

        //追加合格対象者を取得
        let assd = recr.setting.addSelectionStartDate;
        let now = new Date();
        let nowStr = this.commonService.formatDate(now, 'YYYY-MM-DD hh:mm')
        if(assd && nowStr > assd) {

          //console.log(this.recrs[i])
          this.appService.getAddAppsByRecrId(recr.id).subscribe(apps=>{
            //pr null対策
            apps.forEach(a=>{
              if(!a.pr) {
                a.pr = "";
              }
            });

            //console.log(apps);
            let tapps = apps.filter((element, index, array)=>{
              return !element.deleted;
            });
            recr.addApps = tapps;
          });
        }
      });
    });
  }

  downloadPass() {
        let printContents, popupWin;
          printContents = document.getElementById('print-sel').innerHTML;
          popupWin = window.open('', '_blank', 'top=0,left=0,height=100%,width=auto');
          popupWin.document.open();
          popupWin.document.write(`
            <html>
              <head>
                <title>Print tab</title>
                <style>
                  table.bordered {
                    border: solid;
                    border-collapse: collapse;
                  }

                  table.bordered th {
                    border-style: solid;
                  }

                  table.bordered td {
                    text-align: center;
                    border-style: solid;
                  }
                  ul {
                    list-style-type: none;
                  }
                </style>
              </head>
          <body onload="window.print();window.close()">${printContents}</body>
            </html>`
          );
          popupWin.document.close();

  }

  downloadAddPass() {
        let printContents, popupWin;
          printContents = document.getElementById('print-sel-add').innerHTML;
          popupWin = window.open('', '_blank', 'top=0,left=0,height=100%,width=auto');
          popupWin.document.open();
          popupWin.document.write(`
            <html>
              <head>
                <title>Print tab</title>
                <style>
                  table.bordered {
                    border: solid;
                    border-collapse: collapse;
                  }

                  table.bordered th {
                    border-style: solid;
                  }

                  table.bordered td {
                    text-align: center;
                    border-style: solid;
                  }
                  ul {
                    list-style-type: none;
                  }
                </style>
              </head>
          <body onload="window.print();window.close()">${printContents}</body>
            </html>`
          );
          popupWin.document.close();

  }

  confirmAll() {
    this.appService.setConfirmedAllApps(this.stats.setting.id).subscribe(()=>{
      alert('一括確定処理を実行しました。');
    },()=>{
      alert('エラーが発生しました。');
    });
  }
}
