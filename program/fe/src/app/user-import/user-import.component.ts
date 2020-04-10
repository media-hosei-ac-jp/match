import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { PapaParseService } from 'ngx-papaparse';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-user-import',
  templateUrl: './user-import.component.html',
  styleUrls: ['./user-import.component.css']
})
export class UserImportComponent implements OnInit {
  studentData;
  stuSuccessful;
  stuErr = null;
  sending = false;

  teacherData;
  teaSuccessful;
  teaErr = null;

  adminData;
  admSuccessful;
  admErr = null;

  constructor(private papa: PapaParseService,
              private userService: UserService) { }

  ngOnInit() {
  }

  fileOnChange(event, varName) {
    this.readFile(event, (re: any) => {
      this[varName] = re.target.result;
    });
  }

  readFile(event, callback) {
    let files = event.srcElement.files;
    //console.log(files[0]);
    let reader = new FileReader();
    reader.onload = callback;
    reader.readAsBinaryString(files[0]);
  }

  addStudents() {
    this.sending = true;
    /* read workbook */
    const bstr: string = this.studentData;
    //console.log(bstr);
    const wb: XLSX.WorkBook = XLSX.read(bstr, {type: 'binary'});

    /* grab first sheet */
    const wsname: string = wb.SheetNames[0];
    const ws: XLSX.WorkSheet = wb.Sheets[wsname];

    /* save data */
    //let data = (XLSX.utils.sheet_to_json(ws, {header: 1})); //header: 1だと配列として扱う
    const header = ['uid', 'familyName', 'givenName', 'kanaFamilyName', 'kanaGivenName', 'email', 'faculty', 'department', 'grade', 'gender'];
    let students = (XLSX.utils.sheet_to_json(ws, {header: header, range: 1})); //range:1 は1行目からデータを使う（headerは無視）
    //console.log(data);
    this.userService.addStudents(students).subscribe(res=>{
        this.stuErr = null;
        this.stuSuccessful = true;
        this.sending = false;
      },err=>{
        this.stuSuccessful = null;
        this.stuErr = err;
        this.sending = false;
      });
  }

  convertUser(attr, a) {
    let user: any = {};
    for(let i=0; i<attr.length; i++) {
      user[attr[i]] = a[i];
    }
    return user;
  }

  addTeachers() {
    this.sending = true;

    /* read workbook */
    const bstr: string = this.teacherData;
    //console.log(bstr);
    const wb: XLSX.WorkBook = XLSX.read(bstr, {type: 'binary'});

    /* grab first sheet */
    const wsname: string = wb.SheetNames[0];
    const ws: XLSX.WorkSheet = wb.Sheets[wsname];

    /* save data */
    //let data = (XLSX.utils.sheet_to_json(ws, {header: 1})); //header: 1だと配列として扱う
    const header = ['uid', 'familyName', 'givenName', 'kanaFamilyName', 'kanaGivenName', 'email', 'faculty', 'department'];
    let users = (XLSX.utils.sheet_to_json(ws, {header: header, range: 1})); //range:1 は1行目からデータを使う（headerは無視）
    //console.log(data);
    this.userService.addTeachers(users).subscribe(res=>{
        this.teaErr = null;
        this.teaSuccessful = true;
        this.sending = false;
      },err=>{
        this.teaSuccessful = null;
        this.teaErr = err;
        this.sending = false;
      });
  }

  addAdmin() {
    this.sending = true;
    /* read workbook */
    const bstr: string = this.adminData;
    //console.log(bstr);
    const wb: XLSX.WorkBook = XLSX.read(bstr, {type: 'binary'});

    /* grab first sheet */
    const wsname: string = wb.SheetNames[0];
    const ws: XLSX.WorkSheet = wb.Sheets[wsname];

    /* save data */
    //let data = (XLSX.utils.sheet_to_json(ws, {header: 1})); //header: 1だと配列として扱う
    const header = ['uid', 'familyName', 'givenName', 'kanaFamilyName', 'kanaGivenName', 'email', 'faculty', 'department'];
    let users = (XLSX.utils.sheet_to_json(ws, {header: header, range: 1})); //range:1 は1行目からデータを使う（headerは無視）
    //console.log(data);
    this.userService.addAdmin(users).subscribe(res=>{
        this.admErr = null;
        this.admSuccessful = true;
        this.sending = false;
      },err=>{
        this.admSuccessful = null;
        this.admErr = err;
        this.sending = false;
      });
  }



}
