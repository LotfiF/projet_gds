import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { CategoryDto } from 'src/gs-api/src/models';
import { CategoriesService } from 'src/gs-api/src/services/categories.service';
import { UserService } from '../user/user.service';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(
    private userService: UserService,
    private categoryService: CategoriesService
  ) { }

  enregistrerCategorie(categoryDto: CategoryDto): Observable<CategoryDto> {
    categoryDto.idEntreprise = this.userService.getConnectedUser().entreprise?.id;
    return this.categoryService.save(categoryDto);
  }

  findAll(): Observable<CategoryDto[]>{
    return this.categoryService.findAll();
  }

  findById(idCategory: number): Observable<CategoryDto> {
    return this.categoryService.findById(idCategory); 
  }

  delete(idCategory?: number): Observable<any> {
    if (idCategory) {
      return this.categoryService.delete(idCategory);
    }
    return of();
  }
}
