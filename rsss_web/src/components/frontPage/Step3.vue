<template>
<div>
    <el-steps :active="3" simple>
        <el-step title="识别"   icon="el-icon-camera-solid"></el-step>
        <el-step title="付款" icon="el-icon-kwok-consumption-fill"></el-step>
        <el-step title="完成" icon="el-icon-success"></el-step>
    </el-steps>
    <el-carousel indicator-position="outside" height='350px'>
      <el-carousel-item v-for="value in data" >
        <img :src='serverURL+value.image' class='img' alt=''>
      </el-carousel-item>
    </el-carousel>
    <audio controls="controls" :src="sound" ref="audio" hidden='true'></audio>
<i class='el-icon-success succ' > 交易完成</i>
    <el-button  type='success' class='retBtn'
    @click='goHome'
    ><i ref='btnI' class='el-icon-s-home isize'></i></el-button>
</div>
</template>

<script>
import sounds from '@/assets/mp3/5.mp3';
import axios from "axios";

export default {
    data(){
        return{
          serverURL:axios.defaults.baseURL,
          data:[],
            sound:sounds,
            btnTitle:"（10秒后）返回首页"
        }
    },
    methods:{
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
      },
        goHome(){
            this.$router.push('/front/index');
        },
        countdown(){
            const TIME_COUNT = 10;
            if(!this.timer){
                this.count = TIME_COUNT;
                this.timer = setInterval(()=>{
                    if(this.count > 1 && this.count <= TIME_COUNT){
                        this.count--;
                        this.btnTitle="（"+this.count+"秒后）返回首页"
                        this.$refs.btnI.innerText = " "+this.btnTitle;
                    }else{
                        clearInterval(this.timer);
                        this.timer = null;
                        //跳转的页面写在此处
                        this.$router.push({
                            path: '/front/index'
                        });
                    }
                },1000)
            }
        }
        },
    mounted() {
      this.getData();
        this.$refs.audio.currentTime = 0;
        this.$refs.audio.play()
        this.$refs.btnI.innerText = " "+this.btnTitle;
        this.countdown()

    }
};
</script>

<style scoped>
.img{
    height: 100%;
    margin: 0 auto;
    width: 100%;
}
.isize{
    font-size: 60px;
}
.succ{
    width: 60%;
    text-align: center;
    margin: 30px 20%;
    font-size: 60px;
    color: #00a854;
}
.retBtn{
    margin: 30px 20%;
    font-size: 50px;
    width: 60%;
    height: 200px;
}
</style>
