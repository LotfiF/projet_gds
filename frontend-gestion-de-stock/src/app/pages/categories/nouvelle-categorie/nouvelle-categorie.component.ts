import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from 'src/app/services/category/category.service';
import { CategoryDto } from 'src/gs-api/src/models/category-dto';

@Component({
  selector: 'app-nouvelle-categorie',
  templateUrl: './nouvelle-categorie.component.html',
  styleUrls: ['./nouvelle-categorie.component.scss']
})
export class NouvelleCategorieComponent implements OnInit {

  categoryDto: CategoryDto = {};
  errorsMsg: Array<string> = [];
  
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private categoryService: CategoryService
  ) { }

  ngOnInit(): void {
    const idCategory = this.activatedRoute.snapshot.params.idCategory;
    console.log(idCategory);
    if (idCategory){
      this.categoryService.findById(idCategory).subscribe(
        category =>{
          this.categoryDto = category;
        }
      );
    }
  }

  cancel(): void {
    this.router.navigate(['categories']);
  }

  enregistrerCategory(): void {
    this.categoryService.enregistrerCategorie(this.categoryDto).subscribe(
      res =>{
        this.router.navigate(['categories']);
      },
      error =>{
          this.errorsMsg = error.error.errors;
      }
    );
  }


}
