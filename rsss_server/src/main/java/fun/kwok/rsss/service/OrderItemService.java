package fun.kwok.rsss.service;


import fun.kwok.rsss.bean.OrderItem;
import fun.kwok.rsss.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    OrderItemMapper orderItemMapper;
    public List<OrderItem> getOrderItems(Long id){
        return orderItemMapper.getOrderItems(id);
    }
    public int delOrder(Long id){return orderItemMapper.delOrderItem(id);}
    public int addOrderItem(OrderItem orderItem){return orderItemMapper.addOrderItem(orderItem);}
}
