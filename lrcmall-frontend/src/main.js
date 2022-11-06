import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import echarts from 'echarts'
import { ElMessage } from "element-plus";


createApp(App).use(store).use(router).use(ElementPlus).use(echarts).mount('#app')


let socket;
function useSocket(id) {
    // createApp(App).use(VueNativeSock, url, {
    //     reconnection: true, // 自动重新连接 (false)
    //     reconnectionAttempts: Infinity, // 重新连接尝试次数 (Infinity),
    //     reconnectionDelay: 2000, // 重新连接时间间隔
    // })
    socket = new WebSocket( "wss://localhost:8443/websocket/order/" +id);
    socket.onopen = function () {
        console.log("websocket已打开");
      };
      //获得消息事件
      socket.onmessage = function (msg) {
        var serverMsg = "收到服务端信息：" + msg.data;
        console.log(serverMsg);
        ElMessage({
            message: "您的订单:"+msg.data+"已被处理",
            type: "success",
          });
      };
      socket.onclose = function () {
        console.log("websocket已关闭");
      };
      socket.onerror = function () {
        console.log("websocket发生了错误");
      }
}

export { useSocket,socket }