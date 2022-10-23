<template>
  <div class="home-layout">
    <el-container>
      <el-header class="total">
        <el-row>
          <el-col :span="8" class="logoimg"><img src="../assets/logo.png" />
          </el-col>
          <!-- <el-col :span="8" class="searchbar"> -->
          <!-- <el-input
              v-model="input4"
              class="w-50 m-2"
              placeholder="请输入书籍名称"
              :prefix-icon="Search"
            >
              <template #prefix>
                <el-icon class="el-input__icon"><search /></el-icon>
              </template>
            </el-input> -->
          <!-- </el-col> -->
          <el-col :span="16" v-text="showusername" class="hellotext"> </el-col>
        </el-row>
      </el-header>
      <el-header class="guide">
        <router-link :to="{ path: '/home' }">网站首页</router-link>
        <router-link :to="{ path: '/home/all' }">全部书籍</router-link>
        <router-link :to="{ path: '/home/cart' }" v-if=!admin>购物车</router-link>
        <router-link :to="{ path: '/home/order' }" v-if=!admin>我的订单</router-link>
        <router-link :to="{ path: '/home/order' }" v-if=admin>全部订单</router-link>
        <router-link :to="{ path: '/home/admin'}" v-if=admin>用户管理</router-link>
        <router-link :to="{ path: '/home/adminStatistics'}" v-if=admin>统计</router-link>
        <router-link :to="{ path: '/home/userStatistics'}" v-if=!admin>统计</router-link>
        <el-button text id="logout" size:small @click="logout()">退出登录</el-button>
        <!-- <router-link :to="{ path: '/login'}" @click="logout()" >退出登录</router-link> -->
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ElMessageBox, ElMessage } from "element-plus";
import { instance } from "@/axios/axios";
import { socket } from '@/main.js'
export default {
  data() {
    return {
      admin: false,
      userid: 0,
      username: "",
      showusername: "Hi!  ",
    };
  },
  created: function () {
    let user = JSON.parse(localStorage.getItem('user'));
    console.log(JSON.parse(localStorage.getItem('user')));
    this.userid = user.userId;
    this.username = user.name;
    this.showusername = "Hi!  " + this.username;
    if (user.type == 0) this.admin = true;
  },
  methods: {
    logout() {
      instance.get('/Logout', {
        params: {
          userid: this.userid
        }
      })
        .then((res) => {
          ElMessage({
            message: "退出登录！在线时长:" + res.data,
            type: "success",
          });
          if (socket === undefined || socket === null) {
          }
          else{
            socket.close();
          }
          this.$router.push({
            path: "/Login",
          });
        });
    }
  }
};
</script>

<style scoped>
.hellotext {
  font-size: 30px;
  font-weight: bolder;
  color: #ecf5ff;
  margin-top: 3vh;
  text-align: right;
}

.home-layout .total {
  height: 80px;
  background-color: #023a75;
}

.home-layout .guide {
  height: 40px;
  background-color: #fdfdfd;
  padding-top: 2vh;
  font-size: 30px;
}

.home-layout .el-main {
  background-color: #ecf5ff;
  height: 100%;
}

.home-layout .logoimg>img {
  height: 6vh;
  margin-top: 3vh;
  margin-left: 0.5vw;
}

.home-layout .searchbar {
  margin-top: 4vh;
}

.home-layout .userinfo {
  margin-left: 9vw;
  margin-top: 4vh;
  font-size: 20px;
  font-weight: bold;
  text-align: right;
  color: #ecf5ff;
}

.guide {
  display: flex;
}

#logout {
  margin-top: 0;
  padding: 0px;
  color: rgb(0, 0, 0);
}

.router-link-active {
  text-decoration: none;
  color: rgb(0, 0, 0);
}

a {
  text-decoration: none;
  color: rgb(0, 0, 0);
  font-size: 15px;
  margin-right: 15px;
  margin-top: 0;
  font-weight: 600;
}
</style>
