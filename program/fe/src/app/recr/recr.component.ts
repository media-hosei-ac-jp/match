import { Component, OnInit } from '@angular/core';
import { CommonService } from '../common.service';
import { RecrService } from '../recr.service';
import { SettingService } from '../setting.service';
import { CodeService } from '../code.service';
import { AppService } from '../app.service';
import { PapaParseService } from 'ngx-papaparse';
import { Modal } from 'ngx-modialog/plugins/bootstrap';

import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';

//
// 募集画面用コンポーネントクラス
//
@Component({
  selector: 'app-recr',
  templateUrl: './recr.component.html',
  styleUrls: ['./recr.component.css']
})
export class RecrComponent implements OnInit {

  tagArray =  [];
  pmsg;
  err;
  selectionMeansNameArray = [];
  now = new Date();
  nowStr = this.commonService.formatDate(this.now, 'YYYY-MM-DD hh:mm');

  recrs;
  selectedApp;
  sending = false;

  showPr = false;

  // コンストラクタ
  constructor(private recrService: RecrService,
              private settingService: SettingService,
              private commonService: CommonService,
              private appService: AppService,
              private papa: PapaParseService,
              private modal: Modal) { }

  ngOnInit() {
    //this.getSetting();
    //this.getRecr();
    this.getMyRecrs();

    let showPr = localStorage.getItem('showPr');
    if(showPr!=null) {
      this.showPr = JSON.parse(showPr);
    }
  }

  /// タグ取得
  getProgressTagArray(recrData) {
    var action: string;

  //  if (recrData == null || recrData.recruitment == null) {
    if (recrData == null) {
      action = 'recr_add';
    }else {
      if(recrData.setting.selectionStartDate < this.nowStr) {
        action = 'recr_sel';
      }
      else if (recrData.setting.recruitmentEndDate < this.nowStr) {
        action = 'recr_update';
      }
      else if (recrData.setting.applicationStartDate < this.nowStr && recrData.setting.applicationEndDate < this.nowStr) {
        action = 'recr_submitted';
      }

    }
    return this.commonService.getRecrProgressTagArray(action);
    //console.log('tagArray', this.tagArray);

  }

  getMyRecrs() {
    this.recrService.getMyRecrs().subscribe((recrs)=>{
      this.recrs = recrs;

      for(let i=0; i<recrs.length; i++) {
        //初期値を下限に設定
        if(this.nowStr < recrs[i].setting.recruitmentEndDate) {
          recrs[i].grade1 = recrs[i].grade1!=null?recrs[i].grade1:recrs[i].grade1Lower;
          recrs[i].grade2 = recrs[i].grade2!=null?recrs[i].grade2:recrs[i].grade2Lower;
          recrs[i].grade3 = recrs[i].grade3!=null?recrs[i].grade3:recrs[i].grade3Lower;
          recrs[i].grade4 = recrs[i].grade4!=null?recrs[i].grade4:recrs[i].grade4Lower;
        }

        recrs[i].tagArray = this.getProgressTagArray(recrs[i]);

        //応募者を取得
        this.appService.getAppsByRecrId(recrs[i].id).subscribe(apps=>{
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
          this.recrs[i].apps = tapps;

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
          this.recrs[i].passedApps = passedApps;

          this.updateSelectionValid(this.recrs[i]);

          //追加合格対象者を取得
          let assd = recrs[i].setting.addSelectionStartDate;
          if(assd && this.nowStr > assd) {

            //console.log(this.recrs[i])
            this.appService.getAddAppsByRecrId(recrs[i].id).subscribe(apps=>{
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
              this.recrs[i].addApps = tapps;

              //for print-sel-add
              let addPassedApps = []; //addPassedApps[grade][i];
              for(let i=1; i<=4; i++) {
                addPassedApps[i] = [];
                for(let j=0; j<apps.length; j++) {
                  if(apps[j].passed && apps[j].owner.grade==i) {
                    addPassedApps[i].push(apps[j]);
                  }
                }
              }
              this.recrs[i].addPassedApps = addPassedApps;              

              this.updateAddSelectionValid(this.recrs[i]);
            });
          }

          //保存済みか判定
          recrs[i].saved=recrs[i].grade1!=null&&recrs[i].grade2!=null&&recrs[i].grade3!=null&&recrs[i].grade4!=null;
        });

      }


    }
    ,(err)=>{
      console.log(err);
      alert(err.json().message);
    });
  }

  putRecr(recr){
    this.sending = true;
    // 更新POST
    this.recrService.putRecr(recr).subscribe((res)=>{
      // this.setRecr(recrData);
      this.err = null;
      recr.saved=true;
      alert('募集情報を更新しました。');
      this.sending = false;
    },(err)=>{
      this.err = err;
      console.log(this.err);
      alert(this.err);
      this.sending = false;
    });
  }

  confirmSelection(recr) {
    this.sending = true;
    this.recrService.confirmSelection(recr, recr.apps, false).subscribe((res)=>{
      this.err = null;
      //recr.confirmed=true;
      this.getMyRecrs();
      alert('合否を確定しました。');
      this.sending = false;
    },(err)=>{
      this.err = err;
      console.log(this.err);
      alert(this.err);
      this.sending = false;
    });
  }

  confirmAddSelection(recr) {
    this.sending = true;
    this.recrService.confirmSelection(recr, recr.addApps, true).subscribe((res)=>{
      this.err = null;
      //recr.confirmed=true;
      this.getMyRecrs();
      alert('合否を確定しました。');
      this.sending = false;
    },(err)=>{
      this.err = err;
      console.log(this.err);
      alert(this.err);
      this.sending = false;
    });
  }


