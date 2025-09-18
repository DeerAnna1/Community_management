// 工程全局配置，如端口号、是否启用eslint、是否启用压缩等。
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: process.env.NODE_ENV === 'production' ? '/Community_management/' : '/',
  devServer: {
    port: 7070
  },
  chainWebpack: config =>{
    config.plugin('html')
        .tap(args => {
          args[0].title = "社团活动管理系统";
          return args;
        })
  }
})