<template>
<div>
    <el-carousel class="carousel" indicator-position="outside" height='350px'>
      <el-carousel-item v-for="value in data" >
          <img :src='serverURL+value.image' class='img' alt=''>
      </el-carousel-item>
    </el-carousel>
  <div>
    <router-link to="/front/step1">
      <el-button type="success" class="button1"><i class="el-icon-s-goods btnIcon"></i><br>自助结算</el-button>
    </router-link>

    <el-button type="warning" class="button2" @click="dialogVisible=true"><i class="el-icon-s-comment btnIcon"></i><br>投诉建议</el-button>
  </div>

  <el-dialog title="投诉建议" :visible.sync="dialogVisible" @opened="creatQrCode" @close="delQrCode" width='300px'>

    <div id="qrcode" ref="qrcode"></div>
    <div class="qrcode" ref="qrCodeUrl"></div>
    <i class='el-icon-kwok-scanning' style='font-size: 15px;margin: 10px 35px'> 手机扫描二维码进行留言</i>
  </el-dialog>
</div>
</template>

<style>
.img{
    height: 100%;
    margin: 0 auto;
    width: 100%;
}
.btnIcon{
  font-size: 60px;
}
.button2{
    margin: 20px;
    font-size: 40px;
    border-radius: 15px;
    height: 200px;
    width: 20%;
}
.button1{
  margin: 20px;
  font-size: 40px;
  border-radius: 15px;
  height: 200px;
  width: 70%;
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}
.qrcode{
  margin: 0 auto;
  display: inline-block;
}
.qrcode img {
  width: 200px;
  height: 200px;
margin-left: 30px;
  background-color: #fff;
}
</style>
<script>
import QRCode from 'qrcodejs2'
import axios from "axios";
export default {
    data(){
        return{
          serverURL:axios.defaults.baseURL,
          dialogVisible:false,
           data:[]
        }
    },
  created() {
      this.getData()
 },
  mounted() {
  },
  methods:{
      delQrCode(){
        this.$refs.qrCodeUrl.innerHTML="";
      },
    creatQrCode() {
      var qrcode = new QRCode(this.$refs.qrCodeUrl, {
        text: location.origin+'/#/h5Comment', // 需要转换为二维码的内容
        width: 200,
        height: 200,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.H
      })
    },
      tousu(){
        this.dialogVisible=true;
      },
     getData(){
       axios.get(
           '/frontCarousel'
           ).then((response) =>{
         if (response.data.flag){
           this.data=response.data.data;
         }else {
           this.$message.error(response.data.msg)
           if(response.data.code===403)
             this.$router.push('/login');
         }
       })
           .catch(function (error) {
             console.log(error);
           });
     }
  }
};
</script>
