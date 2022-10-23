<template>
  <div id="content">
    <div class="login-header">
      <p class="p">注册:</p>
    </div>
    <form>
      <div class="login-input-box">
        <span class="icon icon-user"></span>
        <input type="text" v-model="username" placeholder="请设置用户名" />
      </div>
      <div class="login-input-box">
        <span class="icon icon-password"></span>
        <input type="password" v-model="pwd" placeholder="请设置密码" />
      </div>
      <div class="login-input-box">
        <span class="icon icon-password"></span>
        <input type="password" v-model="pwdtwice" placeholder="请再次输入密码" />
      </div>
      <div class="login-input-box">
        <span class="icon icon-password"></span>
        <input type="text" v-model="email" placeholder="请设置邮箱" />
      </div>
    </form>
    <div class="login-button-box">
      <button @click="Register()">Register!</button>
    </div>
    <div class="logon-box">
      <a href="">Forgot?</a>
      <router-link :to="{path:'/login'}">Login</router-link>
    </div>
  </div>
</template>

<script>
import { ElMessage } from "element-plus";
import {instance} from "@/axios/axios";
export default {
  data () {
    return {
        username: "",
        pwd: "",
        pwdtwice:"",
        email:"",
    }
  },
  methods: {
    Register(){
        if (this.username == "") {
        ElMessage({
              message: "请设置您的用户名",
              type: "warning",
            });
            return;
      }
      if(this.pwd==""){
        ElMessage({
              message: "请设置您的密码",
              type: "warning",
            });
            return;
      }
      if(this.pwd !=this.pwdtwice){
         ElMessage({
              message: "两次输入密码不一致,请重新输入",
              type: "warning",
            });
            return;
      };
      if(this.email==""){
         ElMessage({
              message: "请设置您的邮箱",
              type: "warning",
            });
            return;
      };
      var pattern = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
      if(!pattern.test(this.email)){
         ElMessage({
              message: "非法邮箱",
              type: "warning",
            });
            return;
      };
      var data = {
            name: this.username,
            password: this.pwd,
            email:this.email
          };
      data=JSON.stringify(data);
      console.log(data);
      instance.post('/addAccount', data)
        .then((res) => {
          if (res.data == false) {
            this.pwd = "";
            this.pwdtwice = "";
            this.username = "";
            ElMessage({
              message: "注册失败请更改用户名后尝试",
              type: "error",
            });
          } else {
            ElMessage({
              message: "注册成功",
              type: "success",
            });
            this.$router.push({
              path: "/login",
            });
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
}
</script>

<style >
@import "../../assets/login.css";
#content{
  height: 60vh;
}
.login-input-box{
    margin-top: 10px;
}
</style>
