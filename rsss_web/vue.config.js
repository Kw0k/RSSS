
module.exports ={
    devServer: {
    https: false
    },
    publicPath: './'
}
const CompressionWebpackPlugin = require('compression-webpack-plugin');
const isProduction = process.env.NODE_ENV === 'production';
module.exports = {
    configureWebpack: config => {
        if (isProduction) {
            config.plugins.push(new CompressionWebpackPlugin({
                algorithm: 'gzip',
                test: /\.(js|css|less)$/,
                threshold: 10240,
                minRatio: 0.8,
                deleteOriginalAssets: true
            }));
            config["performance"] = {//打包文件大小配置
                "maxEntrypointSize": 10000000,
                "maxAssetSize": 30000000
            }
        }
    }
}

