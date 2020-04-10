import { Injectable } from '@angular/core';

@Injectable()
export class CommonService {

  private progressTagArray: string[];

  constructor() { }

  ///
  /// 募集プログレスタグ取得
  ///
  public getRecrProgressTagArray(action): string[]{
    var tagArray: string[];

    switch (action) {
      case 'recr_add':
        tagArray = ['<span class="glyphicon glyphicon-flag text-danger"></span>',
                    '',
                    '',
                    '',
                    ''];
        break;
      case 'recr_update':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '',
                      '',
                      '',
                      ''];
          break;
      case 'recr_submitted':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-flag text-danger"></span>',
                      '',
                      '',
                      ''];
          break;
      case 'recr_sel':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-flag text-danger"></span>',
                      '',
                      ''];
          break;
          /*
      case 'recr_sel':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-flag text-danger"></span>',
                      ''];
          break;

      case 'recr_result_notification':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>'];
          break;
          */
      default:

        break;
    }

    return tagArray;
  }

  ///
  /// 募集プログレスタグ取得
  ///
  public getApplyProgressTagArray(action): string[]{
    var tagArray: string[];

    switch (action) {
      case 'appl_before':
        tagArray = ['',
                    '',
                    '',
                    ''];
        break;
      case 'appl_term':
          tagArray = ['<span class="glyphicon glyphicon-flag text-danger"></span>',
                      '',
                      '',
                      ''];
          break;
      case 'appl_submitted':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-flag text-danger"></span>',
                      '',
                      ''];
          break;
          /*
      case 'appl_changing_submitted':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-flag text-danger"></span>',
                      '',
                      ''];
          break;
          */
      case 'sel_end':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-flag text-danger"></span>',
                      ''];
          break;
      case 'end':
          tagArray = ['<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>',
                      '<span class="glyphicon glyphicon-ok text-success"></span>'];
          break;
      default:

        break;
    }

    return tagArray;
  }

  public formatDate(date, format) {
      if (!format) format = 'YYYY-MM-DD hh:mm:ss.SSS';
      format = format.replace(/YYYY/g, date.getFullYear());
      format = format.replace(/MM/g, ('0' + (date.getMonth() + 1)).slice(-2));
      format = format.replace(/DD/g, ('0' + date.getDate()).slice(-2));
      format = format.replace(/hh/g, ('0' + date.getHours()).slice(-2));
      format = format.replace(/mm/g, ('0' + date.getMinutes()).slice(-2));
      format = format.replace(/ss/g, ('0' + date.getSeconds()).slice(-2));
      if (format.match(/S/g)) {
        var milliSeconds = ('00' + date.getMilliseconds()).slice(-3);
        var length = format.match(/S/g).length;
        for (var i = 0; i < length; i++) format = format.replace(/S/, milliSeconds.substring(i, i + 1));
      }
      return format;
  }



}
