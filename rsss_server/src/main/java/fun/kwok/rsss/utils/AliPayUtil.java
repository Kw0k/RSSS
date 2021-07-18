package fun.kwok.rsss.utils;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import fun.kwok.rsss.bean.AliPaySetting;
import fun.kwok.rsss.service.AliPaySettingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class AliPayUtil {
    private static String className = AliPayUtil.class.getName();
    @Autowired
    AliPaySettingService aliPaySettingService;
    private static AliPayUtil aliPayUtil;

    @PostConstruct
           public void init() {
        aliPayUtil = this;
        aliPayUtil.aliPaySettingService = this.aliPaySettingService;
                  }
    private static Log log = LogFactory.getLog(AliPayUtil.class);
    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;
    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;
    private static AlipayMonitorService monitorService;
    // 简单打印应答
    private static void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
    // 当面付2.0退款
    public static boolean trade_refund(String id,String total) {
        AliPaySetting aliPaySetting = aliPayUtil.aliPaySettingService.getAliPaySetting();
        Configs.setAppid(aliPaySetting.getAppid());
        Configs.setPid(aliPaySetting.getPid());
        Configs.setPrivateKey(aliPaySetting.getPrivateKey());
        Configs.setPublicKey(aliPaySetting.getPublicKey());
        Configs.setAlipayPublicKey(aliPaySetting.getAlipayPublicKey());
        Configs.setOpenApiDomain("https://openapi.alipay.com/gateway.do");
        Configs.setMcloudApiDomain("http://mcloudmonitor.com/gateway.do");
        Configs.setSignType("RSA2");
        Configs.setMaxQueryRetry(5);
        Configs.setQueryDuration(5000);
        Configs.setMaxCancelRetry(3);
        Configs.setCancelDuration(2000);
        Configs.setHeartbeatDelay(5);
        Configs.setCancelDuration(900);
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
                .setFormat("json").build();
        boolean flag;
        // (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = id;
        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = total;
        // (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，
        // 对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        String outRequestNo = "";
        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "管理员退款";
        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
        String storeId = "test_store_id";
        // 创建退款请求builder，设置请求参数
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason)
                .setOutRequestNo(outRequestNo).setStoreId(storeId);
        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝退款成功: )");
                flag=true;
                break;

            case FAILED:
                log.error("支付宝退款失败!!!");
                flag=false;
                break;

            case UNKNOWN:
                log.error("系统异常，订单退款状态未知!!!");
                flag=false;
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                flag=false;
                break;
        }
        return  flag;
    }
    // 测试当面付2.0生成支付二维码
    public static boolean trade_precreate(String id,String total,String amount,String url) {
        AliPaySetting aliPaySetting = aliPayUtil.aliPaySettingService.getAliPaySetting();
        Configs.setAppid(aliPaySetting.getAppid());
        Configs.setPid(aliPaySetting.getPid());
        Configs.setPrivateKey(aliPaySetting.getPrivateKey());
        Configs.setPublicKey(aliPaySetting.getPublicKey());
        Configs.setAlipayPublicKey(aliPaySetting.getAlipayPublicKey());
        String CallBackDomain=aliPaySetting.getCallBackDomain();
        Configs.setOpenApiDomain("https://openapi.alipay.com/gateway.do");
        Configs.setMcloudApiDomain("http://mcloudmonitor.com/gateway.do");
        Configs.setSignType("RSA2");
        Configs.setMaxQueryRetry(5);
        Configs.setQueryDuration(5000);
        Configs.setMaxCancelRetry(3);
        Configs.setCancelDuration(2000);
        Configs.setHeartbeatDelay(5);
        Configs.setCancelDuration(900);
        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();
        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
                .setFormat("json").build();
        boolean flag;
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = id;
        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "餐厅自助结算订单付款";
        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = total;
        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品"+amount+"件共"+total+"元";
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";
        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");
        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";
        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(CallBackDomain+"/alipay_callback");
        //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");
                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                String filePath = String.format(url+"/image/alipay/qr-%s.png", response.getOutTradeNo());
                log.info("filePath:" + filePath);
                File file = FileUtil.createFile(filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, file.getPath());
                flag=true;
                break;
            case FAILED:
                log.error("支付宝预下单失败!!!");
                flag=false;
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                flag=false;
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                flag=false;
                break;
        }
        return  flag;
    }
}
