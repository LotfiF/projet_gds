/* tslint:disable */
import { Injectable } from '@angular/core';

/**
 * Global configuration for Api services
 */
@Injectable({
  providedIn: 'root',
})
export class ApiConfiguration {
  rootUrl: string = 'https://8081-coffee-echidna-37t0zian.ws-eu17.gitpod.io';
}

export interface ApiConfigurationInterface {
  rootUrl?: string;
}
