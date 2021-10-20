import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EntrepriseService } from 'src/app/services/entreprise/entreprise.service';
import { UserService } from 'src/app/services/user/user.service';
import { EntrepriseDto } from 'src/gs-api/src/models/entreprise-dto';
import { UtilisateurDto } from 'src/gs-api/src/models/utilisateur-dto';

@Component({
  selector: 'app-page-profil',
  templateUrl: './page-profil.component.html',
  styleUrls: ['./page-profil.component.scss']
})
export class PageProfilComponent implements OnInit {

  connectedUser: UtilisateurDto = {};
  entrepriseDto: EntrepriseDto = {};
  
  constructor(
    private router: Router,
    private userService: UserService,
    private entrepriseService: EntrepriseService
  ) { }

  ngOnInit(): void {
    this.connectedUser = this.userService.getConnectedUser();
    if (this.connectedUser.entreprise?.id) {
      this.entrepriseService.findById(this.connectedUser.entreprise?.id).subscribe(
        entreprise =>{
          this.entrepriseDto = entreprise;
        }
      )
    }
  }

  modifierMotDePasse(): void{
    this.router.navigate(['changermotdepasse']);
  }

  modifierProfile(): void {
    this.router.navigate(['nouvelutilisateur', this.connectedUser.id]);
  }

}
