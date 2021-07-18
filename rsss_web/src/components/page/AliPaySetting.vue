<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-s-tools"></i> 系统设置
                </el-breadcrumb-item>
                <el-breadcrumb-item>支付宝设置</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="form" label-width="100px">
                    <el-form-item label="Appid">
                        <el-input v-model="form.appid"></el-input>
                    </el-form-item>
                    <el-form-item label="pid">
                        <el-input v-model="form.pid"></el-input>
                    </el-form-item>
                    <el-form-item label="RSA私钥">
                        <el-input type="textarea" rows="5" v-model="form.privateKey"></el-input>
                    </el-form-item>
                    <el-form-item label="公钥">
                        <el-input type="textarea" rows="5" v-model="form.publicKey"></el-input>
                    </el-form-item>
                    <el-form-item label="支付宝公钥">
                        <el-input type="textarea" rows="5" v-model="form.alipayPublicKey"></el-input>
                    </el-form-item>
                    <el-form-item label="回调域名">
                        <el-input v-model="form.callBackDomain"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit">保存</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'baseform',
    data() {
        return {
            form: {
                appid: '',
                aid: '',
                privateKey: '',
                publicKey: '',
                alipayPublicKey: '',
                callBackDomain: ''
            }
        };
    },
    methods: {
        onSubmit() {
            axios.post(
                '/AliPaySetting',
                this.qs.stringify({
                    appid: this.form.appid,
                    pid: this.form.pid,
                    privateKey:this.form.privateKey,
                    publicKey:this.form.publicKey,
                    alipayPublicKey:this.form.alipayPublicKey,
                    callBackDomain:this.form.callBackDomain,
                })
            ).then((response) =>{
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
        getAliPaySetting(){
            axios.get(
                '/AliPaySetting')
                .then((response) =>{
                    if (response.data.flag){
                        this.form=response.data.data;
                    }else {
                        this.$message.error('获取设置失败');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    },
    mounted() {
        this.getAliPaySetting();
    }
};
</script>
