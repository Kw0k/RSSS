<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-s-opportunity"></i> 营销管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
          <div class="handle-box">
              <el-button type="primary" icon="el-icon-plus" @click="dialogVisible=true">添加营销活动</el-button>
          </div>

          <el-table
              :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
              border
              class="table"
              ref="multipleTable"
              header-cell-class-name="table-header"
          >
            <el-table-column prop="id" label="活动编号" width="55" align="center"></el-table-column>
            <el-table-column label="活动类型">
              <template slot-scope="scope">
                <span v-if='scope.row.type===0'>满减活动</span>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="活动标题"  align="center"></el-table-column>
            <el-table-column prop="leastTotal" label="最低参与金额" width="55" align="center"></el-table-column>
            <el-table-column prop="discount" label="优惠金额" width="55" align="center"></el-table-column>
            <el-table-column prop="starttime" label="开始时间"  align="center"></el-table-column>
            <el-table-column prop="endtime" label="结束时间"  align="center"></el-table-column>
            <el-table-column  label='活动状态' width="75" align="center">
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
            <el-table-column  label='剩余量'>
              <template slot-scope="scope">
                <span v-if='scope.row.countType===1'>无限制</span>
                <span v-if='scope.row.countType===0'>{{scope.row.count}}</span>
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
      <el-dialog title="添加营销活动" :visible.sync="dialogVisible" width="400px">
        <el-form ref="form" :model="form" label-width="100px" :rules="rules">
          <el-form-item label="活动类型" prop="type">
            <el-select v-model="form.type" placeholder="活动类型"  class="handle-select mr10">
              <el-option  key="1" label="满减活动" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="活动标题" prop="title">
            <el-input v-model="form.title" type="text" style="width: 220px"></el-input>
          </el-form-item>
          <el-form-item label="开始时间" prop="starttime">
            <el-date-picker
                v-model="form.starttime"
                type="datetime"
                placeholder="选择开始时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="endtime">
            <el-date-picker
                v-model="form.endtime"
                type="datetime"
                placeholder="选择结束时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="参与活动金额" prop="leastTotal">
            <el-input v-model="form.leastTotal" type="number" placeholder="请输入参与活动的最低金额" style="width: 220px"></el-input>
          </el-form-item>
          <el-form-item label="优惠金额" prop="discount">
            <el-input v-model="form.discount" type="number" placeholder="请输入优惠金额" style="width: 220px"></el-input>
          </el-form-item>
          <el-form-item label="活动总容量" prop="count">
            <el-input v-model="form.count" type="number" placeholder="-1表示不限制" style="width: 220px"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible=false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
      </el-dialog>
    </div>
</template>
<script>
import axios from "axios";
export default {
    name: 'Market',
    data() {
      var checkTime = (rule, value, callback) => {
        if (this.form.endtime<=this.form.starttime) {
          callback(new Error('结束时间必须大于开始时间'));
        } else {
          callback();
        }
      };
      var checkDiscount = (rule, value, callback) => {
        if (Number(this.form.discount) >= Number(this.form.leastTotal)) {
          callback(new Error('优惠金额必须小于参与活动金额'));
        } else {
          callback();
        }
      };
      var checkCount = (rule, value, callback) => {
        if (Number(this.form.count)===-1 ||Number(this.form.count)>0) {
          callback();
        } else {
          callback(new Error('活动容量必须大于0或等于-1'));
        }
      };
      return{
        tableData: [],
        pageSize: 5,
        currentPage:1,
        total:50,
        dialogVisible:false,
        form:[],
        rules: {
          type: [
            { required: true, message: '请选择活动类型', trigger: 'change' },
          ],
          title: [
            { required: true, message: '请填写活动标题', trigger: 'blur' }
          ],
          starttime: [
            { type: 'date', required: true, message: '请选择开始时间', trigger: 'change' }
            ,{validator:checkTime,trigger: 'change'}
          ],
          endtime: [
            { type: 'date', required: true, message: '请选择结束时间', trigger: 'change' }
            ,{validator:checkTime,trigger: 'change'}
          ],
          leastTotal: [
            { required: true, message: '请输入参与活动金额', trigger: 'blur' }
            ,{validator:checkDiscount,trigger: 'blur'}

          ],
          discount: [
            { required: true, message: '请输入优惠金额', trigger: 'blur' }
            ,{validator:checkDiscount,trigger: 'blur'}

          ],
          count: [
            { required: true, message: '请输入活动总容量', trigger: 'blur' }
            ,{validator:checkCount,trigger: 'blur'}
          ],
        }
      }
    },
    methods: {
      saveAdd(){
        const moment = require('moment')
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.form.starttime=moment(this.form.starttime).format('YYYY-MM-DD HH:mm:ss')
            this.form.endtime=moment(this.form.endtime).format('YYYY-MM-DD HH:mm:ss')
            axios.post(
                '/activity',
                this.qs.stringify(this.form)
            ).then((response) =>{
              if (response.data.flag){
                this.getData();
                this.$message.success(response.data.msg);
              }else {
                this.$message.error(response.data.msg);
              }
            })
                .catch(function (error) {
                  console.log(error);
                });
            this.dialogVisible = false;
          } else {
            return false;
          }
        });
        console.log(this.form)
      },
      changeStatus($event,id){
        axios.put(
            '/activity',
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
            '/activity')
            .then((response) =>{
              if (response.data.flag){
                this.tableData=response.data.data;
                this.total=response.data.total;
              }else {
                this.tableData=response.data.data;
                this.total=response.data.total;
                this.$message.error(response.data.msg);
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
                  '/activity/'+row.id+'/'
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
    },
  created() {
      this.getData();
  }
};
</script>
<style scoped>
.handle-box {
  margin-bottom: 20px;
}
</style>
