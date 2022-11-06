<template>
  <!-- 搜索和添加项目栏 -->
  <div class="search_bar">
    <el-input
      id="searchbooklist"
      type="text"
      v-model="input"
      placeholder="请输入要搜索的书籍名称"
      @input="recoverdata"
      @keyup.enter="searchnow"
    />
    <el-button type="primary" @click="searchnow">搜索</el-button>
    <!-- 添加也只有管理员页面可显示 -->
    <el-button type="primary" @click="fullTextSearch">全文搜索</el-button>
    <el-button type="primary" @click="addBooks" v-if=admin>添加</el-button>
  </div>

  <!-- 添加商品对话框 -->
  <el-dialog v-model="dialogFormVisible" title="请录入书籍信息">
    <el-form :model="form" :rules="rules">
      <el-form-item label="封面:" :label-width="formLabelWidth">
        <el-input v-model="form.bookImage" autocomplete="off" />
        <!-- <el-upload
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :on-preview="handlePreview"
          multiple
          :limit="1"
          :file-list="fileList"
        >
          <el-button type="primary">上传封面</el-button>
        </el-upload> -->
      </el-form-item>
      <el-form-item label="书名:" :label-width="formLabelWidth">
        <el-input v-model="form.bookName" autocomplete="off" />
      </el-form-item>
      <el-form-item label="作者:" :label-width="formLabelWidth">
        <el-input v-model="form.bookAuthor" autocomplete="off" />
      </el-form-item>
      <el-form-item label="ISBN编号:" :label-width="formLabelWidth">
        <el-input v-model="form.bookISBN" autocomplete="off" />
      </el-form-item>
      <el-form-item label="库存:" :label-width="formLabelWidth">
        <el-input v-model="form.bookSurplus" autocomplete="off" />
      </el-form-item>
      <el-form-item label="单价:" :label-width="formLabelWidth">
        <el-input v-model="form.bookPrice" autocomplete="off" />
      </el-form-item>
      <el-form-item label="分类:" :label-width="formLabelWidth">
        <el-input v-model="form.bookType" autocomplete="off" />
      </el-form-item>
      <el-form-item label="简介:" :label-width="formLabelWidth">
        <el-input v-model="form.bookDescription" autocomplete="off" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAdd">确定</el-button>
      </span>
    </template>
  </el-dialog>

