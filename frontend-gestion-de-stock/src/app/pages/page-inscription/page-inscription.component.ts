import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EntrepriseService } from 'src/app/services/entreprise/entreprise.service';
import { UserService } from 'src/app/services/user/user.service';
import { AuthenticationRequest } from 'src/gs-api/src/models';
import { AdresseDto } from 'src/gs-api/src/models/adresse-dto';
import { EntrepriseDto } from 'src/gs-api/src/models/entreprise-dto';

@Component({
  selector: 'app-page-inscription',
  templateUrl: './page-inscription.component.html',
  styleUrls: ['./page-inscription.component.scss']
})
export class PageInscriptionComponent implements OnInit {

  entrepriseDto: EntrepriseDto = {};
  adresse: AdresseDto = {};
  errorsMsg: Array<string> = [];

  constructor(
    private entrepriseService: EntrepriseService,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  inscrire(): void{
    this.entrepriseDto.adresse = this.adresse;
    this.entrepriseService.sinscrire(this.entrepriseDto).subscribe( 
      entrepriseDto => {
        this.connectEntreprise();
      },
      error => {
        this.errorsMsg = error.error.errors;
      });
  }

  connectEntreprise(): void {
    const authenticationRequest: AuthenticationRequest = {
      login: this.entrepriseDto.mail,
      password: 'motDePasseAdmin'
    }
    this.userService.login(authenticationRequest).subscribe(
      response => {
        this.userService.setAccessToken(response);
        this.getUserByMail(authenticationRequest.login)
        localStorage.setItem('origin', "inscription");
        this.router.navigate(['changermotdepasse']);
      }
    );
  }

  getUserByMail(email?: string): void {
    this.userService.getUserByMail(email).subscribe(
      user =>{
        this.userService.setConnectedUser(user);
      },
      error =>{
        console.log(error);
      }
    );
  }

}
