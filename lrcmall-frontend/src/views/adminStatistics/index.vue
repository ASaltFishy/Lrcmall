<template>
  <div class="timeselect">
    <el-h4>选择查询时间：</el-h4>
    <el-date-picker
      v-model="time"
      type="daterange"
      value-format="YYYY-MM-DD"
      range-separator="至"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      @change="handleTime"
    />
  </div>
  <div>
    <h2>图书销量报表:</h2>
    <el-table :data="saleData" border :default-sort="{ prop: 'sale', order: 'descending' }">
      <el-table-column type="selection" width="55" />
      <el-table-column label="书名">
        <template v-slot="scope">
          <el-link @click="skiptodetail(scope.row.bookId)">{{
            scope.row.name
          }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者" width="300" />
      <el-table-column prop="sale" label="销量" width="450" sortable />
    </el-table>
  </div>
  <div>
    <h2>用户消费报表:</h2>
    <el-table :data="customerData" border :default-sort="{ prop: 'money', order: 'descending' }">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="顾客姓名" width="200" />
      <el-table-column
        prop="money"
        label="累计消费量"
        sortable
      />
    </el-table>
  </div>
</template>

<script>
import { instance } from "@/axios/axios";
export default {
  data() {
    return {
      saleData: "",
      customerData: "",
      time: "",
    };
  },
  methods: {
    skiptodetail(bookid) {
      this.$router.push({
        path: "/home/detail",
        query: { id: bookid, userid: this.userid, username: this.username },
      });
    },
    handleTime() {
      console.log(this.time);
      let start = this.time[0];
      let end = this.time[1];
      instance
        .get("/getAllSales", {
          params: {
            start: start,
            end: end,
          },
        })
        .then((res) => {
          console.log(res.data);
          this.saleData = res.data;
        });
      instance
        .get("/getAllConsumption", {
          params: {
            start: start,
            end: end,
          },
        })
        .then((res) => {
          console.log(res.data);
          this.customerData = res.data;
          this.customerData.forEach(function(item){
            item.money = (item.money / 100).toFixed(2);
          });
        });
    },
  },
};
</script>

<style>
.timeselect {
  display: flex;
}
</style>
