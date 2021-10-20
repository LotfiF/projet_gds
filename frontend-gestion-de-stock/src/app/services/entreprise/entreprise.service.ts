import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EntrepriseDto } from 'src/gs-api/src/models/entreprise-dto';
import { EntreprisesService } from 'src/gs-api/src/services/entreprises.service';

@Injectable({
  providedIn: 'root'
})
export class EntrepriseService {

  constructor(
    private entreprisesService: EntreprisesService
  ) { }

  sinscrire(entreprise: EntrepriseDto): Observable<EntrepriseDto> {
    return this.entreprisesService.save(entreprise);
  }

  findById(idEntreprise: number): Observable<EntrepriseDto> {
    return this.entreprisesService.findById(idEntreprise); 
  }
}
