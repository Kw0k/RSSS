<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-s-goods"></i> 商品管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-table
                :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
            >
                <el-table-column prop="id" label="商品编号" width="55" align="center"></el-table-column>
                <el-table-column prop="name" label='商品名称'>
                </el-table-column>
                <el-table-column label="单价">
                    <template slot-scope="scope">￥{{scope.row.price}}</template>
                </el-table-column>
                <el-table-column label="操作" width="180" align="center">
                  <template slot-scope="scope">
                  <el-button
                      type="primary"
                      icon="el-icon-edit"
                      @click="handleEdit(scope.row)"
                  >编辑</el-button>
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
      <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
        <el-form ref="form" :model="form" label-width="70px">
          <el-form-item label="商品编号">
            <el-input readonly v-model="form.id"></el-input>
          </el-form-item>
          <el-form-item label="商品名称">
            <el-input readonly v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="商品价格">
            <el-input v-model="form.price"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
      </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'goodstable',
    data() {
        return {
            tableData: [],
            pageSize: 5,
            currentPage:1,
            total:50,
          editVisible: false,
          form:[]
        };
    },
    created() {
        this.getData();
    },
    methods: {
        getData() {
            axios.get(
                '/Goods')
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
        // 分页导航
        handleSizeChange(val) {
            this.pageSize =val;
        },
        handleCurrentChange(val) {
            this.currentPage = val;
        },
      handleEdit(row){
          this.form=row;
        this.editVisible=true;
      },
      saveEdit() {
        axios.post(
            '/Goods',
            this.qs.stringify({
              id: this.form.id,
              price: this.form.price
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
.table {
    width: 100%;
    font-size: 14px;
}
</style>
