package fun.kwok.rsss.controller;

import fun.kwok.rsss.bean.ResultInfo;
import fun.kwok.rsss.service.CommentService;
import fun.kwok.rsss.service.OrderService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})

public class DashboardController {
    @Autowired
    OrderService orderService;

    @Autowired
    CommentService commentService;

    @ResponseBody
    @GetMapping({"/DashboardData"})
    public ResultInfo getDashboardData() {
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("count", Integer.valueOf(this.orderService.getOrderCountOfToday()));
        hashMap.put("sum", Float.valueOf(this.orderService.getTotalSumOfToday()));
        hashMap.put("comment", Integer.valueOf(this.commentService.getCommentCountOfToday()));
        hashMap.put("OrderCountListToday", this.orderService.getOrderCountListOfToday());
        hashMap.put("OrderTotalSumListToday", this.orderService.getTotalSumListOfToday());
        hashMap.put("OrderCountList7days", this.orderService.getOrderCountListOf7days());
        hashMap.put("OrderTotalSumList7days", this.orderService.getTotalSumListOf7days());
        hashMap.put("OrderCountList24hours", this.orderService.getOrderCountListOf24hours());
        hashMap.put("OrderTotalSumList24hours", this.orderService.getTotalSumListOf24hours());
        return new ResultInfo(true, "", hashMap);
    }
}
