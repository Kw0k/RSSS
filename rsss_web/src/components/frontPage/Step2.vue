<template>

    <div>  <el-steps :active="2" simple>
        <el-step title="识别"   icon="el-icon-camera-solid"></el-step>
        <el-step title="付款" icon="el-icon-kwok-consumption-fill"></el-step>
        <el-step title="完成" icon="el-icon-success"></el-step>
    </el-steps>
        <audio controls="controls" :src="sound" ref="audio" hidden='true'></audio>
        <audio controls="controls" :src="sound2" ref="audio2" hidden='true'></audio>
<h2>订单详情</h2>

        <img :src='orderImgSrc' ref='orderImg' class='orderImg' alt=''>
      <div class="tableDiv">
        <el-table
            :data="tableData"
            :height=tableHeight
            border
            class='orderTable'
            show-summary
            :summary-method='getSummaries'
        >
            <el-table-column
                prop="goodsName"
                label="商品名称"
                width="180">
            </el-table-column>
            <el-table-column
                prop="amount"
                label="数量">
            </el-table-column>
            <el-table-column
                prop="price"
                label="单价"
                width="180">
            </el-table-column>
        </el-table>
        <span style="color: red; margin-right: 20px;margin-top:5px;text-align: right;width: 100%;display: block;">优惠活动：{{activityComment}}&nbsp;&nbsp;&nbsp;&nbsp;应付价格：{{trueTotal}}元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
      </div>
        <el-divider></el-divider>
        <div class='paydiv'>
            <h2>付款方式</h2>
            <el-button class='paybtn' type='primary' @click='alipay' :loading.sync="alipayLoading"><i class="el-icon-kwok-zhifubao btnIcon"></i> 支付宝</el-button>
        </div>
        <el-dialog :title="payTitle" :visible.sync="dialogPayVisible"  :show-close=false width='300px'>
            <img :src='payImgSrc' class='patImg' alt=''>
            <i class='el-icon-kwok-zhifubao' style='font-size: 25px;color: #2d8cf0;margin: 10px 25px'> 支付宝扫码付款</i>
        </el-dialog>
        <el-button
            class='homeBtn'
            @click="goHome"
            type="warning"
            size="medium"
            icon='el-icon-s-home'
        >
            首页
        </el-button>
        <el-button
            class='nextBtn'
            @click="goPre"
            type="success"
            size="medium"
        icon='el-icon-caret-left'
        >
            上一页
        </el-button>
    </div>

</template>

<script>
import sounds from '@/assets/mp3/3.mp3';
import sounds2 from '@/assets/mp3/4.mp3';
import axios from "axios";
export default {
    data() {
        return {
            sound:sounds,
            sound2:sounds2,
            dialogPayVisible:false,
            payTitle:"",
            payImgSrc:"",
            orderImgSrc:"",
            tableHeight:280,
            tableData:[],
            activityComment:'',
            alipayLoading:false,
            trueTotal:0,
          serverUrl:axios.defaults.baseURL
        };
    },
    methods:{
        goHome(){
            this.$router.push('/front/index');
        },
        getStatus(){
            this.axios.get(
                '/getOrderStatus'
            ).then((response) =>{
                if (response.data.flag){
                    if (response.data.data.status===2){
                        this.$router.push('/front/step3');
                    }else{
                        var that = this;
                        setTimeout(function () {
                            that.getStatus();
                        },500);
                    }
                }else {
                    var that = this;
                    setTimeout(function () {
                        that.getStatus();
                    },500);
                }
            })
                .catch(function (error) {
                    console.log(error);
                });
        },
        goPre(){
            this.$router.push('/front/step1');
        },
        getOrder(){
            this.axios.get(
                '/getOrder'
            ).then((response) =>{
                if (response.data.flag){
                    this.orderImgSrc=this.serverUrl+response.data.data.image;
                    this.trueTotal=response.data.data.total;
                    this.activityComment=response.data.data.activityComment;
                   this.tableData=response.data.data.orderItems;
                }else {
                  if(response.data.code===403) this.$router.push('/login');
                    this.$router.push('/front/step1');
                }
            })
                .catch(function (error) {
                    console.log(error);
                });
        },
        alipay(){
            this.$refs.audio.pause();
            this.$refs.audio.currentTime = 0;
          this.alipayLoading=true;
            this.axios.get(
                '/aliPay'
            ).then((response) =>{
              this.alipayLoading=false;
                if (response.data.flag){
                    this.$refs.audio2.currentTime = 0;
                    this.$refs.audio2.play();
                    this.payTitle="支付宝付款"
                    this.dialogPayVisible=true;
                    this.payImgSrc=this.serverUrl+"/image/alipay/qr-"+response.data.data.id+".png"
                  this.getStatus();
                }else {
                }
            })
                .catch(function (error) {
                  this.alipayLoading=false;
                  console.log(error);
                });
        },
        //指定方式求和
        getSummaries(param) {
            const { columns, data } = param;
            const sums = [];
            var sum=0;
            data.forEach((orderItme,index)=>{
                sum+=orderItme.amount*orderItme.price;
            })
            columns.forEach((column, index) => {
                if (index === 0) {
                    sums[index] = '总价';
                    return;
                }
                if (column.property === 'price') {
                    sums[index]=sum+'元'
                }
            });
            return sums
        }
    },
    mounted() {this.getOrder();
        this.tableHeight=this.$refs.orderImg.offsetHeight;
    this.$refs.audio.currentTime = 0;
    this.$refs.audio.play();
}
};
</script>

<style scoped>
.patImg{
    margin: 10px auto;
    display: block;
    width: 200px;
    height: 200px;
    border: gray 1px solid;
}
h2{
    margin-top: 10px;
    margin-bottom: 10px;
}
.tableDiv{
  width: calc(50% - 10px);
  float: right;
}
.orderTable{
    margin-left: 5px;
    width:100%;
}
.orderImg{
    margin-right: 5px;
  height: 300px;
    width: calc(50% - 10px);
}
.btnIcon{
    font-size: 30px;
}
.paybtn{
    width: 200px;
    font-size: 30px;
    height: 50px;
}
.homeBtn{
    height: 80px;
    font-size: 35px;
    margin-bottom: 10px;
    position: absolute;
    bottom: 0;
    width: calc(50% - 10px);
}
.nextBtn{
    height: 80px;
    font-size: 35px;
    margin-bottom: 10px;
    left: 50%;
    position: absolute;
    bottom: 0;
    width: calc(50% - 20px);
}
</style>
