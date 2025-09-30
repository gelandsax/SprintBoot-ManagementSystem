import axios from "axios";
import { useCurUserStore } from '../stores/user'
import { storeToRefs } from 'pinia'
const instance = axios.create({
  baseURL: 'http://localhost:3000/dev-api',
  timeout: 3000,
});
// 添加请求拦截器
instance.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    const curUserStore = useCurUserStore()
    const { curUser } = storeToRefs(curUserStore)
    if (curUser.value) {
      config.headers.Authorization = `Token ${curUser.value.token}`
    }
    return config;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  });
  instance.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response.data;
  }, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  });
export default instance;
