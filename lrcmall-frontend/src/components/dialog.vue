<template>
<div>
  <el-dialog v-model="dialogFormVisible" title="请录入书籍信息">
    <el-form :model="form">
      <el-form-item label="Promotion name" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Zones" :label-width="formLabelWidth">
        <el-select v-model="form.region" placeholder="Please select a zone">
          <el-option label="Zone No.1" value="shanghai" />
          <el-option label="Zone No.2" value="beijing" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false"
          >确定</el-button
        >
      </span>
    </template>
  </el-dialog>
</div>
</template>
  

<script>
import { reactive, ref } from "vue";

const form = reactive({
  name: "",
  region: "",
  date1: "",
  date2: "",
  delivery: false,
  type: [],
  resource: "",
  desc: "",
});
export default {
  props: ["dialogFormVisible"],
  data() {
    return {
      // dialogFormVisible: false,
      form,
      formLabelWidth: "140px",
      dialogFormVisible:false,
    };
  },
};
</script>

<style scoped>
</style>