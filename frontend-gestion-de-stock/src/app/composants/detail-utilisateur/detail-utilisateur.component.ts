import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';
import { UtilisateurDto } from 'src/gs-api/src/models/utilisateur-dto';

@Component({
  selector: 'app-detail-utilisateur',
  templateUrl: './detail-utilisateur.component.html',
  styleUrls: ['./detail-utilisateur.component.scss']
})
export class DetailUtilisateurComponent implements OnInit {

  @Input()
  utilisateurDto: UtilisateurDto = {};

  @Output()
  suppressionResult = new EventEmitter();
  
  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
  }

  modifierUtilisateur(idUtilisateur?: number): void {
      console.log(idUtilisateur);
      this.router.navigate(['nouvelutilisateur', idUtilisateur]);
  }

  confirmerEtSupprimerUtilisateur(): void {
    if (this.utilisateurDto.id) {
      this.userService.deleteUtilisateur(this.utilisateurDto.id).subscribe(
        res => {
          this.suppressionResult.emit('success');
        },
        error =>{
          this.suppressionResult.emit(error.error.error);
        }
      );
    }
  }

}
