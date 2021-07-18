<template>
    <div class='main'>
      <el-row class="row" :gutter="15">
        <el-col :span="6">
          <el-card shadow="hover"  style="height:100px;">
            <div class="user-info">
              <div class="user-info-cont">
                <div class="user-info-name">欢迎你，{{name}}。</div>
                <div>{{rolestr}}   上次登录时间：
                  <span>{{lastlogintime}}</span></div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"  :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-1">
              <i class="el-icon-kwok-iconmoney grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{sum}}</div>
                <div>今日销售额</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"  :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-3">
              <i class="el-icon-s-order grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{count}}</div>
                <div>今日订单量</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"  :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-2">
              <i class="el-icon-bell grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{comment}}</div>
                <div>今日留言</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row class="row" :gutter="15">
        <el-col :span="6">
          <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>今日订单量排行</span>
          </div>
            <div v-for="(item,i) in OrderCountListToday">
              {{item.username}}
              <el-progress :percentage="item.count/OrderCountListToday[0].count*100" :format="progressFormat(item.count)"></el-progress>
            </div>
        </el-card></el-col>
        <el-col :span="6">
          <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>今日销售额排行</span>
          </div><div v-for="(item,i) in OrderTotalSumListToday">
            {{item.username}}
            <el-progress :percentage="item.sum/OrderTotalSumListToday[0].sum*100" :format="progressFormat(item.sum)"></el-progress>
          </div>
        </el-card></el-col>
        <el-col :span="6">
          <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>7日订单量排行</span>
          </div> <div v-for="(item,i) in OrderCountList7days">
            {{item.username}}
            <el-progress :percentage="item.count/OrderCountList7days[0].count*100" :format="progressFormat(item.count)"></el-progress>
          </div>
        </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>7日销售额排行</span>
            </div>
            <div v-for="(item,i) in OrderTotalSumList7days">
              {{item.username}}
              <el-progress :percentage="item.sum/OrderTotalSumList7days[0].sum*100" :format="progressFormat(item.sum)"></el-progress>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row class="row">
        <el-card shadow="hover">
          <schart ref="line" class="schart" canvasId="line" :options="countoptions"></schart>
        </el-card>
      </el-row>
      <el-row>
        <el-card shadow="hover">
          <schart ref="line" class="schart" canvasId="line2" :options="totaloptions"></schart>
        </el-card>
      </el-row>
    </div>
</template>

<script>
import Schart from 'vue-schart';
export default {
    name: 'dashboard',
    data() {
        return {
          OrderCountListToday:[],
          OrderTotalSumListToday:[],
          OrderCountList7days:[],
          OrderTotalSumList7days:[],
          counts:[0,0,2],
          sums:[0,0,1],
            count:0,
            sum:0,
            comment:0,
            name: "",
            lastlogintime:"",
            role:0,
            rolestr:'',
            data: [
            ],
          countoptions: {
                type: 'line',
                title: {
                    text: '今日各时段订单量趋势图'
                },
                labels: ['1点', '2点', '3点', '4点', '5点','6点','7点','8点','9点','10点','11点','12点',
                  '13点','14点','15点','16点','17点','18点','19点','20点','21点','22点','23点','24点'],
                datasets: [
                    {
                        label: '订单量',
                        data: [1,1,1]
                    }
                ]
            },
          totaloptions: {
            type: 'line',
            title: {
              text: '今日各时段销售额趋势图'
            },
            labels: ['1点', '2点', '3点', '4点', '5点','6点','7点','8点','9点','10点','11点','12点',
              '13点','14点','15点','16点','17点','18点','19点','20点','21点','22点','23点','24点'],
            datasets: [
              {
                label: '销售额',
                data: [1,1,1]
              }
            ]
          }
        };
    },
    components: {
        Schart
    },
    computed: {
    },
    methods: {
      progressFormat(count){
        return ()=>{
          return count;
        }
      },
        getData(){
          this.axios.get('/DashboardData')
              .then((response) =>{
            if (response.data.flag){
              this.sum=response.data.data.sum;
              this.count=response.data.data.count;
              this.comment=response.data.data.comment;
              this.countoptions.datasets=[
                {
                  label: '订单量',
                  data: response.data.data.OrderCountList24hours
                }
              ] ;
              this.totaloptions.datasets=[
                {
                  label: '销售额',
                  data: response.data.data.OrderTotalSumList24hours
                },
              ]
             this.OrderCountListToday=response.data.data.OrderCountListToday;
              this. OrderTotalSumListToday=response.data.data.OrderTotalSumListToday;
              this.OrderCountList7days=response.data.data.OrderCountList7days;
              this.OrderTotalSumList7days=response.data.data.OrderTotalSumList7days;
            }else {
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
                    localStorage.setItem('rsss_username',this.name);
                    this.role=response.data.data.role;
                    this.lastlogintime=response.data.data.lastlogintime;
                }else {
                    this.$router.push('/login');
                }
            })
                .catch(function (error) {
                    console.log(error);
                });
        }
    },
  created(){
      this.getData();
       this.getUser();
        switch (this.role){
            case 0:
                this.rolestr='超级管理员';
                break;
            case 1:
                this.rolestr='普通管理员';
                break;
            }
    }
};
</script>


<style scoped>
.row{
  margin-bottom: 20px;
}
.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}
.user-info-cont {
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 24px;
    color: #222;
}

.user-info-list span {
    margin-left: 70px;
}
.schart {
    width: 100%;
    height: 300px;
}
</style>
