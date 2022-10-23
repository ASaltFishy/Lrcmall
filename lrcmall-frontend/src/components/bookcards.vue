<template>
  <el-row>
    <el-col 
    v-for="item in showData.slice(0,8)"
    :key="item"
    :span="6">
    <el-link @click="skiptodetail(item.bookId)">
      <el-card :body-style="{ padding: '20px' }" style="margin:5px">
        <el-image
          :src="item.image"
          class="image"
        />
        <div style="padding: 14px">
          <span>{{item.name}}</span>
        </div>
      </el-card>
      </el-link>
    </el-col>
  </el-row>
</template>

<script>
import { instance } from "@/axios/axios";

export default {
  name: "bookcards",
  data(){
    return{
      tableData:"",
      showData:"",
    }
  },
  created: function () {
    instance.get("/getBooks")
      .then((res) => {
        let tempData = res.data;
        tempData.forEach(function (item) {
          item.price = (item.price / 100).toFixed(2);
        });
        console.log(tempData);
        this.tableData = tempData;
        this.showData = this.tableData;
      });
  },
   methods:{
    skiptodetail(bookid) {
      this.$router.push({
        path: "/home/detail",
        query: { id: bookid},
      });
    },
   }
};
</script>

<style scoped>

.image {
  width: 100%;
  display: block;
}

.card >image {
  width: 100%;
  display: block;
}

.card {
  /*box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);*/
  transition: 0.3s;
  width: 20vw;
  border-radius: 5px;
  margin-top: 30px;
  margin-right: 30px;
  float: left;
}

.card:hover {
  box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.2);
}

a {
    text-decoration: none;
    color: #000;
}

/* 修改超链接的默认样式 */
/* 状态一: 未被访问过的链接 */
a:link {
    text-decoration: none;
    color: #000;
}

/* 状态二: 已经访问过的链接 */
a:visited {
    text-decoration: none;
    color: #000;
}

/* 状态三: 鼠标划过(停留)的链接(默认红色) */
a:hover {
    text-decoration: none;
    color: #000;
}
/* 状态四: 被点击的链接 */
a:active {
    text-decoration: none;
    color: #000;
}

/* .card .container{
    background-color: rgb(255, 255, 255);
    padding: 0;
    margin: 0;
    border-width: 1px;
    border-color: blue;
} */
</style>

