/* tslint:disable */
import { VenteDto } from './vente-dto';
import { ArticleDto } from './article-dto';
export interface LigneVenteDto {
  id?: number;
  vente?: VenteDto;
  article?: ArticleDto;
  quantite?: number;
  prixUnitaire?: number;
  idEntreprise?: number;
}
