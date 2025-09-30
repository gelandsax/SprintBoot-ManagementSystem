import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    // 允许IP访问
    host: "0.0.0.0",
    // 应用端口 (默认:3000)
    port: Number(3000),
    // 运行是否自动打开浏览器
    open: true,

    proxy: {
      /** 代理前缀为 /dev-api 的请求  */
      ['/dev-api']: {
        changeOrigin: true,
        // 接口地址
        target: 'http://localhost:8080/api',
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req, res) => {
            proxyReq.removeHeader('referer')
            proxyReq.removeHeader('origin')
          });
        },
        rewrite: (path) =>
          path.replace(new RegExp("^" + '/dev-api'), ""),
      },
    },
  }
})