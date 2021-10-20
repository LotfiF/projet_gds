import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';
import { UtilisateurDto } from 'src/gs-api/src/models/utilisateur-dto';

@Component({
  selector: 'app-page-utilisateur',
  templateUrl: './page-utilisateur.component.html',
  styleUrls: ['./page-utilisateur.component.scss']
})
export class PageUtilisateurComponent implements OnInit {

  ​listUtilisateurs: Array<UtilisateurDto> = [];
  errorMsg = '';
  origin = 'utilisateur';

  constructor(
    private router: Router,
    private userervice: UserService
  ) { }

  ngOnInit(): void {
    ​this.findListUtilisateurs();
  }

  nouvelUtilisateur(): void {
    this.router.navigate(['nouvelutilisateur'])
  }

  findListUtilisateurs(): void {
    this.userervice.findAllUtilisateurs().subscribe(
      utilisateurs => {
        this.listUtilisateurs = utilisateurs;
      }
    );
  }

  handleSuppression(event: any): void {
    if(event === 'success') {
      this.findListUtilisateurs();
    } else {
      this.errorMsg = event;
    }
  }

}
