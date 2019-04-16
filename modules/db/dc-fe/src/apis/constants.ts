// 线上部署环境
let USER_HOST: string, APIM_HOST: string, APICR_HOST: string;

if (process.env.NODE_ENV !== 'development') {
  // ********** 这个条件分支的的东西，是部署相关配置，不要修改 ***********
  USER_HOST = 'USER_API_DEPLOY_ADDR';
  APIM_HOST = 'APIM_API_DEPLOY_ADDR';
  APICR_HOST = 'APIM_API_DEPLOY_ADDR';
} else {
  USER_HOST = 'http://dev.dmc.truelore.cn/user';
  APIM_HOST = 'http://dev.dmc.truelore.cn/apim';
  APICR_HOST = 'http://dev.dmc.truelore.cn/apim';
}

// USER_HOST='http://221.226.86.39/dmc/user'
// APIM_HOST = APICR_HOST = 'http://221.226.86.39/dmc/apim'

// const PROD_IP = '221.226.86.39';
// USER_HOST = `http://${PROD_IP}/dmc/user/`;
// APIM_HOST = `http://${PROD_IP}/dmc/apim/`;
// APICR_HOST = `http://${PROD_IP}/dmc/apicr/`;

export { USER_HOST, APIM_HOST, APICR_HOST };
