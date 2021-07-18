# RSSS
基于计算机视觉的餐厅自助结算系统  SpringBoot+Vue+MyBatis+MySql+Redis+OpenCV+YOLOv3

## 前端

前端采用Vue+ElementUI

运行方式，修改main.js中的后端地址，并执行：

```bash
npm install
npm run dev
```

## 后端

后端采用SpringBoot+SpringSecurity+MyBatis，数据库为MySQL，Redis作为MyBatis的二级缓存。

图像识别部分使用OpenCV的DNN模块调用Darknet框架所训练出的YOLOv3模型来实现。

运行方式，直接运行即可。数据库连接及后端端口号在application.yaml文件中修改。

首次运行前需首先在后台设置支付宝接口的相关配置。

测试账号：

管理员：

admin 123456

终端机器：

东餐厅一号机 123456

东餐厅二号机 123456

东餐厅三号机 123456

## 改进方向

毕业设计项目，时间较为仓促，很多地方写的不规范。登录态及权限验证采用session，今后将会改为token验证。