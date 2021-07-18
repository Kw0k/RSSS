<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-s-order"></i> 订单管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-select v-model="query.userid" clearable placeholder="下单机器" class="handle-select mr10">
                    <el-option v-for="(item,index) in users" :key="index" :label="item.username" :value="item.id"></el-option>
                </el-select>
                <el-select v-model="query.status" clearable placeholder="订单状态" class="handle-select mr10">
                    <el-option key="1" label="未付款" value="1"></el-option>
                    <el-option key="2" label="已付款" value="2"></el-option>
                    <el-option key="3" label="已退款" value="3"></el-option>
                </el-select>
              <el-select v-model="query.paytype" clearable placeholder="付款方式" class="handle-select mr10">
                <el-option key="1" label="支付宝" value="1"></el-option>
                <el-option key="2" label="微信" value="2"></el-option>
              </el-select>
              <el-input v-model="query.id" placeholder="订单号" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            </div>
            <el-table
                :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
            >
                <el-table-column prop="id" label="订单号" width="55" align="center"></el-table-column>
                <el-table-column prop="orderItems" label='订单详情'>
                    <template slot-scope="scope" >
                        <el-table  :data="scope.row.orderItems" :show-header='false'>
                            <el-table-column prop="name"  align="center">
                                <template slot-scope="scope">
                                    <span>{{scope.row.goodsName}}(￥{{scope.row.price}}) * {{scope.row.amount}}</span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </template>
                </el-table-column>
              <el-table-column label="订单图片" align="center">
                <template slot-scope="scope">
                  <el-image
                      class="table-td-thumb"
                      :src="serverURL+scope.row.image"
                      :preview-src-list="[serverURL+scope.row.image]"
                  ></el-image>
                </template>
              </el-table-column>
                <el-table-column label="订单总价">
                    <template slot-scope="scope">￥{{scope.row.total}}</template>
                </el-table-column>
              <el-table-column prop="activityComment" label="优惠活动"></el-table-column>

              <el-table-column prop="paytype" label="支付方式">
                    <template slot-scope="scope" >
                        <span v-if='scope.row.paytype===1'>支付宝</span>
                        <span v-if='scope.row.paytype==2'>微信</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" align="center">
                    <template slot-scope="scope">
                        <el-tag size="medium" v-if='scope.row.status===1'>未付款</el-tag>
                        <el-tag size="medium" v-else-if='scope.row.status===2' type="success">已付款</el-tag>
                        <el-tag size="medium" v-else-if='scope.row.status===3' type="warning">已退款</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="下单机器"></el-table-column>
                <el-table-column prop="datatime" label="下单时间"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                            icon="el-icon-delete"
                            type="danger"
                            @click="handleDelete(scope.$index, scope.row)"
                        >删除</el-button>
                        <el-button
                            v-if="scope.row.status===2"
                            icon="el-icon-refresh-left"
                            type="warning"
                            @click="handleRefund(scope.$index, scope.row)"
                        >退款</el-button>
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
import axios from 'axios';
export default {
    name: 'ordertable',
    data() {
        return {
            query: {
                userid: null,
                status: null,
                id:null,
                paytype:null
            },
          serverURL:axios.defaults.baseURL,
            tableData: [],
            pageSize: 5,
            currentPage:1,
            total:50,
            users:null
        };
    },
    created() {
        this.getData();
        this.getUsersData();
    },
    methods: {
        getUsersData(){
            axios.get(
                '/UserRole2'
                    ).then((response) =>{
                if (response.data.flag){
                    this.users=response.data.data
                }else {

                }
            })
                .catch(function (error) {
                    console.log(error);
                });
        },
        getData() {
            axios.post(
                '/Order',this.qs.stringify({
                      userid: this.query.userid,
                      status: this.query.status,
                      id:this.query.id,
                      paytype:this.query.paytype
                }
                )).then((response) =>{
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

        // 触发搜索按钮
        handleSearch() {
            this.$set(this.query, 'pageIndex', 1);
            this.getData();
        },
        //退款操作
        handleRefund(index, row){
          //二次确认退款
            this.$confirm('确定要退款吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    axios.post(
                        '/orderRefund',
                        this.qs.stringify({
                            id:row.id
                        })
                    ).then((response) =>{
                        if (response.data.flag){
                            this.$message.success(response.data.msg);
                            this.getData();
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
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.axios.delete(
                        '/order/'+row.id+'/'
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
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 180px;
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
