import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ClientDto } from 'src/gs-api/src/models/client-dto';
import { FournisseurDto } from 'src/gs-api/src/models/fournisseur-dto';
import { ClientsService, FournisseursService } from 'src/gs-api/src/services';
import { UserService } from '../user/user.service';

@Injectable({
  providedIn: 'root'
})
export class CltfrsService {

  constructor(
    private userService: UserService,
    private clientService: ClientsService,
    private fournisseurService: FournisseursService
  ) { }

  enregistrerClient(clientDto: ClientDto): Observable<ClientDto> {
    clientDto.idEntreprise = this.userService.getConnectedUser().entreprise?.id;
    return this.clientService.save(clientDto);
  }

  enregistrerFournisseur(fournisseurDto: FournisseurDto): Observable<FournisseurDto> {
    fournisseurDto.idEntreprise = this.userService.getConnectedUser().entreprise?.id;
    return this.fournisseurService.save(fournisseurDto);
  }

  findAllClients(): Observable<ClientDto[]> {
    return this.clientService.findAll();
  }

  findAllFournisseurs(): Observable<FournisseurDto[]> {
    return this.fournisseurService.findAll();
  }

  findClientById(idClient: number): Observable<ClientDto> {
    if (idClient){
      return this.clientService.findById(idClient);
    }
    return of();
  }

  findFournisseurById(idFournisseur: number): Observable<FournisseurDto> {
    if (idFournisseur) {
      return this.fournisseurService.findById(idFournisseur);
    }
    return of();
  }

  deleteClient(idClient: number): Observable<any>{
    if (idClient){
      return this.clientService.delete(idClient);
    }
    return of();
  }

  deleteFournisseur(idFournisseur: number): Observable<any>{
    if (idFournisseur){
      return this.fournisseurService.delete(idFournisseur);
    }
    return of();
  }


}
