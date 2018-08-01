const path = require('path');

module.exports = {
  mode: 'development',
  context: `${__dirname}`,
  devtool: 'source-map',
  entry: './src/index.js',
  output: {
    filename: 'main.js',
    path: path.resolve(`${__dirname}`, 'dist')
  },
  performance: {
    maxEntrypointSize: 512000,
    maxAssetSize: 512000
  },
  module: {
    rules: [
      {
        test: /\.css$/,
        use: [
           'style-loader',
           'css-loader'
         ]
       },
      {
        test: /\.(woff|woff2|eot|ttf|otf)$/,
        use: [
          {
            loader: 'url-loader',
            options: {
              limit: 60000
            }
          }
        ]
       }
     ]
  }
};
