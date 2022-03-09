const path = require('path');

module.exports = {
  mode: 'production',
  context: `${__dirname}`,
  devtool: 'source-map',
  entry: './src/index.js',
  output: {
    filename: 'payment-chooser.js',
    path: path.resolve(`${__dirname}`, 'build/static/js')
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
