import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CmdcltfrsService } from 'src/app/services/cmdcltfrs/cmdcltfrs.service';
import { CommandeClientDto } from 'src/gs-api/src/models/commande-client-dto';
import { LigneCommandeClientDto } from 'src/gs-api/src/models/ligne-commande-client-dto';

@Component({
  selector: 'app-page-cmd-clt-frs',
  templateUrl: './page-cmd-clt-frs.component.html',
  styleUrls: ['./page-cmd-clt-frs.component.scss']
})
export class PageCmdCltFrsComponent implements OnInit {

  origin = '';
  listeCommandes: Array<any> = [];
  mapLignesCommande = new Map();
  mapPrixTotalCommande = new Map();


  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private cmdCltFrsSevice: CmdcltfrsService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.origin = data.origin;
    });
    this.findAllCommandes()
  }

  nouvelleComande(): void {
    if (this.origin === 'client') {
      this.router.navigate(['nouvellecommandeclt']);
    } else if (this.origin === 'fournisseur') {
      this.router.navigate(['nouvellecommandefrs']);
    }
  }

  findAllCommandes(): void {
    if (this.origin === 'client') {
      this.cmdCltFrsSevice.findAllCommandesClient().subscribe(
        commande => {
          this.listeCommandes = commande;
          this.findAllLignesCommande();
        });
    } else if (this.origin === 'fournisseur') {
      this.cmdCltFrsSevice.findAllCommandesFournisseur().subscribe(
        commande => {
          this.listeCommandes = commande;
          this.findAllLignesCommande();
        });
    }
  }

  findAllLignesCommande(): void {
    this.listeCommandes.forEach(cmd => {
     this.findLignesCommande(cmd.id);
    });
  }

  nouvelleCommande(): void {
    if (this.origin === 'client') {
      this.router.navigate(['nouvellecommandeclt']);
    } else if (this.origin === 'fournisseur') {
      this.router.navigate(['nouvellecommandefrs']);
    }
  }

  findLignesCommande(idCommande?: number): void {
    if (this.origin === 'client') {
      this.cmdCltFrsSevice.findAllLigneCommandesClient(idCommande).subscribe(
        list => {
          console.log(list)
          this.mapLignesCommande.set(idCommande, list);
          this.mapPrixTotalCommande.set(idCommande, this.calculerTotalCmd(list));
      });
    } else if (this.origin === 'fournisseur') {
      this.cmdCltFrsSevice.findAllLigneCommandesFournisseur(idCommande).subscribe(
        list => {
          console.log(list)
          this.mapLignesCommande.set(idCommande, list);
          this.mapPrixTotalCommande.set(idCommande, this.calculerTotalCmd(list));
      });
    }
  }

  calculerTotalCmd(list: Array<LigneCommandeClientDto>): number {
    let total = 0;
    list.forEach(ligne => {
      if (ligne.prixUnitaire && ligne.quantite) {
        total += +ligne.quantite * +ligne.prixUnitaire;
      }
    });
    return Math.floor(total);
  }

  calculerTotalCommande(id?: number): number {
    return this.mapPrixTotalCommande.get(id);
  }

}
