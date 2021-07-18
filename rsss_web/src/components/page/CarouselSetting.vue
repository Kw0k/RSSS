<template>
<div>
  <div class="crumbs">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>
        <i class="el-icon-s-tools"></i> 系统设置
      </el-breadcrumb-item>
      <el-breadcrumb-item>轮播广告设置</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <div class="container">
    <div class="handle-box">
      <router-link to="/admin/CarouselUpload">
      <el-button type="primary" icon="el-icon-plus">添加轮播广告</el-button>
        </router-link>
    </div>
    <el-table
        :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
    >
      <el-table-column prop="id" label="广告编号" width="55" align="center"></el-table-column>
      <el-table-column label='广告图片'>
        <template slot-scope="scope">
          <el-image
              class="table-td-thumb"
              :src="serverURL+scope.row.image"
              :preview-src-list="[serverURL+scope.row.image]"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column  label='广告状态' width="75" align="center">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              :active-value=1
              :inactive-value=0
              @change="changeStatus($event,scope.row.id)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="scope">
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
</div>
</template>

<script>
import axios from "axios";
import bus from "@/components/common/bus";

export default {
name: "CarouselSetting",
  data(){
      return{
        serverURL:axios.defaults.baseURL,
        tableData: [],
        pageSize: 5,
        currentPage:1,
        total:50,
        addVisible:false,
        form:[]
      }
  },
  created() {
  this.getData()
    bus.$on('close_carousel_upload', msg => {
      if (msg){
           this.getData()
      }
    });
  },
  methods:{
  changeStatus($event,id){
    axios.put(
        '/carousel',
        this.qs.stringify({
          id: id,
          status:$event
        })
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
  },
    getData() {
      axios.get(
          '/carousel')
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
    // 删除操作
    handleDelete(row) {
      // 二次确认删除
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      })
          .then(() => {
            this.axios.delete(
                '/carousel/'+row.id+'/'
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
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.table-td-thumb{
  display: block;
  margin: auto;
  width: 200px;
  height: 120px;
}
</style>
