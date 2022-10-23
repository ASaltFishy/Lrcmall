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
    <h2>用户消费统计表:</h2>
    <el-table
      :data="customerData"
      border
      :default-sort="{ prop: 'number', order: 'descending' }"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="书籍名称" width="200" />
      <el-table-column prop="number" label="购买数量" width="200" />
      <el-table-column prop="money" label="小计" sortable />
    </el-table>
    <div class="info">
    <p3 v-text="totalNumber"></p3>
    <p3 v-text="totalCost"></p3>
  </div>
  </div>
</template>

<script>
import { instance } from "@/axios/axios";
export default {
  data() {
    return {
      totalNumber: "总计数量：0",
      totalCost: "总计金额：0.00",
      customerData: [],
      user: "",
      time: "",
      myChart2:"",
    };
  },
  created: function () {
    this.user = JSON.parse(localStorage.getItem("user"));
    // instance
    //   .get("/getOneSale", {
    //     params: {
    //       userId: this.user.userId,
    //     },
    //   })
    //   .then((res) => {
    //     console.log(res.data);
    //     this.customerData = res.data;
    //   });
  },
  methods: {
    handleTime() {
      console.log(this.time);
      let start = this.time[0];
      let end = this.time[1];
      instance
        .get("/getOneSale", {
          params: {
            userid:this.user.userId,
            start: start,
            end: end,
          },
        })
        .then((res) => {
          console.log(res.data);
          let tempNumber = 0;
          let tempCost = 0;
          this.customerData = res.data;
          this.customerData.forEach(item => {
             tempCost+=item.money*item.number;
            item.money = (item.money / 100).toFixed(2);
            tempNumber+=item.number;
          });
          this.totalCost = "总计金额："+(tempCost / 100).toFixed(2);
          this.totalNumber = "总计数量："+tempNumber;
        });
    }
  },
};
</script>

<style>
.info {
  float: right;
}
.info p3{
  margin-right: 2vw;
}
.timeselect {
  display: flex;
}

</style>
