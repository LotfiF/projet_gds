import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CategoryService } from 'src/app/services/category/category.service';
import { CategoryDto } from 'src/gs-api/src/models/category-dto';

@Component({
  selector: 'app-page-categories',
  templateUrl: './page-categories.component.html',
  styleUrls: ['./page-categories.component.scss']
})
export class PageCategoriesComponent implements OnInit {

  listCategories: Array<CategoryDto> = [];
  selectedCatIdToDelete?: number = -1;
  errorMsg: string = '';

  constructor(
    private router: Router, 
    private categoryService: CategoryService 
  ) { }

  ngOnInit(): void {
    this.findAllCategories();
  }

  findAllCategories(): void {
    this.categoryService.findAll().subscribe(
      res => {
        this.listCategories = res;
      }
    );
  }

  nouvelCategorie(): void{
    this.router.navigate(['nouvellecategorie']);
  }

  modifierCategorie(id?: number): void{
    this.router.navigate(['nouvellecategorie', id]);
  }

  confirmerEtSupprimerCat(): void {
    if(this.selectedCatIdToDelete !== -1){
      this.categoryService.delete(this.selectedCatIdToDelete).subscribe(
        res => {
          this.findAllCategories();
        },
        error =>{
          this.errorMsg = error.error.message;
        }
      );
    }
  }

  annulerSuppressionCat(): void {
    this.selectedCatIdToDelete = -1;
  }

  selectCatPourSupprimer(id?: number): void{
    this.selectedCatIdToDelete = id;
  }

}
