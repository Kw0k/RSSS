<template>
  <div v-loading.fullscreen.lock="loading"
       element-loading-text="正在识别，请稍后"
  >
      <el-steps :active="1" simple>
          <el-step title="识别"   icon="el-icon-camera-solid"></el-step>
          <el-step title="付款" icon="el-icon-kwok-consumption-fill"></el-step>
          <el-step title="完成" icon="el-icon-success"></el-step>
      </el-steps>
      <div class="box" >
          <audio controls="controls" :src="sound" ref="audio" hidden='true'></audio>
          <audio controls="controls" :src="sound2" ref="audio2" hidden='true'></audio>
          <audio controls="controls" :src="sound3" ref="audio3" hidden='true'></audio>
         <video ref='video' id="videoCamera" class='videoCanvas' autoPlay></video>
              <canvas ref='canvas' id="canvasCamera" class='videoCanvas2' :width='videoWidth' :height='videoHeight'></canvas>
          <el-button
              class='homeBtn'
              @click="home"
              type="warning"
              size="medium"
                icon='el-icon-s-home'
          >
          </el-button>
              <el-button
                  class='nextBtn'
                  @click="drawImage"
                  type="success"
                  size="medium">
                  下一步
              </el-button>
      </div>
  </div>
</template>
<script>
import axios from 'axios';
import sounds from '@/assets/mp3/1.mp3';
import sounds2 from '@/assets/mp3/2.mp3';
import sounds3 from '@/assets/mp3/6.mp3';
export default {
  data() {
    return {
      os: false,//控制摄像头开关
      thisVideo: null,
      thisContext: null,
      thisCancas: null,
      videoWidth: 1280,
      videoHeight: 720,
        sound:sounds,
        sound2:sounds2,
        sound3:sounds3,
        loading:false
    };
  },
  created() {

  },
  methods: {
      upload(){
          axios.post(
              '/orderUpload',
              this.qs.stringify({
                  image: this.imgSrc
              })
          ).then((response) =>{
              this.loading=false
              if (response.data.flag){
                  this.$message.success(response.data.msg);
                  this.$router.push('/front/step2');
              }else {
                  this.resetCanvas()
                  this.$refs.audio2.pause();
                  this.$refs.audio2.currentTime = 0;
                  this.$refs.audio3.currentTime = 0;
                  this.$refs.audio3.play()
                  this.$message.error(response.data.msg);
              }
          })
              .catch(function (error) {
                  console.log(error);
              });
      },
      home(){
          this.$router.push('/front/index');
      },
      getCompetence() {
          //必须在model中render后才可获取到dom节点,直接获取无法获取到model中的dom节点
          this.$nextTick(() => {
              this.$refs.canvas.style.setProperty("display", "none");
              const _this = this;
              this.os = false;//切换成关闭摄像头
              this.thisCancas = document.getElementById('canvasCamera');
              this.thisContext = this.thisCancas.getContext('2d');
              this.thisVideo = document.getElementById('videoCamera');
              // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
              if (navigator.mediaDevices === undefined) {
                  navigator.menavigatordiaDevices = {}
              }
              // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象
              // 使用getUserMedia，因为它会覆盖现有的属性。
              // 这里，如果缺少getUserMedia属性，就添加它。
              if (navigator.mediaDevices.getUserMedia === undefined) {
                  navigator.mediaDevices.getUserMedia = function (constraints) {
                      // 首先获取现存的getUserMedia(如果存在)
                      let getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia;
                      // 有些浏览器不支持，会返回错误信息
                      // 保持接口一致
                      if (!getUserMedia) {
                          return Promise.reject(new Error('getUserMedia is not implemented in this browser'))
                      }
                      // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
                      return new Promise(function (resolve, reject) {
                          getUserMedia.call(navigator, constraints, resolve, reject)
                      })
                  }
              }
              var constraints = {
                  audio: false,
                  video: {width: _this.videoWidth, height: _this.videoHeight, transform: 'scaleX(-1)',facingMode: "environment" }
              };
              navigator.mediaDevices.getUserMedia(constraints).then(function (stream) {
                  // 旧的浏览器可能没有srcObject
                  if ('srcObject' in _this.thisVideo) {
                      _this.thisVideo.srcObject = stream
                  } else {
                      // 避免在新的浏览器中使用它，因为它正在被弃用。
                      _this.thisVideo.src = window.URL.createObjectURL(stream)
                  }
                  _this.thisVideo.onloadedmetadata = function (e) {
                      _this.thisVideo.play()
                  }
              }).catch(err => {
                  this.$notify({
                      title: '警告',
                      message: '没有开启摄像头权限或浏览器版本不兼容.',
                      type: 'warning'
                  });
              });
          });
      },
    //绘制图片
    drawImage() {
        this.$refs.audio.pause();
        this.$refs.audio.currentTime = 0;
        this.$refs.audio2.currentTime = 0;
        this.$refs.audio2.play()
        this.thisContext.drawImage(this.thisVideo, 0, 0,this.videoWidth,this.videoHeight);
        this.imgSrc = this.thisCancas.toDataURL('image/png');
        this.$refs.canvas.style.setProperty("display", "");
        this.$refs.video.style.setProperty("display", "none");
        this.stopNavigator();
        this.upload();
        this.loading=true;

    },
    //清空画布
    clearCanvas(id) {
      let c = document.getElementById(id);
      let cxt = c.getContext("2d");
      cxt.clearRect(0, 0, c.width, c.height);
    },
    //重置画布
    resetCanvas() {
        this.$refs.video.style.setProperty("display", "");
        this.getCompetence();
        this.imgSrc = "";
      this.clearCanvas('canvasCamera');
    },
    //关闭摄像头
    stopNavigator() {
      if (this.thisVideo && this.thisVideo !== null) {
        this.thisVideo.srcObject.getTracks()[0].stop();
        this.os = true;//切换成打开摄像头
      }
    },
  },
    mounted() {
        this.videoHeight=this.$refs.video.offsetHeight;
        this.videoWidth=this.$refs.video.offsetWidth;
        this.$refs.canvas.style.setProperty("display", "none");
        this.getCompetence();
        this.$refs.audio.currentTime = 0;
        this.$refs.audio.play();
    }
};
</script>

<style>
.videoCanvas2{
    margin: 0 auto;
    bottom: 100px;
    display: block;
}
.videoCanvas{
    position: absolute;
    width: calc(100% - 20px);
    height: calc(100% - 166px);
    bottom: 100px;
    display: block;
}
.homeBtn{
    height: 80px;
    font-size: 35px;
    margin-bottom: 10px;
    position: absolute;
    bottom: 0;
    width: calc(10% - 10px);
}
.nextBtn{
    height: 80px;
    font-size: 35px;
    margin-bottom: 10px;
    left: 10%;
    position: absolute;
    bottom: 0;
    width: calc(90% - 20px);
}
</style>
