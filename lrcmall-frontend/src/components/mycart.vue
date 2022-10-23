<template>
  <!-- 购物车列表主体 -->
  <div class="booklist">
    <el-table :data="cartData" border>
      <el-table-column type="selection" width="55" />
      <el-table-column label="封面" width="110">
        <template v-slot="scope">
          <img :src="scope.row.image" alt="" height="50" />
        </template>
      </el-table-column>
      <el-table-column label="书名">
        <template v-slot="scope">
          <el-link @click="skiptodetail(scope.row.bookId)">{{
            scope.row.name
          }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者" width="140" />
      <el-table-column prop="surplus" label="库存" width="120" sortable>
      </el-table-column>
      <el-table-column prop="price" label="单价" width="120" sortable />
      <el-table-column prop="number" label="购买数量" width="150">
        <template v-slot="scope">
          <!-- v-model="bynow" :buynum="scope.row.num" v-on:input="handleBlur" @change="handleChange( scope.row )" -->
          <el-input-number
            v-model="scope.row.number"
            size="small"
            :min="1"
            :max="scope.row.surplus"
            @change="changeNumber(scope.row)"
          ></el-input-number>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div class="buy">
    <div class="total">
      <h3 v-text="total"></h3>
    </div>
    <el-button
      type="primary"
      round
      color="#bf242a"
      size="large"
      @click="purchase()"
      >立即购买</el-button
    >
  </div>
</template>

<script>
import { instance } from "@/axios/axios";

export default {
  data() {
    return {
      total: "总计：0.00",
      num: "",
      cartData: null,
      originData: null,
      input_number_value: 1,
      userid: 0,
      username: "",
    };
  },
  created: function () {
    let user = JSON.parse(localStorage.getItem("user"));
    console.log(JSON.parse(localStorage.getItem("user")));
    this.userid = user.userId;
    this.username = user.name;
    instance
      .get("/getCart", {
        params: {
          userid: this.userid,
        },
      })
      .then((res) => {
        this.originData = res.data;
        this.cartData = res.data;
        var temp = 0;
        this.cartData.forEach(function (item) {
          temp += item.number * item.price;
          item.price = (item.price / 100).toFixed(2);
        });
        this.total = "总计：" + (temp / 100).toFixed(2);
        console.log(this.cartData);
      });
  },
  methods: {
    changeNumber(row) {
      var data = {
        bookId: row.bookId,
        number: row.number,
        userId: this.userid,
      };
      instance
        .post("/changeCartNumber", data)
        .then((res) => {
          if (res.data != true)
            ElMessage({
              message: "修改数量失败",
              type: "error",
            });
          else {
            location.reload();
            this.$router.go(0);
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
    purchase() {
      var data = {
        userid: this.userid,
      };
      instance.post("/cartToOrder", data).then((res) => {
        this.$router.push({
          path: "/home/order",
          query: { total: res.data },
        });
        // this.$router.push({
        //       path: "/home/order",
        //       query: {
        //         userid:this.userid,
        //         username:this.username,
        //         }
        //     });
      });
    },
    // handleBlur读取输入并保存 value是input的数字
    // handleBlur(value){
    //     		this.input_number_value = value
    //     	},
    // handleChange( data ) {
    //       	data.buynum = this.input_number_value;
    //     	}
  },
};
</script>

<style scoped>
.buy {
  float: right;
  display: flex;
}
.buy > button {
  margin-left: 30px;
  margin-top: 10px;
}
.el-input-number {
  width: 8vw;
}
.buy .router-link-active {
  text-decoration: none;
  color: rgb(255, 255, 255);
}
.buy a {
  text-decoration: none;
  color: rgb(255, 255, 255);
}
</style>
