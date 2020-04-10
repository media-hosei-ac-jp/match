import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import { Router } from '@angular/router';
declare var jquery:any;
declare var $: any;

@Injectable()
export class UploadService {

  constructor() { }


  // csvをJSON②変換
  public csvJSON(csv):any {

    var reviveDate = function(value) {
      if (value == null ||
          value.constructor !== String ||
          value.search(/^\d{4}-\d{2}-\d{2}/g) === -1)
          return value;

      var datetime = value;
      if (value.length <= 10){
        datetime = value + ' 00:00';
      }
      return datetime;
    }


    var result = [];

    var headers = csv[0];
    console.log('headers', headers);
    console.log('Data', csv[1][0]);

    for (var i = 1; i < csv.length; i++) {

        var obj = {};

        console.log('csv[i]', csv[i]);

        if (csv[i].length == csv[0].length) {
          for (var j = 0; j < headers.length; j++) {
              obj[headers[j]] = reviveDate(csv[i][j]);

              //console.log('obj[headers[j]]', headers[j]);
              //console.log('csv[i][j]', csv[i][j]);
          }
          result.push(obj);
        }


    }
    // return JSON.stringify(result); //JSON
    return result; //JSON
  }


/*
  public setUploadFile(uploadFileData, funcId, fileName, outId): any {

    console.log('arg uploadFileData', uploadFileData);

    if ($('#' + outId).val() == '') {
      return null;
    }

    let fileData = {
        uploadFile: uploadFileData.uploadFile
      , uploadConfig: uploadFileData.uploadConfig
    }

    if (uploadFileData.uploadFile.functionClass == null) {
       // FileData設定
       fileData.uploadFile.functionClass = fileData.uploadConfig.functionClass;
       fileData.uploadFile.functionId = funcId;
    }

    fileData.uploadFile.fileName = fileName
    fileData.uploadFile.file = $('#' + outId).val();

    console.log('fileData', fileData);

    return fileData;
  }
  */
  public setUploadFile(id, fileName, outId): any {

    if ($('#' + outId).val() == '') {
      return null;
    }

    let fileData = {
                    id: id,
                    fileName: fileName,
                    content: $('#' + outId).val()
                  };

    console.log('fileData', fileData);

    return fileData;
  }


  public fileChange(event, outId, fileName, downloadId) {
    let fileList: FileList = event.target.files;
    if(!fileList.length) return;

    var file=fileList[0];

    var reader:any;
    var target:EventTarget;
    var base64:any;

    reader= new FileReader();
    reader.onload = function (imgsrc){
        var fileUrl = imgsrc.target.result;
        base64 = btoa(fileUrl);


        $(outId).val(base64);

        //console.log('this.hdnFile');
        //console.log(this.hdnFile);

        var objectURL = window.URL.createObjectURL(toBlob(base64, 'application/pdf'));
        // console.log(objectURL);

        var link: any;
        link = document.getElementById(downloadId);
        // var href: HTMLElement;
        link.style = "display: inline";
        link.href = objectURL;
        /* link.download = $('#title').val() + '_紹介.pdf';
        this.fileName = $('#title').val() + '_紹介.pdf'; */
        link.download = fileName;
        this.fileName = fileName;
        // console.log(base64);
    }


    // binaryからblobへ
    var toBlob = function(base64, mime_ctype) {
      // 日本語の文字化けに対処するためBOMを作成する。
       var bom = new Uint8Array([0xEF, 0xBB, 0xBF]);

       var bin = atob(base64.replace(/^.*,/, ''));
       var buffer = new Uint8Array(bin.length);
       for (var i = 0; i < bin.length; i++) {
           buffer[i] = bin.charCodeAt(i);
       }
       // Blobを作成
       try {
           var blob = new Blob([bom, buffer.buffer], {
               type: mime_ctype,
           });
       } catch (e) {
           return false;
       }
       return blob;
    }

    reader.readAsBinaryString(file);

    //console.log('base64');
    //console.log(base64);
    //this.hdnFile = base64;
  }


  // binaryからblobへ
  public toBlob(base64, mime_ctype) {
    // 日本語の文字化けに対処するためBOMを作成する。
     var bom = new Uint8Array([0xEF, 0xBB, 0xBF]);

     var bin = atob(base64.replace(/^.*,/, ''));
     var buffer = new Uint8Array(bin.length);
     for (var i = 0; i < bin.length; i++) {
         buffer[i] = bin.charCodeAt(i);
     }
     // Blobを作成
     try {
         var blob = new Blob([bom, buffer.buffer], {
             type: mime_ctype,
         });
     } catch (e) {
         return false;
     }
     return blob;
  }


  public initFileSet(uploadFile, downloadId) {

    if (uploadFile != null && uploadFile.content != null) {
      var base64 = uploadFile.content;
      $('#seminar_file').val(base64);
      var objectURL = window.URL.createObjectURL(this.toBlob(base64, 'application/pdf'));

      var link: any;
      link = document.getElementById(downloadId);
      // var href: HTMLElement;
      link.style = "display: inline";
      link.href = objectURL;
      link.download = uploadFile.fileName;
      // this.fileName = uploadFileData.uploadFile.fileName;
    }

  }



}
