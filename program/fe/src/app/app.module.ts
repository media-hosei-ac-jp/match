import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
//import { Ng2BootstrapModule } from 'ng2-bootstrap/ng2-bootstrap';
import { NgUploaderModule } from 'ngx-uploader';
import { PapaParseModule } from 'ngx-papaparse';

import { PopoverModule } from 'ngx-popover';

// RECOMMENDED (doesn't work with system.js)
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { DateValueAccessorModule } from 'angular-date-value-accessor';
import { FileUploadModule } from 'ng2-file-upload';

import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import {environment} from '../environments/environment';

import { AppComponent } from './app.component';

import { UserService } from './user.service';
import { SettingService } from './setting.service';
import { SeminarService } from './seminar.service';
import { LoginService } from './login.service';
import { AppService } from './app.service';
import { NoticeService } from './notice.service';
import { UploadService } from './upload.service';
import { CommonService } from './common.service';
import { RecrService } from './recr.service';
import { CodeService } from './code.service';
import { AdminService } from './admin.service';


import { LoginComponent } from './login/login.component';
import { SeminarComponent } from './seminar/seminar.component';
import { UserComponent } from './user/user.component';
import { HomeComponent } from './home/home.component';
import { NoticeComponent } from './notice/notice.component';
import { SettingComponent } from './setting/setting.component';
import { UserImportComponent } from './user-import/user-import.component';
import { ApplyComponent } from './apply/apply.component';
import { RecrComponent } from './recr/recr.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';
import { InfoComponent } from './info/info.component';
import { DateParsePipe } from './date-parse.pipe';
import { MyDatePickerModule } from 'mydatepicker';
import { AdminSemComponent } from './admin-sem/admin-sem.component';
import { AdminStatsListComponent } from './admin-stats-list/admin-stats-list.component';
import { AdminStatsComponent } from './admin-stats/admin-stats.component';
import { AdminRecrComponent } from './admin-recr/admin-recr.component';
import { SettingRecrsComponent } from './setting-recrs/setting-recrs.component';
import { AdminAppComponent } from './admin-app/admin-app.component';

import { ModalModule } from 'ngx-modialog';
import { BootstrapModalModule } from 'ngx-modialog/plugins/bootstrap';

export const appRoutes: Routes = [
  { path: 'login', component: LoginComponent, data: {title: 'ログイン'} },
  { path: 'apply', component: ApplyComponent},
  { path: 'recr', component: RecrComponent},
  { path: 'user', component: UserComponent},
  { path: 'seminar', component: SeminarComponent},
  { path: 'home', component: HomeComponent},
  { path: 'notice', component: NoticeComponent},
  { path: 'setting', component: SettingComponent},
  { path: 'setting-recrs/:id', component: SettingRecrsComponent},
  { path: 'user-import', component: UserImportComponent},
  { path: 'admin-login', component: AdminLoginComponent },
  { path: 'info', component: InfoComponent },
  { path: 'admin-stats-list', component: AdminStatsListComponent },
  { path: 'admin-stats/:id', component: AdminStatsComponent },
  { path: 'admin-recr/:id', component: AdminRecrComponent },
  { path: 'admin-app/:id', component: AdminAppComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

export const BE_URL = environment.beUrl;
export const API_URL = environment.beUrl+'api/';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SeminarComponent,
    UserComponent,
    HomeComponent,
    NoticeComponent,
    SettingComponent,
    UserImportComponent,
    ApplyComponent,
    RecrComponent,
    AdminLoginComponent,
    InfoComponent,
    DateParsePipe,
    AdminSemComponent,
    AdminStatsListComponent,
    AdminStatsComponent,
    AdminRecrComponent,
    SettingRecrsComponent,
    AdminAppComponent
  ],

  imports: [
    BsDropdownModule.forRoot(),
    BrowserModule,
    FormsModule,
    HttpModule,
    //Ng2BootstrapModule,
    DateValueAccessorModule,
    NgUploaderModule,
    PapaParseModule,
    PopoverModule,
    RouterModule.forRoot(appRoutes, {useHash: true}),
    MyDatePickerModule,
    ModalModule.forRoot(),
    BootstrapModalModule
  ],
  providers: [
    UserService,
    SettingService,
    LoginService,
    AppService,
    NoticeService,
    SeminarService,
    UploadService,
    CommonService,
    RecrService,
    CodeService,
    AdminService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
