var webpack = require('webpack');

const VueLoaderPlugin = require('vue-loader/lib/plugin');

module.exports = require('./scalajs.webpack.config');

module.exports.entry = {
    "client-fastopt": [
        "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/client-fastopt.js",
        "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/client-jsdeps.js",
        "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/bundle.js"
        ],
    "client-opt": [
        "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/client-opt.js",
        "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/client-jsdeps.js",
        "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/bundle.js"
        ],
    "client-jsdeps": [
        "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/client-jsdeps.js",
        "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/bundle.js"
        ],
};

module.exports.output = {
  "path": "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/",
  "filename": "[name]-bundle.js"
};
//
//module.exports.resolve = {
//  mainFields: ['vue', 'vue-material']
//};

module.exports.module = {
  rules: [
       // ... other rules
       {
         test: /\.vue$/,
         loader: 'vue-loader'
       },
       {
         test: /\.vue$/,
         loader: 'vue-loader'
       },
       // this will apply to both plain `.js` files
       // AND `<script>` blocks in `.vue` files
       {
         test: /\.js$/,
         loader: 'babel-loader'
       },
       // this will apply to both plain `.css` files
       // AND `<style>` blocks in `.vue` files
       {
         test: /\.css$/,
         use: [
           'vue-style-loader',
           'css-loader',
           'style-loader'
         ]
       }
     ],
  };
module.exports.plugins = [
    // make sure to include the plugin!
    new VueLoaderPlugin(),
  ];

module.exports.optimization = {
  minimizer: []
};