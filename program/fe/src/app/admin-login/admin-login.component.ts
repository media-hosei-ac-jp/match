import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin.service';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-login',
  templateUrl: './admin-login.component.html',
  styleUrls: ['./admin-login.component.css']
})
export class AdminLoginComponent implements OnInit {
  uid;
  err;
  constructor(private adminService: AdminService,
              private loginService: LoginService,
              private router: Router) { }

  ngOnInit() {
  }

  login() {
    console.log(this.uid);
    this.adminService.login(this.uid).subscribe(res=>{
      if(res.successful) {
        //location.reload();
        this.router.navigate(['/']);
      }
      else {
        this.err=true;
      }
    }, err=>{
      this.err = err;
    });
  }

}
