import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ArticleDto } from 'src/gs-api/src/models';
import { ArticlesService } from 'src/gs-api/src/services';
import { UserService } from '../user/user.service';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  constructor(
    private userService: UserService,
    private articleService: ArticlesService
  ) { }

  enregistrerArticle(articleDto: ArticleDto): Observable<ArticleDto> {
    articleDto.idEntreprise = this.userService.getConnectedUser().entreprise?.id;
    return this.articleService.save(articleDto);
  }

  findAllArticles(): Observable<ArticleDto[]> {
    return this.articleService.findAll();
  }

  findArticleById(idArticle?: number): Observable<ArticleDto> {
    console.log(idArticle);
    if (idArticle) {
      return this.articleService.findById(idArticle);
    }
    return of();
  } 

  deleteArticle(idArticle?: number): Observable<any> {
    if (idArticle) {
      return this.articleService.delete(idArticle);
    }
    return of();
  }

  findArticleByCode(codeArticle: string): Observable<ArticleDto>  {
    return this.articleService.findByCodeArticle(codeArticle);
  }
}
