import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RecrService } from '../recr.service';

@Component({
  selector: 'app-setting-recrs',
  templateUrl: './setting-recrs.component.html',
  styleUrls: ['./setting-recrs.component.css']
})
export class SettingRecrsComponent implements OnInit {
  recrs$;

  constructor(private route: ActivatedRoute,
              private recrService: RecrService) { }

  ngOnInit() {
    let id = this.route.snapshot.params['id'];
    if(id) {
      this.recrs$ = this.recrService.getAllBySettingId4A(id);
    }
  }

}
