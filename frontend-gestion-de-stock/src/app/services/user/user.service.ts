import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthenticationRequest } from 'src/gs-api/src/models/authentication-request';
import { AuthenticationResponse } from 'src/gs-api/src/models/authentication-response';
import { ChangerMotDePasseUtilisateurDto } from 'src/gs-api/src/models/changer-mot-de-passe-utilisateur-dto';
import { UtilisateurDto } from 'src/gs-api/src/models/utilisateur-dto';
import { AuthenticationService, UtilisateursService } from 'src/gs-api/src/services';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private authenticationService: AuthenticationService,
    private utilisateurService: UtilisateursService,
    private router: Router
  ) { }

  login(authenticationRequest: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.authenticationService.authenticate(authenticationRequest);
  }

  getUserByMail(email?: string): Observable<UtilisateurDto> {
    if (email !== undefined){
      return this.utilisateurService.findByMail(email);
    }
    return of(); 
  }

  setAccessToken(authenticationResponse: AuthenticationResponse): void {
    localStorage.setItem('accessToken', JSON.stringify(authenticationResponse));
  }

  setConnectedUser(utilisateur: UtilisateurDto): void {
    localStorage.setItem('connectedUser', JSON.stringify(utilisateur));
  }

  
  getConnectedUser(): UtilisateurDto {
    if (localStorage.getItem('connectedUser')){
        return JSON.parse(localStorage.getItem('connectedUser') as string);
    }
    return {};
  }

  changerMotDePasse(changerMotDePasseDto: ChangerMotDePasseUtilisateurDto): Observable<ChangerMotDePasseUtilisateurDto> {
    return this.utilisateurService.changerMotDePasse(changerMotDePasseDto);
  }


  isUserLoggedAndAccessTokenValid(): boolean{
    if (localStorage.getItem('accessToken')) {
        //verifiy acces token validity
      return true;
    }
    this.router.navigate(['login']);
    return false;
  }

  enregistrerUtilisateur(utilisateurDto: UtilisateurDto): Observable<UtilisateurDto> {
    return this.utilisateurService.save(utilisateurDto);
  }

  findAllUtilisateurs(): Observable<UtilisateurDto[]> {
    return this.utilisateurService.findAll();
  }

  deleteUtilisateur(idUtilisateur?: number): Observable<any>{
    if (idUtilisateur) {
      return this.utilisateurService.delete(idUtilisateur);
    }
    return of();
  }

  findUtilisateurById(idUtilisateur: number): Observable<UtilisateurDto> {
    return this.utilisateurService.findById(idUtilisateur); 
  }
}
