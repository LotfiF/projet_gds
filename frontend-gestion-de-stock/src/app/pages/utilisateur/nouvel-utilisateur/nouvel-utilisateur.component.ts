import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { EntrepriseService } from 'src/app/services/entreprise/entreprise.service';
import { UserService } from 'src/app/services/user/user.service';
import { AdresseDto } from 'src/gs-api/src/models/adresse-dto';
import { EntrepriseDto } from 'src/gs-api/src/models/entreprise-dto';
import { UtilisateurDto } from 'src/gs-api/src/models/utilisateur-dto';
import { PhotosService } from 'src/gs-api/src/services/photos.service';
import SavePhotoParams = PhotosService.SavePhotoParams;

@Component({
  selector: 'app-nouvel-utilisateur',
  templateUrl: './nouvel-utilisateur.component.html',
  styleUrls: ['./nouvel-utilisateur.component.scss']
})
export class NouvelUtilisateurComponent implements OnInit {

  utilisateurDto: UtilisateurDto = {};
  adresseDto: AdresseDto ={};
  entrepriseDto: EntrepriseDto = {};
  errorsMsg: Array<string> = [];
  file: File | null = null;
  imgUrl: string | ArrayBuffer = 'assets/client.png';
  dateDeNaissance: any; 
  
  constructor(
    private router: Router,
    private userService: UserService,
    private photoService: PhotosService,
    private entrepriseService: EntrepriseService,
    private activatedRoute: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    const idUtilisateur = this.activatedRoute.snapshot.params.idUtilisateur;
    console.log(idUtilisateur);
    if (idUtilisateur){
      this.userService.findUtilisateurById(idUtilisateur).subscribe(
        utilisateur =>{
          this.utilisateurDto = utilisateur;
          this.adresseDto = utilisateur.adresse?utilisateur.adresse: {};
          //this.dateDeNaissance = utilisateur.dateDeNaissance;
          if (utilisateur.dateDeNaissance){
            this.dateDeNaissance = new NgbDate(
              utilisateur.dateDeNaissance.getFullYear(),
              utilisateur.dateDeNaissance.getMonth() + 1,
              utilisateur.dateDeNaissance.getDate()
            );
          }
        }
      );
    }
    console.log(this.utilisateurDto);
    const idEntreprise = this.userService.getConnectedUser().entreprise?.id;
    if (idEntreprise) {
      this.entrepriseService.findById(idEntreprise).subscribe(
        entreprise => {
          this.entrepriseDto = entreprise;
        }
      )
    }
  }

  cancel(): void {
    this.router.navigate(['utilisateurs']);
  }

  saveClick(): void {
    this.utilisateurDto.adresse = this.adresseDto;
    this.utilisateurDto.entreprise = this.entrepriseDto;
 /* this.utilisateurDto.dateDeNaissance = this.dateDeNaissance; */
    this.utilisateurDto.dateDeNaissance = new Date(
      this.dateDeNaissance.year,
      this.dateDeNaissance.month,
      this.dateDeNaissance.day
    );  
    console.log(this.utilisateurDto);
    this.userService.enregistrerUtilisateur(this.utilisateurDto).subscribe(
      utilisateur => {
        this.savePhoto(utilisateur.id, utilisateur.nom);
        this.router.navigate(['utilisateurs'])
      },
      error =>{
        this.errorsMsg = error.error.errors;
      }
    )
  }

  onFileInput(files: FileList | null): void {
    if (files) {
      this.file = files.item(0);
      if (this.file) {
        const fileReader = new FileReader();
        fileReader.readAsDataURL(this.file);
        fileReader.onload = (event) => {
          if (fileReader.result) {
            this.imgUrl = fileReader.result;
          }
        };
      }
    }
  }

  savePhoto(idUtilisateur?: number, titre?: string): void {
    if (idUtilisateur && titre && this.file) {
      const params: SavePhotoParams = {
        id: idUtilisateur, 
        file: this.file, 
        title: titre,
        context: "utilisateur"
      };
      this.photoService.savePhoto(params).subscribe(
        res => {
          this.router.navigate(['utilisateurs']);
        }
      );
    } else {
      this.router.navigate(['utilisateurs']);
    }
  }
}
