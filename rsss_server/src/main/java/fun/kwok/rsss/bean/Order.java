package fun.kwok.rsss.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Order {
    private Long id;
    private float total;
    private Integer amount;
    private Integer status; //1未付款 2已付款 3已退款
    private Integer paytype;//1支付宝 2微信
    private Long userId;
    private String image;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date datatime;
    private String userName;
    private List<OrderItem> orderItems;
    private String activityComment;
}
