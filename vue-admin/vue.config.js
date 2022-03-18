let path = require('path');
const resolve = dir => path.join(__dirname, dir);
module.exports = {
  publicPath: process.env.VUE_APP_PUBLIC_PATH,
  //输出文件目录
  outputDir: 'dist',
  //放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录。
  assetsDir: 'static',
  //生产环境不需要生产map文件
  productionSourceMap: false,
  devServer: {
    open: true,
    proxy: {
      '^/api': {
        target: 'http://127.0.0.1:80',
        secure: false,
        changeOrigin: true,
      }
    }
  },
  chainWebpack: config => {
    //设置别名
    config.resolve.alias
        .set('@', resolve('src'))
        .set('@api', resolve('src/api'))
        .set('@assets', resolve('src/assets'))
        .set('@views', resolve('src/views'))
        .set('@store', resolve('src/store'))
        .set('@router', resolve('src/router'))
        .set('@components', resolve('src/components'));
  }

};

