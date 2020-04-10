import { Component, OnInit } from '@angular/core';
import { PapaParseService } from 'ngx-papaparse';
import { SettingService } from '../setting.service';
import { RecrService } from '../recr.service';
import * as XLSX from 'xlsx';

// 募集設定一括取込画面コンポーネントクラス
@Component({
  selector: 'app-setting',
  templateUrl: './setting.component.html',
  styleUrls: ['./setting.component.css']
})
export class SettingComponent implements OnInit {
  settings;
  settingData;

  sending = false;
  setSuccessful;
  setErr = null;

  constructor(private papa: PapaParseService,
              private settingService: SettingService,
              private recrService: RecrService) { }

  ngOnInit() {
    this.init();
  }

  init() {
    this.settingService.getSetting().subscribe(res=>{
      this.settings = res;
    });
  }

  // ファイル選択
  settingFileOnChange(event) {
    this.readFile(event, (re: any) => {
      this.settingData = re.target.result;
    });
  }

  fileOnChange(event, index) {
    this.readFile(event, (re: any) => {
      this.settings[index].recrData = re.target.result;
    });
  }

  updateSettingFileOnChange(event, index) {
    this.readFile(event, (re: any) => {
      this.settings[index].settingData = re.target.result;
    });
  }

  readFile(event, callback) {
    let files = event.srcElement.files;
    //console.log(files[0]);
    let reader = new FileReader();
    reader.onload = callback;
    reader.readAsBinaryString(files[0]);
  }

  convertData(attr, a) {
    let data: any = {};
    for(let i=0; i<attr.length; i++) {
      data[attr[i]] = a[i];
    }
    return data;
  }


  addSetting() {
    this.sending = true;

    /* read workbook */
    const bstr: string = this.settingData;
    //console.log(bstr);
    const wb: XLSX.WorkBook = XLSX.read(bstr, {type: 'binary'});

    /* grab first sheet */
    const wsname: string = wb.SheetNames[0];
    const ws: XLSX.WorkSheet = wb.Sheets[wsname];

    /* save data */
    //let data = (XLSX.utils.sheet_to_json(ws, {header: 1})); //header: 1だと配列として扱う
    const header = ['title','recruitmentStartDate','recruitmentEndDate','applicationStartDate','dataRefreshDate','applicationEndDate','selectionStartDate','selectionEndDate','registrationStartDate','registrationEndDate','addSelectionStartDate','addSelectionEndDate','addRegistrationStartDate','addRegistrationEndDate','displayType','maximum'];
    let data = (XLSX.utils.sheet_to_json(ws, {header: header, range: 1})); //range:1 は1行目からデータを使う（headerは無視）
    console.log(data);
    this.settingService.saveSetting(data[0]).subscribe(res=>{
      this.setErr = null;
      this.setSuccessful = true;
      this.sending = false;
      this.init();
    },err=>{
      this.setSuccessful = null;
      this.setErr = err;
      this.sending = false;
    });

  }

  updateSetting(setting) {
    this.sending = true;

    /* read workbook */
    const bstr: string = setting.settingData;
    //console.log(bstr);
    const wb: XLSX.WorkBook = XLSX.read(bstr, {type: 'binary'});

    /* grab first sheet */
    const wsname: string = wb.SheetNames[0];
    const ws: XLSX.WorkSheet = wb.Sheets[wsname];

    /* save data */
    //let data = (XLSX.utils.sheet_to_json(ws, {header: 1})); //header: 1だと配列として扱う
    const header = ['title','recruitmentStartDate','recruitmentEndDate','applicationStartDate','dataRefreshDate','applicationEndDate','selectionStartDate','selectionEndDate','registrationStartDate','registrationEndDate','addSelectionStartDate','addSelectionEndDate','addRegistrationStartDate','addRegistrationEndDate','displayType','maximum'];
    let data = (XLSX.utils.sheet_to_json(ws, {header: header, range: 1})); //range:1 は1行目からデータを使う（headerは無視）
    //console.log(data);
    data[0]['id']=setting.id;
    this.settingService.saveSetting(data[0]).subscribe(res=>{
      setting.upErr = null;
      setting.upSuccessful = true;
      this.sending = false;
      this.init();
    },err=>{
      setting.upSuccessful = null;
      setting.upErr = err;
      this.sending = false;
    });
  }

  addRecrs(setting) {
    this.sending = true;

    /* read workbook */
    const bstr: string = setting.recrData;
    //console.log(bstr);
    const wb: XLSX.WorkBook = XLSX.read(bstr, {type: 'binary'});

    /* grab first sheet */
    const wsname: string = wb.SheetNames[0];
    const ws: XLSX.WorkSheet = wb.Sheets[wsname];

    /* save data */
    //let data = (XLSX.utils.sheet_to_json(ws, {header: 1})); //header: 1だと配列として扱う
    const header = ['uid','familyName','givenName','grade1Lower','grade1Upper','grade2Lower','grade2Upper','grade3Lower','grade3Upper','grade4Lower','grade4Upper'];
    let data = (XLSX.utils.sheet_to_json(ws, {header: header, range: 1})); //range:1 は1行目からデータを使う（headerは無視）
    //console.log(data);
    data.forEach(d=>{d['settingId']=setting.id;});

    this.recrService.updateLimits(data).subscribe(res=>{
      setting.err = null;
      setting.successful = true;
      this.sending = false;
      //this.init();
    },err=>{
      setting.successful = null;
      setting.err = err;
      this.sending = false;
    });

  }


}
