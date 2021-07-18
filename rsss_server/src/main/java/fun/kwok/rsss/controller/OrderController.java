package fun.kwok.rsss.controller;


import fun.kwok.rsss.bean.*;
import fun.kwok.rsss.service.ActivityService;
import fun.kwok.rsss.service.GoodsService;
import fun.kwok.rsss.service.OrderItemService;
import fun.kwok.rsss.service.OrderService;
import fun.kwok.rsss.utils.AliPayUtil;
import fun.kwok.rsss.utils.DarknetUtil;
import fun.kwok.rsss.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})

public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ActivityService activityService;
    @Autowired
    GoodsService goodsService;

    @ResponseBody
    @PostMapping("/Order")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getOrders(
            @RequestParam(value = "userid",required = false) Long userid,
            @RequestParam(value = "status",required = false) Long status,
            @RequestParam(value = "id",required = false) Long id,
            @RequestParam(value = "paytype",required = false) Long paytype
            ){
        List<Order> orders=orderService.getOrders(userid, status, id,paytype);
        int tatal=orders.size();
        if (tatal>0)
            return new ResultInfo(true,"success",tatal,orders);
        else
            return new ResultInfo(false,"订单列表为空",orders);
    }

    @ResponseBody
    @DeleteMapping("/order/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo delOrder(@PathVariable("id") Long id){
        if (orderItemService.delOrder(id)>0)
            if (orderService.delOrder(id)>0)
                return new ResultInfo(true,"删除成功",null);
        return new ResultInfo(false,"删除失败",null);
    }


    @ResponseBody
    @PostMapping("/orderUpload")
    public ResultInfo uploadImgToAddOrder(@RequestParam("image") String imgBase64, HttpSession httpSession){
        httpSession.setAttribute("order",null);
        String fileName=new Date().getTime()+(Math.random()*1000+"")+".png";
        String filePath="static/upload/"+fileName;
       imgBase64=imgBase64.replace("data:image/png;base64,","");
       Boolean flag=ImageUtil.GenerateImage(imgBase64,filePath);
       int[] resultInt=null;
       Order order=new Order();
       List<OrderItem> orderItemList=new ArrayList<OrderItem>();
       int goodsId[]=new int[15];
       if (flag) {
           resultInt = DarknetUtil.IdentifyImage(filePath);
       }
       if (resultInt!=null&&resultInt.length>0){
           for (int i = 0; i <resultInt.length ; i++) {
               goodsId[resultInt[i]+1]++;
           }
           for (int i = 1; i <goodsId.length ; i++) {
               if (goodsId[i]!=0)
               orderItemList.add(new OrderItem(goodsService.getGoodsById(new Long((long)i)).getPrice(),goodsId[i],new Long((long)i)));
           }
           float total=0;
           for (OrderItem orderItem:orderItemList
                ) {
               total+=orderItem.getPrice()*orderItem.getAmount();
           }
           order.setAmount(resultInt.length);
           order.setTotal(total);
           order.setOrderItems(orderItemList);
           order.setStatus(1);
           order.setPaytype(1);
           order.setDatatime(new Date());
           User user= (User) httpSession.getAttribute("user");
           order.setUserId(user.getId());
           order.setImage("/upload/"+fileName);
           order.setActivityComment("无");
                   List<Activity> activities = activityService.getActivityByOrderTotal(order.getTotal());
           for (int j = 0; j < activities.size(); j++) {
               Activity activity = activities.get(j);
               if (activity.getCountType() == 1) {
                   order.setTotal(order.getTotal() - activity.getDiscount());
                   order.setActivityComment(activity.getTitle());
                   break;
               }
               int result = activityService.reduceCount(activity.getId());
               if (result > 0) {
                   order.setTotal(order.getTotal() - activity.getDiscount());
                   order.setActivityComment(activity.getTitle());
                   break;
               }
           }
           try{
               orderService.addOrder(order);
           }catch (Exception e){
               return new ResultInfo(false,"生成订单失败",null);
           }
               httpSession.setAttribute("order",order);
           return new ResultInfo(true,"识别成功",order);
       }else {
           return new ResultInfo(false,"识别失败",null);
       }
    }
    @ResponseBody
    @GetMapping("/aliPay")
    public ResultInfo aliPay(HttpSession session){
        Order order= (Order) session.getAttribute("order");
        if (order==null)
        return new ResultInfo(false,"",null);
        boolean b = AliPayUtil.trade_precreate(order.getId() + "", order.getTotal() + "",order.getAmount()+"", "static");
        if (b)
            return new ResultInfo(b,"",order);
        else
            return new ResultInfo(b,"生成支付宝订单失败，请重试",null);
    }
    @ResponseBody
    @GetMapping("/getOrder")
    public ResultInfo getRecentOrder(HttpSession httpSession){
        Order order=(Order) httpSession.getAttribute("order");
        if (order!=null)
            return new ResultInfo(true,"",order);
        else
            return new ResultInfo(false,"未找到订单信息",null);
    }
    @ResponseBody
    @GetMapping("/getOrderStatus")
    public ResultInfo getOrderStatus(HttpSession httpSession){
        Order order=(Order) httpSession.getAttribute("order");
        if (order!=null){
            List<Order> orders = orderService.getOrders(null, null, order.getId(), null);
            if (orders.size()>0)
                order=orders.get(0);
            else
                return new ResultInfo(false,"",null);
            httpSession.setAttribute("order",order);
            return new ResultInfo(true,"",order);
        } else
            return new ResultInfo(false,"",null);
    }

    @ResponseBody
    @PostMapping("/orderRefund")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo orderRefund(@RequestParam("id") Long id
    ){
        List<Order> orders = orderService.getOrders(null, 2L, id, null);
        Order order=null;
        if (orders.size()>0)
            order = orders.get(0);
        else
            return new ResultInfo(false,"非法请求",null);
        if (AliPayUtil.trade_refund(id+"",order.getTotal()+"")){
            orderService.updateStatus(Integer.parseInt(id + ""), 3);
            return new ResultInfo(true,"退款成功",null);
        }else {
            return new ResultInfo(false,"退款失败",null);
        }

    }
}
