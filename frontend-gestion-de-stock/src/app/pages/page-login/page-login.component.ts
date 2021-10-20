import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';
import { AuthenticationRequest } from 'src/gs-api/src/models';

@Component({
  selector: 'app-page-login',
  templateUrl: './page-login.component.html',
  styleUrls: ['./page-login.component.scss']
})
export class PageLoginComponent implements OnInit {

  authenticationRequest: AuthenticationRequest = {};
  errorMessage = '';

  constructor(
    private userService : UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  login(): void {
    this.userService.login(this.authenticationRequest).subscribe(
      (data) => {
        this.userService.setAccessToken(data);
        this.getUserByMail();
        this.router.navigate(['']);
      }, 
      error => {
        this.errorMessage = "Login et/ou mot de passe incorrecte";
      });
    }

  getUserByMail(): void {
    this.userService.getUserByMail(this.authenticationRequest.login).subscribe(
      user => {
        this.userService.setConnectedUser(user);
      },
      error =>{
        console.log(error);
      }
      );
  }
}
