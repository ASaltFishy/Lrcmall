<template>
  <div style="margin: 20px">
    <el-pagination
      background
      layout="total,prev, pager, next,jumper"
      :total="total"
      :page-size="pageSize"
      @current-change="changpage"
    />
  </div>
</template>

<script>
export default {
  props: {
    total: {
      type: Number,
      default: 100,
    },
    pageSize: {
      type: Number,
      default: 10,
    },
  },
  methods:{
      changepage(){
          
      }
  }
};
</script>

<style scoped>
</style>