package fun.kwok.rsss.bean;

import lombok.Data;

@Data
public class OrderItem {
    private Long id;
    private float price;
    private Integer amount;
    private Long goodsId;
    private String goodsName;
    private Long orderId;

    public OrderItem(float price, Integer amount, Long goodsId) {
        this.price = price;
        this.amount = amount;
        this.goodsId = goodsId;
    }
}
