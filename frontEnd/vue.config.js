const webpack = require('webpack');

module.exports = {
  configureWebpack: {
    resolve: {
      alias: {
        'vue': 'vue/dist/vue.runtime.esm-bundler.js'
      }
    },
    plugins: [
      new webpack.DefinePlugin({
        __VUE_OPTIONS_API__: true,
        __VUE_PROD_DEVTOOLS__: false,
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: false
      })
    ],
    optimization: {
      minimize: false
    }
  },
  chainWebpack: config => {
    config.plugin('html').tap(args => {
      args[0].title = 'SixTale'; // 원하는 제목으로 변경
      return args;
    });
  },
  devServer: {
    https: false,
    port: 8083,
    open: true,
    proxy: {
      '/api/v1': {
        target: 'http://localhost:8080/'
      }
    },
    historyApiFallback: true,
    hot: true
  },
  css: {
    requireModuleExtension: false,
    loaderOptions: {
      postcss: {
        plugins: [
          require('cssnano')({
            preset: [
              'default',
              {
                discardComments: {
                  removeAll: true,
                },
                normalizeWhitespace: false,
                mergeRules: false
              },
            ],
          }),
        ],
      },
    },
  },
  lintOnSave: false,
  outputDir: '../backend/src/main/resources/dist'
};
