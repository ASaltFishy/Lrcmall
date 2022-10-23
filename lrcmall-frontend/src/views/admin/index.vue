<template>
  <div>
    <el-table :data="showData" stripe style="width: 100%">
      <el-table-column prop="name" label="用户名称" width="180" />
      <el-table-column label="操作">
        <template #default="scope">
          <!-- <el-button size="small" type="primary" @click="Modify(scope.row)"
            >修改</el-button
          >
          <el-button size="small" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          > -->
          <el-switch
            v-model="scope.row.status"
            active-text="禁用"
            inactive-text="解禁"
            @change="handleChange(scope.row)"
          />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { ElMessage } from "element-plus";
import { instance } from "@/axios/axios";
export default {
  data() {
    return {
      showData: "",
      tableData: "",
      value: false,
    };
  },
  created: function () {
    instance.get("/getUsers").then((res) => {
      this.tableData = res.data;
      this.tableData.forEach(item => {
        if(item.type==2)item.status=true;
        else
        item.status=false;
      });
      this.showData = this.tableData;
      console.log(this.showData);
    });
  },
  methods: {
    handleChange(item) {
      console.log(item);
      instance.post("/changeUserType", { data: item.userId }).then((res) => {
        if (res.data == true) {
          ElMessage({
            type: "success",
            message: "修改成功",
          });
        } else {
          ElMessage({
            type: "warning",
            message: "修改失败",
          });
        }
      });
    },
  },
};
</script>

<style>
</style>
