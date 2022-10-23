<template>
  <div>
    <el-dialog
      v-model="dialogVisible"
      title="订单信息"
      width="80%"
    >
    <div class="success">
        <h1>支付成功！</h1>
      <span>小主莫急，您的订单正在加紧送达~</span>
    </div>
    <div class="step">
    <el-steps :space="200" :active="1" finish-status="success" align-center>
    <el-step title="支付完成" />
    <el-step title="等待商品送达" />
    <el-step title="订单完成" />
    </el-steps>
    </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="dialogVisible = false"
            >确认</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
    data(){
        return{
            dialogVisible:true,
            total:this.$route.query.total,

        };
    },
};
</script>

<style scoped>
.success{
    text-align: center;

}
.step{
    margin-top: 30px;
    margin-left:20%;
}
</style>