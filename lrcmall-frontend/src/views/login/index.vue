<template>
  <div id="content">
    <div class="login-header">
      <p class="p">登录:</p>
    </div>
    <form>
      <div class="login-input-box">
        <span class="icon icon-user"></span>
        <!-- <el-input v-model="input" placeholder="请输入用户名" /> -->
        <input type="text" v-model="username" placeholder="请输入用户名" @keyup.enter="checkAccount()" />
      </div>
      <div class="login-input-box">
        <span class="icon icon-password"></span>
        <!-- <el-input v-model="input" placeholder="请输入密码" /> -->
        <input type="password" v-model="pwd" placeholder="请输入密码" @keyup.enter="checkAccount()" />
      </div>
    </form>
    <div class="remember-box">
      <label> <input type="checkbox" />&nbsp;Remember Me </label>
    </div>
    <div class="login-button-box">
      <button @click="checkAccount()">shopping right now!</button>
    </div>
    <div class="logon-box">
      <a href="">Forgot?</a>
      <router-link :to="{ path: '/register' }">Register</router-link>
    </div>
  </div>
</template>

<script>
import { instance } from "@/axios/axios";
import { ElMessage } from "element-plus";
import { useSocket } from '@/main.js'
export default {
  data() {
    return {
      username: "",
      pwd: "",
    };
  },
  methods: {
    checkAccount() {
      if (this.username == "") {
        ElMessage({
          message: "请输入您的用户名",
          type: "warning",
        });
        return;
      }
      else if (this.pwd == "") {
        ElMessage({
          message: "请输入您的密码",
          type: "warning",
        });
        return;
      }
      var data = {
        name: this.username,
        password: this.pwd,
      };
      data = JSON.stringify(data);
      console.log(data);
      instance
        .post("/checkAccount", data)
        .then((res) => {
          if (res.data == "") {
            this.pwd = "";
            ElMessage({
              message: "用户名或密码输入错误",
              type: "error",
            });
          }
          else {
            if (res.data.type == 2) {
              ElMessage({
                message: "您的账号已被禁用，请联系管理员解封",
                type: "error",
              })
            }
            else {
              localStorage.setItem("user", JSON.stringify(res.data));
              var id = res.data.userId;
              useSocket(id);
              console.log(res);
              ElMessage({
                message: "登录成功",
                type: "success",
              });
              this.$router.push({
                path: "/home",
                // query: { userid: res.data.userId, username: res.data.name },
              });
            }
          }
        })
        .catch((err) => {
          console.log(err);
          ElMessage({
            message: "网络错误",
            type: "error",
          });
        });
    },
  },
};
</script>

<style>
/* 引入了外部的css */
@import "../../assets/login.css";
</style>