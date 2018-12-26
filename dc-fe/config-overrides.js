const loaders = require('@truelore/web-template/config/loaders');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

const customTheme = {
  loader: require.resolve('less-loader'),
  options: {
    modifyVars: {
      'primary-color': '#01D7BD'
    }
  }
}

const lessLoaderDev = {
  test: /\.less$/,
  use: [
    require.resolve('style-loader'),
    loaders.rawCssLoaderDev,
    loaders.postcssLoader,
    customTheme
  ],
};

const lessLoaderProd = {
  test: /\.less$/,
  loader: ExtractTextPlugin.extract(
    Object.assign(
      {
        fallback: require.resolve('style-loader'),
        use: [
          loaders.rawCssLoaderProd,
          loaders.postcssLoader,
          customTheme
        ],
      },
      loaders.extractTextPluginOptions
    )
  )
};

module.exports = function override(config, env) {
  // 修改主题
  const loaderList = config.module.rules.find(rule => Array.isArray(rule.oneOf));
  if (loaderList) {
    loaderList.oneOf = loaderList.oneOf.map(loader => {
      if (String(loader.test).includes('.less$')) {
        if (env.includes('prod')) {
          return lessLoaderProd;
        } else {
          return lessLoaderDev;
        }
      } else {
        return loader;
      }
    })
  }
  return config;
};