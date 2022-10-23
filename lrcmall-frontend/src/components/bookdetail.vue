<template>
  <div>
    <el-row>
      <el-col :span="12">
        <img :src="this.image" />
      </el-col>
      <el-col :span="12" class="bookinfo">
        <el-descriptions :column="1">
          <el-descriptions-item>{{ this.name }}</el-descriptions-item>
          <el-descriptions-item label="作者">{{
            this.author
          }}</el-descriptions-item>
          <el-descriptions-item label="ISBN编码">{{
            this.isbn
          }}</el-descriptions-item>
          <el-descriptions-item label="分类">
            <el-tag size="small">{{ this.type }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="价格">{{
            this.price
          }}</el-descriptions-item>
          <el-descriptions-item label="简介">{{
            this.discription
          }}</el-descriptions-item>
        </el-descriptions>
        <el-row class="chooseNum" v-if="!admin">
          <el-col :span="7" :offset="3"> <p>请选择购买数量：</p></el-col>
          <el-col :span="14">
            <el-input-number
              class="choosebar"
              v-model="num"
              :min="1"
              :max="this.surplus"
              @change="handleChange"
              :size="small"
            />
            库存：{{ this.surplus }}
          </el-col>
        </el-row>
        <el-row v-if="!admin">
          <el-col :offset="12">
            <el-button
              type="primary"
              color="#bf242a"
              size="large"
              @click="addToCart()"
              >加入购物车</el-button
            >
            <el-button
              type="primary"
              color="#bf242a"
              size="large"
              @click="addToOrder()">
              立即购买
              </el-button
            ></el-col
          >
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ElMessage } from "element-plus";
import {instance} from "@/axios/axios";
export default {
  data() {
    return {

      num: 1,
      bookid: this.$route.query.id,
      userid: 0,
      admin:false,
      name: "",
      author: "",
      type: "",
      isbn: "",
      discription: "",
      price: "",
      surplus: "",
      image: "",
    };
  },
  created: function () {
    let user = JSON.parse(localStorage.getItem('user'));
    console.log(JSON.parse(localStorage.getItem('user')));
    this.userid = user.userId;
    if(user.type==0)this.admin=true;
  },
  methods: {
    addToCart() {
      var data= {
            bookid: this.bookid,
            userid:this.userid,
            number:this.num
          }
      instance.post('/addToCart', data)
        .then((res) => {
          if (res.data == false) console.log("invalid bookid");
          else {
            ElMessage({
              message: "加入购物车成功！",
              type: "success",
            });
          }
        });
    },
    addToOrder() {
      var data = {
            bookid: this.bookid,
            userid:this.userid,
            number:this.num
          }
      instance.post('/addToOrder', data)
        .then((res) => {
          ElMessage({
              message: "购买成功！请等待后台处理您的订单！",
              type: "success",
            });
          this.$router.push({
              path: "/home/order",
              query: {total: res.data},
            });
        });
        },
  },
  beforeCreate() {
    instance.get('/getBookDetail', {
          params: {
            bookid: this.$route.query.id
          }
        })
      .then((res) => {
        this.name = res.data.name;
        this.author = res.data.author;
        this.type = res.data.bookType;
        this.isbn = res.data.isbn;
        this.price = res.data.price;
        this.surplus = res.data.surplus;
        this.image = res.data.image;
        this.discription = res.data.discription;
        console.log(res.data);
      });
  },
};
</script>

<style scoped>
img {
  width: 33vw;
}
.chooseNum {
  margin-bottom: 20px;
}
/* .chooseNum > el-input-number {
  margin-top:2vh;
} */
.chooseNum > p {
  font-size: 23px;
  text-align: right;
}
.choosebar {
  margin-top: 1.5vh;
}

.router-link-active {
  text-decoration: none;
  color: rgb(255, 255, 255);
}
a {
  text-decoration: none;
  color: rgb(255, 255, 255);
}
</style>
<style>
#bookname {
  font-size: 60px;
}
</style>
