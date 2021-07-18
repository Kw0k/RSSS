<template>
  <el-container class="el-container">
  <el-form  label-position="top" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="form demo-ruleForm">
    <el-form-item label="订单编号(支付宝订单记录中的商家订单号)" prop="orderId">
      <el-input size="large" type="number" v-model="ruleForm.orderId"></el-input>
    </el-form-item>
    <el-form-item  label="联系方式" prop="contact">
      <el-input size="large" v-model="ruleForm.contact"></el-input>
    </el-form-item>
    <el-form-item label="投诉建议"  prop="content">
      <el-input size="large" type="textarea" :rows="5" v-model="ruleForm.content"></el-input>
    </el-form-item>
      <el-button size="large" class="button" type="primary" :loading.sync="btnLoading" @click="submitForm('ruleForm')">提交</el-button>
  </el-form>
  </el-container>
</template>
<script>
import axios from "axios";
export default {
  data() {
    return {
      btnLoading:false,
      ruleForm: {
        orderId:"",
        contact:"",
        content:"",
      },
      rules: {
        content: [
          { required: true, message: '请填写投诉建议', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.btnLoading=true;
          axios.post(
              '/addComment',
              this.qs.stringify({
                orderId: this.ruleForm.orderId,
                contact: this.ruleForm.contact,
                content: this.ruleForm.content
              })
          ).then((response) =>{
            this.btnLoading=false;
            if (response.data.flag){
              this.$message.success(response.data.msg);
            }else {
              this.$message.error(response.data.msg);
            }
          })
              .catch(function (error) {
                this.btnLoading=false;
                console.log(error);
              });


        } else {
          return false;
        }
      });
    }
  }
}
</script>

<style scoped>
.button{
  width: 100%;
}
.form{
  width: 100%;
}
.el-container{
  padding-left: 10px;
  padding-right: 20px;
  padding-top: 20px;
}
</style>
