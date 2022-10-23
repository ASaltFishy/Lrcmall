<template>
  <div id="zoumadeng">
    <el-carousel indicator-position="outside">
      <el-carousel-item v-for="item in imglist" :key="item">
        <h3><img :src="item" alt="" style="width:100%"/></h3>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>


<script>
export default {
  name: "zoumadeng",
  data() {
    return {
      imglist: [
        require("../assets/zmd1.jpeg"),
        require("../assets/zmd2.jpeg"),
        require("../assets/zmd3.jpeg"),
        require("../assets/zmd4.jpeg"),
      ],
    };
  },
};
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>