<!-- 修改商品对话框 -->
   <el-dialog v-model="modifyDialogFormVisible" title="请修改书籍信息">
    <el-form :model="form" :rules="rules">
      <el-form-item label="封面:" :label-width="formLabelWidth">
        <el-input v-model="form.bookImage" autocomplete="off" />
        <!-- <el-upload
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :on-preview="handlePreview"
          multiple
          :limit="1"
          :file-list="fileList"
        >
          <el-button type="primary">上传封面</el-button>
        </el-upload> -->
      </el-form-item>
      <el-form-item label="书名:" :label-width="formLabelWidth">
        <el-input v-model="form.bookName" autocomplete="off" />
      </el-form-item>
      <el-form-item label="作者:" :label-width="formLabelWidth">
        <el-input v-model="form.bookAuthor" autocomplete="off" />
      </el-form-item>
      <el-form-item label="ISBN编号:" :label-width="formLabelWidth">
        <el-input v-model="form.bookISBN" autocomplete="off" />
      </el-form-item>
      <el-form-item label="库存:" :label-width="formLabelWidth">
        <el-input v-model="form.bookSurplus" autocomplete="off" />
      </el-form-item>
      <el-form-item label="单价:" :label-width="formLabelWidth">
        <el-input v-model="form.bookPrice" autocomplete="off" />
      </el-form-item>
      <el-form-item label="分类:" :label-width="formLabelWidth">
        <el-input v-model="form.bookType" autocomplete="off" />
      </el-form-item>
      <el-form-item label="简介:" :label-width="formLabelWidth">
        <el-input v-model="form.bookDescription" autocomplete="off" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="modifyDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleModify">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 数据表格主体 -->
  <div class="booklist">
    <el-table :data="showData" border>
      <el-table-column type="selection" width="55" />
      <el-table-column label="封面" width="110"  v-if=!fullText>
        <template v-slot="scope">
          <img :src="scope.row.image" alt="" height="50"  />
        </template>
      </el-table-column>
      <el-table-column label="书名" width="150">
        <template v-slot="scope">
          <el-link @click="skiptodetail(scope.row.bookId)">{{
            scope.row.name
          }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者" width="140" />
      <el-table-column prop="bookType" label="图书分类" width="120" v-if=!fullText sortable />
      <el-table-column prop="surplus" label="库存" width="120" v-if=!fullText sortable>
      </el-table-column>
      <el-table-column prop="price" label="单价" width="120" v-if=!fullText sortable />
      <!-- 只有使用全文搜索功能时简介这一栏才会显示 -->
      <el-table-column prop="discription" label="简介" v-if=fullText />

      <!-- 删除这个只有管理员界面才可显示 -->
      <el-table-column label="操作" width="160" v-if=admin>
        <template #default="scope">
          <el-button size="small" type="primary" @click="Modify(scope.row)"
            >修改</el-button
          >
          <el-button size="small" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- 分页 -->
  <!-- <div class="page">
    <pagination :total="total" :pageSize="pageSize"></pagination>
  </div> -->
</template>

<script>
import pagination from "./pagination.vue";
import { ElMessageBox, ElMessage } from "element-plus";
import { instance } from "@/axios/axios";
// import { tableData } from "../main.js";

export default {
  name: "booklist",
  data() {
    return {
      admin:false,
      fullText:true,
      input: "",
      showData: null,
      tableData: null, //save the initial booklist
      total: 5,
      pageSize: 1,
      form: {
        bookId: "",
        bookImage: "",
        bookName: "",
        bookAuthor: "",
        bookISBN: "",
        bookSurplus: "",
        bookPrice: "",
        bookType:"",
        bookDescription:"",
      },
      formLabelWidth: "120px",
      dialogFormVisible: false,
      modifyDialogFormVisible: false,
      rules: {
        bookName: [
          { required: true, message: "请输入书籍名称", trigger: "blur" },
        ],
      },
      tabRowIndex: null,
      tabColumnIndex: null,
    };
  },
  created: function () {
    let user = JSON.parse(localStorage.getItem('user'));
    console.log(JSON.parse(localStorage.getItem('user')));
    if(user.type==0)this.admin=true;
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
  methods: {
    //recover the whole data while blur
    recoverdata() {
      if (this.input == "") {
        this.showData = this.tableData;
        this.fullText = false;
      }
    },
    //   搜索实现
    searchnow() {
      let bookname = this.input;
      this.fullText = false;
      let searchitem = [];
      var temp = String(this.input).toUpperCase();
      this.tableData.forEach(function (item) {
        if (String(item.name).toUpperCase().indexOf(temp) > -1) {
          searchitem.push(item);
        }
      });
      this.showData = JSON.parse(JSON.stringify(searchitem));
    },
    //全文搜索
    fullTextSearch(){
      instance.get("/searchBook", {
          params: {
            keyword: this.input
          }
        })
      .then((res) => {
        let tempData = res.data;
        console.log(tempData);
        this.tableData = tempData;
        this.showData = this.tableData;
        this.fullText = true;
      });
    },
    skiptodetail(bookid) {
      this.fullText = false;
      this.$router.push({
        path: "/home/detail",
        query: { id: bookid},
      });
    },
    //添加的相关函数
    addBooks() {
      this.dialogFormVisible = true;
    },
    handleAdd() {
      var data = {
            image: this.form.bookImage,
            name: this.form.bookName,
            author: this.form.bookAuthor,
            isbn: this.form.bookISBN,
            surplus: this.form.bookSurplus,
            price: (this.form.bookPrice*100),
            bookType: this.form.bookType,
            discription: this.form.bookDescription,
          };
          data=JSON.stringify(data);
      instance
        .post("/addBook", data)
        .then((res) => {
              if (res.data == true) {
                ElMessage({
                  type: "success",
                  message: "添加书籍成功",
                });
                location.reload();
                this.$router.go(0);
              } else {
                ElMessage({
                  type: "warning",
                  message: "添加书籍失败",
                });
              }
            });
    },
    // 删除
    handleDelete(item) {
      ElMessageBox.confirm("您确定要删除该书籍吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          console.log(item);
          instance
            .post("/deleteBooks", {data:item.name})
            .then((res) => {
              if (res.data == true) {
                location.reload();
                this.$router.go(0);
                ElMessage({
                  type: "success",
                  message: "成功删除",
                });
              } else {
                ElMessage({
                  type: "warning",
                  message: "删除失败",
                });
              }
            });
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "取消删除",
          });
        });
    },
    //modify
    Modify(item){
      this.form={
        bookId: item.bookId,
        bookImage: item.image,
        bookName: item.name,
        bookAuthor: item.author,
        bookISBN: item.isbn,
        bookSurplus: item.surplus,
        bookPrice: item.price,
        bookType:item.bookType,
        bookDescription:item.discription,
      };
      this.modifyDialogFormVisible = true;
    },
    handleModify(){
      var data = {
            bookId:this.form.bookId,
            image: this.form.bookImage,
            name: this.form.bookName,
            author: this.form.bookAuthor,
            isbn: this.form.bookISBN,
            surplus: this.form.bookSurplus,
            price: (this.form.bookPrice*100),
            bookType: this.form.bookType,
            discription: this.form.bookDescription,
          };
          data=JSON.stringify(data);
      instance
        .post("/modifyBook", data)
        .then((res) => {
              if (res.data == true) {
                ElMessage({
                  type: "success",
                  message: "修改书籍成功",
                });
                location.reload();
                this.$router.go(0);
              } else {
                ElMessage({
                  type: "warning",
                  message: "修改书籍失败",
                });
              }
            });
    },
  },
  components: {
    pagination,
  },
};
</script>

<style scoped>
.search_bar {
  margin: 10px;
  display: flex;
}
.search_bar > button {
  margin-left: 20px;
}
</style>