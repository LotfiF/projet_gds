import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleService } from 'src/app/services/article/article.service';
import { ArticleDto } from 'src/gs-api/src/models/article-dto';

@Component({
  selector: 'app-page-article',
  templateUrl: './page-article.component.html',
  styleUrls: ['./page-article.component.scss']
})
export class PageArticleComponent implements OnInit {

  ​listArticles: Array<ArticleDto> = [];
  errorMsg = '';

  constructor(
    ​private router: Router,
    private articleService: ArticleService
  ) { }

  ngOnInit(): void {
    ​this.findListArticle();
  }

  ​findListArticle(): void {
    this.articleService.findAllArticles().subscribe(
      articles => {
        this.listArticles = articles;
      }
    );
  }

  ​nouvelArticle(): void {
    this.router.navigate(['nouvelarticle']);
  }

  handleSuppression(event: any): void {
    if(event === 'success') {
      this.findListArticle();
    } else {
      this.errorMsg = event;
    }
  }


}
