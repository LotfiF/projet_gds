/* tslint:disable */
import { Injectable } from '@angular/core';

/**
 * Global configuration for Api services
 */
@Injectable({
  providedIn: 'root',
})
export class ApiConfiguration {
  rootUrl: string = 'https://8081-copper-crocodile-ankj90m3.ws-eu17.gitpod.io';
}

export interface ApiConfigurationInterface {
  rootUrl?: string;
}
