<template>
    <div class="header">
        <div class="logo">餐厅自助结算系统</div>
        <div class="header-right">
            <div class="header-user-con">
                <!-- 全屏显示 -->
                <div class="btn-fullscreen" @click="handleFullScreen">
                    <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                        <i class="el-icon-rank"></i>
                    </el-tooltip>
                </div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{name}}
                        <i class="el-icon-caret-bottom"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item divided command="loginout" >退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
              <el-dialog class="logoutDialog" title="退出登录" :visible.sync="dialogFormVisible" width="200px"  :show-close=false>
                <el-form :model="param" :rules="rules" >
                  <el-form-item>
                    <el-input
                        size='medium'
                        type="password"
                        placeholder="请输入密码"
                        v-model="param.password"
                    ></el-input>
                  </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="dialogFormVisible = false">取 消</el-button>
                  <el-button type="primary" @click="logout">确 定</el-button>
                </div>
              </el-dialog>

            </div>
        </div>

    </div>

</template>
<script>
import axios from "axios";

export default {
    data() {
        return {
            fullscreen: false,
            name: '',
            message: 2,
          dialogFormVisible: false,
          param: {
            password: ''
          },
          rules: {
            password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
          },
        };
    },
    computed: {
    },
    methods: {
        logout(){
          axios.post(
              '/logout',
              this.qs.stringify({
                password: this.param.password
              })
          ).then((response) =>{
            if (response.data.flag){
              this.$message.success(response.data.msg);
              this.$router.push('/login');
            }else {
              this.$message.error(response.data.msg);
            }
          })
              .catch(function (error) {
                console.log(error);
              });
        },
        getUser(){
            this.axios.get(
                '/login'
            ).then((response) =>{
                if (response.data.flag){
                    this.name=response.data.data.username;
                    if (response.data.data.role===0||response.data.data.role===0)
                      this.$router.push('/admin');
                }else {
                    this.$router.push('/login');
                }
            })
                .catch(function (error) {
                    console.log(error);
                });
        },
        // 用户名下拉菜单选择事件
        handleCommand(command) {
            if (command === 'loginout') {
            this.dialogFormVisible=true;
            }
        },
        // 全屏事件
        handleFullScreen() {
            let element = document.documentElement;
            if (this.fullscreen) {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.webkitCancelFullScreen) {
                    document.webkitCancelFullScreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.msExitFullscreen) {
                    document.msExitFullscreen();
                }
            } else {
                if (element.requestFullscreen) {
                    element.requestFullscreen();
                } else if (element.webkitRequestFullScreen) {
                    element.webkitRequestFullScreen();
                } else if (element.mozRequestFullScreen) {
                    element.mozRequestFullScreen();
                } else if (element.msRequestFullscreen) {
                    // IE11
                    element.msRequestFullscreen();
                }
            }
            this.fullscreen = !this.fullscreen;
        }
    },
    mounted() {
    },
    created() {
        this.getUser();
    }
};
</script>
<style scoped>

.header {
    position: relative;
    box-sizing: border-box;
    width: 100%;
    height: 70px;
    font-size: 22px;
    color: #fff;
}
.collapse-btn {
    float: left;
    padding: 0 21px;
    cursor: pointer;
    line-height: 70px;
}
.header .logo {
  margin-left: 30px;
    float: left;
    width: 250px;
    line-height: 70px;
}
.header-right {
    float: right;
    padding-right: 50px;
}
.header-user-con {
    display: flex;
    height: 70px;
    align-items: center;
}
.btn-fullscreen {
    transform: rotate(45deg);
    margin-right: 5px;
    font-size: 24px;
}
.btn-bell,
.btn-fullscreen {
    position: relative;
    width: 30px;
    height: 30px;
    text-align: center;
    border-radius: 15px;
    cursor: pointer;
}
.btn-bell-badge {
    position: absolute;
    right: 0;
    top: -2px;
    width: 8px;
    height: 8px;
    border-radius: 4px;
    background: #f56c6c;
    color: #fff;
}
.btn-bell .el-icon-bell {
    color: #fff;
}
.user-name {
    margin-left: 10px;
}
.user-avator {
    margin-left: 20px;
}
.user-avator img {
    display: block;
    width: 40px;
    height: 40px;
    border-radius: 50%;
}
.el-dropdown-link {
    color: #fff;
    cursor: pointer;
}
.el-dropdown-menu__item {
    text-align: center;
}
</style>
