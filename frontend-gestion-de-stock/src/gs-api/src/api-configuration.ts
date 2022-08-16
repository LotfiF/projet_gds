/* tslint:disable */
import { Injectable } from '@angular/core';

/**
 * Global configuration for Api services
 */
@Injectable({
  providedIn: 'root',
})
export class ApiConfiguration {
  rootUrl: string = 'https://8081-lotfif-projetgds-sq2nzo5cznh.ws-eu59.gitpod.io';
}

export interface ApiConfigurationInterface {
  rootUrl?: string;
}
