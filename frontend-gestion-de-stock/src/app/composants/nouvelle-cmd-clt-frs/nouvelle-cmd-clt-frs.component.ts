import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from 'src/app/services/article/article.service';
import { CltfrsService } from 'src/app/services/cltfrs/cltfrs.service';
import { CmdcltfrsService } from 'src/app/services/cmdcltfrs/cmdcltfrs.service';
import { ArticleDto } from 'src/gs-api/src/models/article-dto';
import { CommandeClientDto } from 'src/gs-api/src/models/commande-client-dto';
import { CommandeFournisseurDto } from 'src/gs-api/src/models/commande-fournisseur-dto';
import { LigneCommandeClientDto } from 'src/gs-api/src/models/ligne-commande-client-dto';

@Component({
  selector: 'app-nouvelle-cmd-clt-frs',
  templateUrl: './nouvelle-cmd-clt-frs.component.html',
  styleUrls: ['./nouvelle-cmd-clt-frs.component.scss']
})
export class NouvelleCmdCltFrsComponent implements OnInit {

  origin = '';
  selectedClientFournisseur: any = {};
  listClientsFournisseurs: Array<any> = [];
  searchedArticle: ArticleDto = {};
  listArticle: Array<ArticleDto> = [];
  codeArticle = '';
  quantite ='';
  codeCommande ='';
  totalCommande = 0;
  articleNotYetSelected = false;
  lignesCommande: Array<any> = [];
  errorMsg: Array<string> = [];

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private cltFrsService: CltfrsService,
    private articleService: ArticleService,
    private cmdCltFrsService: CmdcltfrsService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.origin = data.origin;
    });
    this.findAllClientsFournisseurs();
    this.findAllArticles();
  }

  cancel(): void {
    if (this.origin === 'client') {
      this.router.navigate(['commandesclient']);
    }
    else if (this.origin === 'fournisseur') {
      this.router.navigate(['commandesfournisseur']);
    }  
  }

  findAllClientsFournisseurs(): void {
    if (this.origin === 'client') {
      this.cltFrsService.findAllClients().subscribe(
      clients => {
        this.listClientsFournisseurs = clients;
      });
    } else if (this.origin === 'fournisseur' ) {
      this.cltFrsService.findAllFournisseurs().subscribe(
      fournisseurs => {
        this.listClientsFournisseurs = fournisseurs;
      });
    }
  }

  findAllArticles(): void {
    this.articleService.findAllArticles().subscribe(
      articles => {
        this.listArticle = articles;
      });
  }

  filtrerArticle(): void {
    if (this.codeArticle.length === 0) {
      this.findAllArticles();
    }
    this.listArticle = this.listArticle.filter( 
      article => article.codeArticle?.includes(this.codeArticle) || article.designation?.includes(this.codeArticle));
  }

  ajouterLigneCommande(): void {
    this.checkLigneCommande()
    this.calculerTotalCommande()

    this.searchedArticle = {};
    this.quantite = '';
    this.codeArticle = '';
    this.articleNotYetSelected = false;
    this.findAllArticles();
  }

  checkLigneCommande(): void {
    const ligneCmdAlreadyExists = this.lignesCommande.find(ligne => ligne.article?.codeArticle === this.searchedArticle.codeArticle)
    if (ligneCmdAlreadyExists) {
      this.lignesCommande.forEach(ligne => {
        if ( ligne && ligne.article?.codeArticle === this.searchedArticle.codeArticle) {
          // @ts-ignore
          ligne.quantite = +ligne.quantite + +this.quantite;
        }
      });
      this.quantite = ligneCmdAlreadyExists.quantite + this.quantite;
    } else {
      const ligneCmd: LigneCommandeClientDto = {
        article: this.searchedArticle,
        prixUnitaire: this.searchedArticle.prixUnitaireTtc,
        quantite: +this.quantite,
      };
      this.lignesCommande.push(ligneCmd);
    }
  }

  calculerTotalCommande(): void {
    this.totalCommande = 0;
    this.lignesCommande.forEach(ligne => {
      if ( ligne.prixUnitaire && ligne.quantite) {
        this.totalCommande += +ligne.prixUnitaire * +ligne.quantite;
      }
    });
  }

  selectArticleClick(article: ArticleDto): void {
    this.searchedArticle = article;
    this.codeArticle = article.codeArticle ? article.codeArticle: '';
    this.articleNotYetSelected = true;
  }

  enrgistrerCommande(): void {
    const commande = this.preparerCommande();
    if (this.origin === 'client') {
      this.cmdCltFrsService.enregistrerCommandeClient(commande as CommandeClientDto).subscribe(
      cmd => {
        this.router.navigate(['commandesclient']);
      }, error => {
        this.errorMsg = error.error.errors;
      });
    } else if (this.origin === 'fournisseur'){
      this.cmdCltFrsService.enregistrerCommandeFournisseur(commande as CommandeFournisseurDto).subscribe(
        cmd => {
          this.router.navigate(['commandesfournisseur']);
        }, error => {
          this.errorMsg = error.error.errors;
        });
    }
  }

  private preparerCommande(): any {
    if (this.origin === 'client') {
      return {
        client: this.selectedClientFournisseur,
        code: this.codeCommande,
        dateCommande: new Date().getTime(),
        etatCommande: 'EN_PREPARATION',
        ligneCommandeClients: this.lignesCommande
      };
    } else if (this.origin === 'fournisseur') {
      return {
        fournisseur: this.selectedClientFournisseur,
        code: this.codeCommande,
        dateCommande: new Date().getTime(),
        etatCommande: 'EN_PREPARATION',
        ligneCommandeFournisseurs: this.lignesCommande
      };
    }
  }

}
