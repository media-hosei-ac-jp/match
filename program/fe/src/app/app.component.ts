import { Component } from '@angular/core';
import { NavigationEnd } from '@angular/router';

import { UserService } from './user.service';
import { LoginService } from './login.service';

import { BE_URL } from './app.module';

import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Inject } from '@angular/core';
import { Location } from '@angular/common'
import { LocationStrategy } from '@angular/common'
import { Title } from '@angular/platform-browser'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [UserService,
              LoginService]
})
export class AppComponent {


  login: any = {userId: null, roles: null};
  auth : boolean = false;
  beUrl : string;
  googleUrl : string = 'https://accounts.google.com/AddSession?hl=ja&continue=https://mail.google.com/mail&service=mail';
  currentPath;
  demoStr;
  msg: boolean = false;
  isAdmin: boolean = false;
  isTeacher: boolean = false;
  isStudent: boolean = false;

  constructor(  private  _userService: UserService,
                private _loginService: LoginService,
                private _location: Location,
                private router: Router,
                private route: ActivatedRoute) {

    //console.log('app constructor');
  　this.beUrl = BE_URL;
    // this.isAdmin = this._loginService.isAdmin();

    // this.currentPath = this.router.url;
    // this.currentPath = this._location.path();

    router.events.subscribe(()=>{this.getUser()});

    //router.events.subscribe(( path )=>{
      //this.setSelectedId(path);
    //});
  }

  ngOnInit() {
    //console.log('ngOnInit');

    // this.currentPath = this.route.snapshot.paramMap.get('id');

    this.getUser();

    // this.isAdmin = this._loginService.isAdmin();
    // this.demoStr = this._demoService.getMessage();
  }
  getUser() {
    this._userService.me().subscribe(login => {
      //console.log('getUser');
      //console.log(login);
      this._loginService.setUser(login);
      this.auth = login.id != null;
      this.login = login;
      this.isAdmin = this._loginService.isAdmin();
      this.isStudent = this._loginService.isStudent();
      this.isTeacher = this._loginService.isTeacher();

      // console.log(login);
      this.getCurrentPath();
    });
  }


  getCurrentPath() {
      //console.log('getCurrentPath');
      // this.currentPath = path;
      if(this.router.url=="/login") {
        this.router.navigate(['/home']);
      }
  }

  setSelectedId(id) {
    // this.currentPath = id.get('id');
    // return id.get('id');
  }

}
