import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { CommandeClientDto } from 'src/gs-api/src/models/commande-client-dto';
import { CommandeFournisseurDto } from 'src/gs-api/src/models/commande-fournisseur-dto';
import { LigneCommandeClientDto } from 'src/gs-api/src/models/ligne-commande-client-dto';
import { LigneCommandeFournisseurDto } from 'src/gs-api/src/models/ligne-commande-fournisseur-dto';
import { CommandesclientsService } from 'src/gs-api/src/services/commandesclients.service';
import { CommandesfournisseursService } from 'src/gs-api/src/services/commandesfournisseurs.service';
import { UserService } from '../user/user.service';

@Injectable({
  providedIn: 'root'
})
export class CmdcltfrsService {

  constructor(
    private commandeClientService: CommandesclientsService,
    private commandeFournisseurService: CommandesfournisseursService,
    private userService: UserService
  ) { }

  enregistrerCommandeClient(commandeClient: CommandeClientDto): Observable<CommandeClientDto> {
    commandeClient.idEntreprise = this.userService.getConnectedUser().entreprise?.id;
    return this.commandeClientService.save(commandeClient);
  }

  enregistrerCommandeFournisseur(commandeFournisseurDto: CommandeFournisseurDto): Observable<CommandeFournisseurDto> {
    commandeFournisseurDto.idEntreprise = this.userService.getConnectedUser().entreprise?.id;
    return this.commandeFournisseurService.save(commandeFournisseurDto);
  }

  findAllCommandesClient(): Observable<CommandeClientDto[]> {
    return this.commandeClientService.findAll();
  }

  findAllCommandesFournisseur(): Observable<CommandeFournisseurDto[]> {
    return this.commandeFournisseurService.findAll();
  }

  findAllLigneCommandesClient(idCmd?: number): Observable<LigneCommandeClientDto[]> {
    if (idCmd) {
      return this.commandeClientService.findAllLignesCommandesClientByCommandeClientId(idCmd);
    }
    return of();
  }

  findAllLigneCommandesFournisseur(idCmd?: number): Observable<LigneCommandeFournisseurDto[]> {
    if (idCmd) {
      return this.commandeFournisseurService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCmd);
    }
    return of();
  }
}
