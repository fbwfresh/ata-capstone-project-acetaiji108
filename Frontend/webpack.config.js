const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    videoGamePage: path.resolve(__dirname, 'src', 'pages', 'videoGamePage.js'),
    recommendPage: path.resolve(__dirname, 'src', 'pages', 'recommendPage.js'),
    top5Page: path.resolve(__dirname,'src','pages','top5Page.js'),
    searchPage: path.resolve(__dirname, 'src', 'pages', 'searchPage.js'),
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),

  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    openPage: 'http://localhost:8080/enterWebsite.html',
    // diableHostChecks, otherwise we get an error about headers and the page won't render
    disableHostCheck: true,
    contentBase: 'packaging_additional_published_artifacts',
    // overlay shows a full-screen overlay in the browser when there are compiler errors or warnings
    overlay: true,
    proxy: [
      {
        context: [
          '/games',
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/VideoGameMainPage.html',
      filename: 'VideoGameMainPage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
        template: './src/recommendMainPage.html',
        filename: 'recommendMainPage.html',
        inject: false
    }),
      new HtmlWebpackPlugin({
        template: './src/Top5Page.html',
        filename: 'Top5Page.html',
        inject: false
      }),
      new HtmlWebpackPlugin({
              template: './src/searchPage.html',
              filename: 'searchPage.html',
              inject: false
      }),
    new HtmlWebpackPlugin({
      template: './src/enterWebsite.html',
      filename: 'enterWebsite.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
