import { Component, OnInit, AfterViewChecked } from '@angular/core';
import { RecrService } from '../recr.service';
import { SettingService } from '../setting.service';
import { API_URL } from '../app.module';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
//export class InfoComponent implements OnInit, AfterViewChecked {
export class InfoComponent implements OnInit {
  fileApiUrl = API_URL+'file/download/';
  stats;
//  private fileGenerated = false;
  infoLength = 100; //選考方法のデフォルト表示

  constructor(private recrService: RecrService) { }

  ngOnInit() {
    this.getStats();
  }

  getStats() {
    this.recrService.getAllStats().subscribe(stats=>{
      this.stats = stats.filter((element, index, array)=>{
        let recr = element.recr;
        return !((recr.grade1==null||recr.grade1==0)&&
                 (recr.grade2==null||recr.grade2==0)&&
                 (recr.grade3==null||recr.grade3==0)&&
                 (recr.grade4==null||recr.grade4==0));
      });
    });
  }
}