  s2ab(s) {
    let buf = new ArrayBuffer(s.length);
    let view = new Uint8Array(buf);
    for (let i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
  }

  downloadApps(apps) {
    let header = ['学生証番号','姓','名','姓（カナ）','名（カナ）','性別', '学科', '学年', 'メールアドレス', 'メールアドレス（携帯）', '電話番号', '携帯番号', '誕生日', '郵便番号', '住所', '部活動、サークル等', '趣味', '特技、資格等', '出身高校' , 'facebook、ブログ等のURL', '自己PR'];

    let dataJson = [];
    dataJson.push(header);

    for(let i=0; i<apps.length; i++) {
      let owner = apps[i].owner;
      let a = [];
      a.push(owner.uid);
      a.push(owner.familyName);
      a.push(owner.givenName);
      a.push(owner.kanaFamilyName);
      a.push(owner.kanaGivenName);
      a.push(owner.gender);
      a.push(owner.department);
      //a.push(owner.faculty);
      a.push(owner.grade);
      a.push(owner.email);
      a.push(owner.mobileEmail);
      a.push(owner.phoneNumber);
      a.push(owner.mobileNumber);
      a.push(owner.birthday);
      a.push(owner.postalcode);
      a.push(owner.address);
      a.push(owner.club);
      a.push(owner.hobby);
      a.push(owner.qualification);
      a.push(owner.highschool);
      a.push(owner.url);
      //a.push(apps[i].pr);
      a.push(apps[i].pr.replace(/\n/g, '\r\n')); //excel形式での改行に対応
      dataJson.push(a);
    }

    this.save('応募者情報', dataJson);
  }

  save(fileName, data1) {
    /* generate worksheet */
    let ws: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(data1);
    /* generate workbook and add the worksheet */
    let wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1'); //Sheet1

    /* save to file */
    const wbout: string = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
    FileSaver.saveAs(new Blob([this.s2ab(wbout)]), fileName+'.xlsx');
  }
/*
  downloadApps(apps) {

    let header = ['学生証番号','姓','名','姓（カナ）','名（カナ）','性別', '学科', '学年', 'メールアドレス', 'メールアドレス（携帯）', '電話番号', '携帯番号', '誕生日', '郵便番号', '住所', '部活動、サークル等', '趣味', '特技、資格等', '出身高校' , 'facebook、ブログ等のURL', '自己PR'];

    let dataJson = [];
    dataJson.push(header);

    for(let i=0; i<apps.length; i++) {
      let owner = apps[i].owner;
      let a = [];
      a.push(owner.uid);
      a.push(owner.familyName);
      a.push(owner.givenName);
      a.push(owner.kanaFamilyName);
      a.push(owner.kanaGivenName);
      a.push(owner.gender);
      a.push(owner.department);
      //a.push(owner.faculty);
      a.push(owner.grade);
      a.push(owner.email);
      a.push(owner.mobileEmail);
      a.push(owner.phoneNumber);
      a.push(owner.mobileNumber);
      a.push(owner.birthday);
      a.push(owner.postalcode);
      a.push(owner.address);
      a.push(owner.club);
      a.push(owner.hobby);
      a.push(owner.qualification);
      a.push(owner.highschool);
      a.push(owner.url);
      a.push(apps[i].pr);
      dataJson.push(a);
    }

    let data = this.papa.unparse(dataJson);
    //console.log(data);
    this.downloadFile('応募者情報.csv', data);
  }


  downloadFile(fileName, data: any) {
    let bom = new Uint8Array([0xEF, 0xBB, 0xBF]); //for utf8 dom
    //let parsedResponse = data.text();
    let parsedResponse = data;
    let blob = new Blob([bom, parsedResponse], { type: 'text/csv' });
    let url = window.URL.createObjectURL(blob);

    if(navigator.msSaveOrOpenBlob) {
        navigator.msSaveBlob(blob, fileName);
    } else {
        let a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
    }
    window.URL.revokeObjectURL(url);
  }
  */

  printRecr(recrId) {
    let printContents, popupWin;
      printContents = document.getElementById('print-recr-'+recrId).innerHTML;
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

  printSel(recrId) {
    let printContents, popupWin;
      printContents = document.getElementById('print-sel-'+recrId).innerHTML;
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

  printAddSel(recrId) {
    let printContents, popupWin;
      printContents = document.getElementById('print-sel-add-'+recrId).innerHTML;
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

  getRows(str, min) {
    let len = str?str.split('\n').length:1;
    return len>min?len:min;
  }

  changeShowPr(e) {
    //console.log(this.showPr);
    localStorage.setItem('showPr', JSON.stringify(this.showPr));
  }

  openInfoModal(id) {
    const dialogRef = this.modal.alert()
      .size('lg')
      .showClose(true)
      .title('詳細情報')
      .body(document.getElementById('infomodal-'+id).innerHTML)
      .open();
  }

  openPrModal(app) {
    const dialogRef = this.modal.alert()
      .size('lg')
      .showClose(true)
      .title('自己PR '+app.owner.uid+" "+app.owner.familyName+" "+app.owner.givenName)
      .body(`<div class="pre">${app.pr}</div>`) //preクラスはstyles.cssに明記する必要あり
      //.body(document.getElementById('pr-'+app.id).innerHTML)
      .open();
  }

  updateSelectionValid(recr) {
    recr.selectionValid = recr.apps.every(a=>a.passed!=null);
  }

  updateAddSelectionValid(recr) {
    recr.addSelectionValid = recr.addApps.every(a=>a.passed!=null);
  }
}
