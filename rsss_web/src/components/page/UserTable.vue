<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-s-custom"></i> 用户管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
          <div class="handle-box">
            <el-button type="primary" icon="el-icon-plus" @click="addUser">添加用户</el-button>
          </div>
            <el-table
                :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
            >
                <el-table-column prop="id" label="用户编号" width="55" align="center"></el-table-column>
                <el-table-column prop="username" label='用户名'></el-table-column>
                <el-table-column label="用户类型">
                    <template slot-scope="scope">
                      <span v-if='scope.row.role===0'>超级管理员</span>
                      <span v-if='scope.row.role===1'>管理员</span>
                      <span v-if='scope.row.role===2'>终端机器</span>
                    </template>
                </el-table-column>
              <el-table-column prop="lastlogintime" label='最后登录时间'></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                  <template slot-scope="scope">
                  <el-button
                      type="primary"
                      icon="el-icon-edit"
                      @click="handleEdit(scope.row)"
                  >编辑</el-button>
                    <el-button
                        type="danger"
                        icon="el-icon-delete"
                        @click="handleDelete(scope.row)"
                    >删除</el-button>
                  </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    :current-page="currentPage"
                    :page-sizes="[5, 10, 20, 50]"
                    :page-size="5"
                    background
                    layout="total, sizes, prev, pager, next"
                    :total="total"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                ></el-pagination>
            </div>
        </div>
      <!-- 编辑弹出框 -->
      <el-dialog :title="dialogTitle" :visible.sync="editVisible" width="30%">
        <el-form ref="form" :model="form" label-width="70px">
          <el-form-item label="用户编号">
            <el-input readonly v-model="form.id" placeholder="无需填写"></el-input>
          </el-form-item>
          <el-form-item label="用户名" >
            <el-input  v-model="form.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="用户类型">
            <el-select v-model="form.role" placeholder="用户类型"  class="handle-select mr10">
              <el-option  key="1" label="超级管理员" :value="0"></el-option>
              <el-option key="2" label="管理员" :value="1"></el-option>
              <el-option key="3" label="终端机器" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" :placeholder="dialogPassword"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
      </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'userTable',
    data() {
        return {
            tableData: [],
            pageSize: 5,
            currentPage:1,
            total:50,
          editVisible: false,
          form:[],
          dialogTitle:"编辑",
          dialogPassword:"若无需修改密码，请留空"
        }
    },
    created() {
        this.getData();
    },
    methods: {
        cancel(){
        this.editVisible=false;
        this.getData()
         },
        getData() {
            axios.get(
                '/User')
                .then((response) =>{
                if (response.data.flag){
                    this.tableData=response.data.data;
                    this.total=response.data.total;
                }else {
                    this.tableData=response.data.data;
                }
            })
                .catch(function (error) {
                    console.log(error);
                });
        },
      addUser(){
          this.dialogPassword="请输入密码"
          this.dialogTitle="添加用户"
          this.editVisible=true;
          this.form=[];
      },
      handleDelete(row) {
        // 二次确认删除
        this.$confirm('确定要删除吗？', '提示', {
          type: 'warning'
        })
            .then(() => {
              this.axios.delete(
                  '/User/'+row.id+'/'
              ).then((response) =>{
                this.getData();
                if (response.data.flag){
                  this.$message.success(response.data.msg);
                }else {
                  this.$message.error(response.data.msg);
                }
              })
                  .catch(function (error) {
                    console.log(error);
                  });

            })
            .catch(() => {});
      },
        // 分页导航
        handleSizeChange(val) {
            this.pageSize =val;
        },
        handleCurrentChange(val) {
            this.currentPage = val;
        },
      handleEdit (row){
          this.dialogPassword="若无需修改密码，请留空"
          this.dialogTitle="编辑用户"
          this.form=row;
          this.form.password="";
        this.editVisible=true;
      },
      saveEdit() {
        axios.post(
            '/User',
            this.qs.stringify({
              id: this.form.id,
              username: this.form.username,
              password:this.form.password,
              role:this.form.role
            })
        ).then((response) =>{
          if (response.data.flag){
            this.$message.success(response.data.msg);
          }else {
            this.$message.error(response.data.msg);
          }
        })
            .catch(function (error) {
              console.log(error);
            });
        this.editVisible = false;
        this.getData()
      },
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 215px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}
.table {
    width: 100%;
    font-size: 14px;
}
.red {
    color: #ff0000;
}
.mr10 {
    margin-right: 10px;
}
.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>
