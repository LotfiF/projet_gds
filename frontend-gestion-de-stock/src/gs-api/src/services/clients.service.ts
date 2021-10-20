/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { ClientDto } from '../models/client-dto';
@Injectable({
  providedIn: 'root',
})
class ClientsService extends __BaseService {
  static readonly findAllPath = '/gestiondestock/v0/clients/all';
  static readonly savePath = '/gestiondestock/v0/clients/create';
  static readonly deletePath = '/gestiondestock/v0/clients/delete/{idClient}';
  static readonly findByIdPath = '/gestiondestock/v0/clients/{idClient}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Cette méthode permet de renvoyer la liste des clients dans la BDD
   * @return La liste des clients (voir une liste vide).
   */
  findAllResponse(): __Observable<__StrictHttpResponse<Array<ClientDto>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/gestiondestock/v0/clients/all`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ClientDto>>;
      })
    );
  }
  /**
   * Cette méthode permet de renvoyer la liste des clients dans la BDD
   * @return La liste des clients (voir une liste vide).
   */
  findAll(): __Observable<Array<ClientDto>> {
    return this.findAllResponse().pipe(
      __map(_r => _r.body as Array<ClientDto>)
    );
  }

  /**
   * Cette méthode permet d'enregistrer ou de modifier un client
   * @param body undefined
   * @return L'objet client a été créé/modifié avec succès.
   */
  saveResponse(body?: ClientDto): __Observable<__StrictHttpResponse<ClientDto>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = body;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/gestiondestock/v0/clients/create`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ClientDto>;
      })
    );
  }
  /**
   * Cette méthode permet d'enregistrer ou de modifier un client
   * @param body undefined
   * @return L'objet client a été créé/modifié avec succès.
   */
  save(body?: ClientDto): __Observable<ClientDto> {
    return this.saveResponse(body).pipe(
      __map(_r => _r.body as ClientDto)
    );
  }

  /**
   * Cette méthode permet de supprimer un client identifié par son ID.
   * @param idClient undefined
   * @return Le client a été supprimé.
   */
  deleteResponse(idClient: number): __Observable<__StrictHttpResponse<ClientDto>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/gestiondestock/v0/clients/delete/${idClient}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ClientDto>;
      })
    );
  }
  /**
   * Cette méthode permet de supprimer un client identifié par son ID.
   * @param idClient undefined
   * @return Le client a été supprimé.
   */
  delete(idClient: number): __Observable<ClientDto> {
    return this.deleteResponse(idClient).pipe(
      __map(_r => _r.body as ClientDto)
    );
  }

  /**
   * Cette méthode permet de chercher et récupérer un client par son ID
   * @param idClient undefined
   * @return le client a été trouvé dans la BDD
   */
  findByIdResponse(idClient: number): __Observable<__StrictHttpResponse<ClientDto>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/gestiondestock/v0/clients/${idClient}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ClientDto>;
      })
    );
  }
  /**
   * Cette méthode permet de chercher et récupérer un client par son ID
   * @param idClient undefined
   * @return le client a été trouvé dans la BDD
   */
  findById(idClient: number): __Observable<ClientDto> {
    return this.findByIdResponse(idClient).pipe(
      __map(_r => _r.body as ClientDto)
    );
  }
}

module ClientsService {
}

export { ClientsService }
