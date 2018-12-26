import { apicrAgent } from './agent';
import BaseApi from './BaseApi';

export class RouteApi extends BaseApi {
  constructor() {
    super(apicrAgent);
  }

  async refresh() {
    await this.get({
      url: '/refreshRoute'
    });
  }
}

export const ROUTE_API = new RouteApi();
