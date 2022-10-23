<template>
  <div class="search_bar">
    <el-input id="searchbooklist" type="text" v-model="input" placeholder="根据购买书籍搜索订单" @input="recoverdata"
      @keyup.enter="searchnow" />
    <el-button type="primary" @click="searchnow">搜索</el-button>
  </div>
  <div class="timeselect">
    <el-h4>根据购买时间搜素订单：</el-h4>
    <el-date-picker v-model="time" type="daterange" value-format="YYYY-MM-DD" range-separator="至"
      start-placeholder="开始日期" end-placeholder="结束日期" @change="handleTime" />
  </div>
  <el-table :data="showData" stripe style="width: 100%">
    <el-table-column prop="orderId" label="订单编号" width="180" />
    <el-table-column prop="content" label="订单内容" />
    <el-table-column prop="user" label="顾客id" v-if="admin" />
    <el-table-column prop="time" label="购买日期" />
    <el-table-column prop="cost" label="订单金额" />
  </el-table>
</template>

<script>
import { instance } from "@/axios/axios";
import { socket } from '@/main.js'
export default {
  data() {
    return {
      userid: 0,
      tableData: [],  //存放后端抓取原始数据
      showData: [],   //存放展示在表格中的数据
      tempData: [],    //存放处理过格式的后台数据
      input: "",
      admin: false,
      time: "",
    };
  },
  created: function () {
    let user = JSON.parse(localStorage.getItem("user"));
    this.userid = user.userId;
    if (user.type == 0) {
      this.admin = true;
      instance.get("/getAllOrders").then((res) => {
        // console.log(res.data);
        this.tableData = res.data;
        this.showData = JSON.parse(JSON.stringify(res.data));
        this.showData.forEach(function (item) {
          let entry = "";
          item.content = {};
          item.items.forEach(function (book) {
            entry += book.book.name + " × " + book.number + "  ;  ";
          });
          item.content = entry;
          if (item.owner.userId == null) item.user = item.owner;
          else item.user = item.owner.userId;
          item.cost = (item.cost / 100).toFixed(2);
          item.time = item.time.toString().slice(0, 10)
        });
        this.tempData = JSON.parse(JSON.stringify(this.showData));
        console.log(this.showData);
        console.log(this.tableData);
      });
    } else {
      instance
        .get("/getOrder", {
          params: {
            userid: this.userid,
          },
        })
        .then((res) => {
          this.tableData = res.data;
          console.log(this.tableData);
          this.showData = JSON.parse(JSON.stringify(res.data));
          this.showData.forEach(function (item) {
            let entry = "";
            item.content = {};
            item.items.forEach(function (book) {
              entry += book.book.name + " × " + book.number + "  ;  ";
            });
            item.content = entry;
            item.cost = (item.cost / 100).toFixed(2);
            item.time.toString().slice(0, 10)
          });
          this.tempData = JSON.parse(JSON.stringify(this.showData));
        });
    }
  },
  // mounted() {
  //   socket.onmessage = function (msg) {
  //     var serverMsg = "收到服务端信息：" + msg.data;
  //     console.log(serverMsg);
  //   };
  // },
  // sockets: {
  //   // 连接成功
  //   onopen() {
  //     console.log("socket success");
  //   },
  //   // 接收消息
  //   onmessage(e) {
  //     console.log("===data", JSON.parse(e.data));
  //   },
  //   // 连接报错
  //   onerror() {
  //     console.log("websocket发生了错误");
  //   },
  //   // 关闭连接
  //   onclose() {
  //     console.log("websocket已关闭");
  //   },
  // },
  destroyed() {
    // 销毁websocket
    // this.$disconnect();
  },
  methods: {
    // initWebSocket() {
    //   this.$connect("ws://localhost:8080/websocket/order/" + this.userid);
    // },
    searchnow() {
      let tempData = JSON.parse(JSON.stringify(this.tempData));
      let searchitem = [];
      var temp = String(this.input).toUpperCase();
      tempData.forEach(function (item) {
        let entry = "";
        var isAim = false;
        item.content = {};
        item.items.forEach(function (book) {
          if (String(book.book.name).toUpperCase().indexOf(temp) > -1)
            isAim = true;
        });
        if (isAim) {
          item.items.forEach(function (book) {
            entry += book.book.name + " × " + book.number + "  ;  ";
          });
          item.content = entry;
          item.cost = item.cost;
          searchitem.push(item);
        }
      });
      this.showData = searchitem;
    },
    recoverdata() {
      if (this.input == "") this.showData = JSON.parse(JSON.stringify(this.tempData));
    },
    handleTime() {
      if (this.time == null)
        this.recoverdata();
      let tempData = JSON.parse(JSON.stringify(this.tempData));
      var start = new Date(this.time[0]);
      var end = new Date(this.time[1]);
      console.log(start, end);
      let searchitem = [];
      tempData.forEach(function (item) {
        let entry = "";
        var isAim = false;
        var time = new Date(item.time);
        console.log(time);
        item.content = {};
        if (time >= start && time <= end) isAim = true;
        if (isAim) {
          item.items.forEach(function (book) {
            entry += book.book.name + " × " + book.number + "  ;  ";
          });
          item.content = entry;
          item.cost = item.cost;
          searchitem.push(item);
        }
      });
      this.showData = searchitem;
    }
  },
};
</script>

<style>
.el-main {
  width: 85vw;
  margin-left: 4vw;
}

.search_bar {
  margin: 30px;
  display: flex;
}

.search_bar>button {
  margin-left: 20px;
}

.timeselect {
  display: flex;
  margin: 10px;
}
</style>
