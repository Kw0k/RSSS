package fun.kwok.rsss.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import fun.kwok.rsss.bean.AliPaySetting;
import fun.kwok.rsss.bean.ResultInfo;
import fun.kwok.rsss.service.AliPaySettingService;
import fun.kwok.rsss.service.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})
public class AliPayController {
    private static Log logger = LogFactory.getLog(AliPayController.class);
    @Autowired
    OrderService orderService;

    @Autowired
    AliPaySettingService aliPaySettingService;

    @ResponseBody
    @GetMapping("/AliPaySetting")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getAliPaySetting(){
        AliPaySetting aliPaySetting = aliPaySettingService.getAliPaySetting();
        if (aliPaySetting!=null)
            return new ResultInfo(true,"",aliPaySetting);
        else
            return new ResultInfo(false,"获取设置信息失败",null);

    }
    @PostMapping("/AliPaySetting")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResultInfo updateAliPaySetting(AliPaySetting aliPaySetting){
        if (aliPaySettingService.updateAliPaySetting(aliPaySetting)>0)
            return new ResultInfo(true,"保存成功",null);
        else
            return new ResultInfo(false,"保存失败",null);
    }
    @ResponseBody
    @RequestMapping("/alipay_callback")
    public String AliPayCallBack(
            HttpServletRequest request
    ){
        Map<String,String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){

                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8", Configs.getSignType());
            if(!alipayRSACheckedV2){
                return "非法请求，已被记录！";
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
            return "非法请求，已被记录！";
        }
        if("TRADE_SUCCESS".equals(params.get("trade_status"))){
            orderService.updateStatus(Integer.parseInt(params.get("out_trade_no")),2);
        }
        System.out.println(params.get("out_trade_no")+":"+params.get("trade_status"));
        return "success";
    }
}
