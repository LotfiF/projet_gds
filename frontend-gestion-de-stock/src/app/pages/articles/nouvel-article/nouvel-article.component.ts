import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from 'src/app/services/article/article.service';
import { CategoryService } from 'src/app/services/category/category.service';
import { ArticleDto } from 'src/gs-api/src/models/article-dto';
import { CategoryDto } from 'src/gs-api/src/models/category-dto';
import { PhotosService } from 'src/gs-api/src/services';
import SavePhotoParams = PhotosService.SavePhotoParams;

@Component({
  selector: 'app-nouvel-article',
  templateUrl: './nouvel-article.component.html',
  styleUrls: ['./nouvel-article.component.scss']
})
export class NouvelArticleComponent implements OnInit {

  articleDto: ArticleDto = {};
  categorieDto: CategoryDto = {};
  listeCategorie: Array<CategoryDto> = [];
  errorsMsg: Array<string> = [];
  file: File | null = null;
  imgUrl: string | ArrayBuffer = 'assets/product.png';
  
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private articleService: ArticleService,
    private categoryService: CategoryService,
    private photoService: PhotosService
  ) { }

  ngOnInit(): void {
    this.categoryService.findAll().subscribe(
      categories =>{
        this.listeCategorie = categories;
      });

    const idArticle = this.activatedRoute.snapshot.params.idArticle;
    console.log(idArticle);
    if (idArticle) {
      console.log("je suis inside IF")
      this.articleService.findArticleById(idArticle).subscribe(
        article => {
          this.articleDto = article;
          this.categorieDto = this.articleDto.category ? this.articleDto.category : {};
          console.log(this.categorieDto);
        }
      );
    }
  }

  cancel(): void {
    this.router.navigate(['articles']);
  }

  enregistrerArticle(): void {
    this.articleDto.category = this.categorieDto;
    this.articleService.enregistrerArticle(this.articleDto).subscribe(
      article =>{
        this.savePhoto(article.id, article.codeArticle);
        this.router.navigate(['articles'])
      },
      error =>{
          this.errorsMsg = error.error.errors;
      }
    );
  }

  calculerTTC(): void {
    if (this.articleDto.prixUnitaireHt && this.articleDto.tauxTva) {
      // prixHt + prixHt *(tauxTva /100)
      this.articleDto.prixUnitaireTtc =
      +this.articleDto.prixUnitaireHt + (+(this.articleDto.prixUnitaireHt * (this.articleDto.tauxTva / 100)));
    }
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

  savePhoto(idArticle?: number, titre?: string): void {
    if (idArticle && titre && this.file) {
      const params: SavePhotoParams = {
        id: idArticle, 
        file: this.file, 
        title: titre,
        context: "article"
      };
      this.photoService.savePhoto(params).subscribe(
        res => {
          this.router.navigate(['articles']);
        }
      );
    } else {
      this.router.navigate(['articles']);
    }
  }

}
