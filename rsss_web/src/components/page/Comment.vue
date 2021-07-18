<template>
    <div class="">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-s-comment"></i> 留言管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-tabs v-model="message">
                <el-tab-pane :label="`未读留言(`+unreadCount+`)`" name="first">
                  <el-table
                      :data="unreadData.slice((unreadcurrentPage-1)*unreadpageSize,unreadcurrentPage*unreadpageSize)"
                      border
                      class="table"
                      ref="multipleTable"
                      header-cell-class-name="table-header"
                  >
                    <el-table-column prop="id" label="留言编号" width="80" align="center"></el-table-column>
                    <el-table-column prop="orderId" width="80" label='订单编号'>
                    </el-table-column>
                    <el-table-column label="留言内容" prop="content"></el-table-column>
                    <el-table-column label="联系方式" prop="contact" width="180" align="center"></el-table-column>
                    <el-table-column label="留言时间" prop="datatime" width="180"></el-table-column>
                    <el-table-column label="操作" width="100" align="center">
                      <template slot-scope="scope">
                        <el-button
                            type="primary"
                            icon="el-icon-edit"
                            @click="handleRead(scope.row)"
                        >已读</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div class="pagination">
                    <el-pagination
                        :current-page="unreadcurrentPage"
                        :page-sizes="[5, 10, 20, 50]"
                        :page-size="5"
                        background
                        layout="total, sizes, prev, pager, next"
                        :total="unreadCount"
                        @size-change="handleSizeChangeUnRead"
                        @current-change="handleCurrentChangeUnread"
                    ></el-pagination></div>
                </el-tab-pane>
                <el-tab-pane :label="`已读留言(`+readCount+`)`" name="second">
                  <el-table
                      :data="readData.slice((readcurrentPage-1)*readpageSize,readcurrentPage*readpageSize)"
                      border
                      class="table"
                      ref="multipleTable"
                      header-cell-class-name="table-header"
                  >
                    <el-table-column prop="id" label="留言编号" width="80" align="center"></el-table-column>
                    <el-table-column prop="orderId" width="80" label='订单编号'>
                    </el-table-column>
                    <el-table-column label="留言内容" prop="content"></el-table-column>
                    <el-table-column label="联系方式" prop="contact" width="180" align="center"></el-table-column>
                    <el-table-column label="留言时间" prop="datatime" width="180"></el-table-column>
                    <el-table-column label="操作" width="100" align="center">
                      <template slot-scope="scope">
                        <el-button
                            type="danger"
                            icon="el-icon-delete"
                            @click="handleDel(scope.row)"
                        >删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div class="pagination">
                    <el-pagination
                        :current-page="readcurrentPage"
                        :page-sizes="[5, 10, 20, 50]"
                        :page-size="5"
                        background
                        layout="total, sizes, prev, pager, next"
                        :total="readCount"
                        @size-change="handleSizeChangeRead"
                        @current-change="handleCurrentChangeread"
                    ></el-pagination></div>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: 'Comment',
        data() {
            return {
              unreadpageSize: 5,
              unreadcurrentPage:1,
              readpageSize: 5,
              readcurrentPage:1,
              unreadCount:50,
              readCount:0,
              unreadData:[],
              readData:[],
              message: 'first'
            }
        },
        methods: {
            getData(){
              axios.get(
                  '/getUnReadComment')
                  .then((response) =>{
                    if (response.data.flag){
                      this.unreadData=response.data.data;
                      this.unreadCount=response.data.total;
                    }else {
                      this.unreadData=response.data.data;
                      this.unreadCount=response.data.total;
                    }
                  })
                  .catch(function (error) {
                    console.log(error);
                  });
              axios.get(
                  '/getReadComment')
                  .then((response) =>{
                    if (response.data.flag){
                      this.readData=response.data.data;
                      this.readCount=response.data.total;
                    }else {
                      this.readData=response.data.data;
                      this.readCount=response.data.total;
                    }
                  })
                  .catch(function (error) {
                    console.log(error);
                  });
            },
          // 分页导航
          handleSizeChangeUnRead(val) {
            this.unreadpageSize =val;
          },
          handleCurrentChangeUnread(val) {
            this.unreadcurrentPage = val;
          },
          handleSizeChangeRead(val) {
            this.readpageSize =val;
          },
          handleCurrentChangeread(val) {
            this.readcurrentPage = val;
          },
          handleRead(comment) {
            axios.put(
                '/readComment',
                this.qs.stringify({
                  id: comment.id
                })
            ).then((response) =>{
              this.getData();
              if (response.data.flag){
              }else {
                this.$message.error(response.data.msg);
              }
            })
                .catch(function (error) {
                  console.log(error);
                });
            },
            handleDel(comment) {
              // 二次确认删除
              this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'})
                  .then(() => {
                    axios.delete(
                        '/delComment/'+comment.id+'/'
                    ).then((response) =>{
                      this.getData();
                      if (response.data.flag){
                        this.$message.success("删除成功");
                      }else {
                        this.$message.error(response.data.msg);
                      }
                    })
                        .catch(function (error) {
                          console.log(error);
                        });
                  })
                  .catch(() => {});
            }
        },
        computed: {

        },
      created() {
          this.getData();
      }
    }

</script>

<style>
.message-title{
    cursor: pointer;
}
.handle-row{
    margin-top: 30px;
}
</style>

