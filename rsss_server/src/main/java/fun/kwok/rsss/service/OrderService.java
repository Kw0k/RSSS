package fun.kwok.rsss.service;

import fun.kwok.rsss.bean.*;
import fun.kwok.rsss.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    UserService userService;

    public List<Order> getOrders(Long userid, Long status, Long id, Long paytype) {
        List<Order> orders = orderMapper.getOrders(userid, status, id, paytype);
        orders.forEach(order -> order.setOrderItems(orderItemService.getOrderItems(order.getId())));
        return orders;
    }

    public int delOrder(Long id) {
        return orderMapper.delOrder(id);
    }

    @Transactional
    public void addOrder(Order order) {
        orderMapper.addOrder(order);
        List<OrderItem> orderItemList = order.getOrderItems();
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItemService.addOrderItem(orderItem);
        }
        orderItemList = orderItemService.getOrderItems(order.getId());
        order.setOrderItems(orderItemList);
    }

    public int updateStatus(int id, int status) {
        return orderMapper.updateStatus(id, status);
    }

    public float getTotalSumOfToday() {
        return orderMapper.getTotalSumOfToday();
    }

    public int getOrderCountOfToday() {
        return orderMapper.getOrderCountOfToday();
    }

    public List<OrderCount> getOrderCountListOfToday() {
        List<OrderCount> list = new ArrayList<>();
        List<User> usersByRole = userService.getUsersByRole(Integer.valueOf(2));
        List<OrderCount> orderCountListOfToday = orderMapper.getOrderCountListOfToday();
        usersByRole.forEach(user -> {
            OrderCount orderCount = new OrderCount();
            orderCount.setCount(Long.valueOf(Long.parseLong("0")));
            orderCountListOfToday.forEach(orderCountList -> {
                if (orderCountList.getId()==user.getId()){
                    orderCount.setCount(orderCountList.getCount());
                }
            });
            orderCount.setId(user.getId());
            orderCount.setUsername(user.getUsername());
            list.add(orderCount);
        });
        list.sort((x, y) -> Long.compare(x.getCount().longValue(), y.getCount().longValue()));
        Collections.reverse(list);
        return list;
    }

    public List<OrderTotalSum> getTotalSumListOfToday() {
        List<OrderTotalSum> list = new ArrayList<>();
        List<User> usersByRole = userService.getUsersByRole(Integer.valueOf(2));
        List<OrderTotalSum> totalSumListOfToday = orderMapper.getTotalSumListOfToday();
        usersByRole.forEach(user -> {
            OrderTotalSum orderTotalSum = new OrderTotalSum();
            orderTotalSum.setSum(0.0F);
            totalSumListOfToday.forEach(totalSum -> {
                if (totalSum.getId()==user.getId()){
                    orderTotalSum.setSum(totalSum.getSum());
                }
            });
            orderTotalSum.setId(user.getId());
            orderTotalSum.setUsername(user.getUsername());
            list.add(orderTotalSum);
        });
        list.sort((x, y) -> Float.compare(x.getSum(), y.getSum()));
        Collections.reverse(list);
        return list;
    }

    public List<OrderCount> getOrderCountListOf7days() {
        List<OrderCount> list = new ArrayList<>();
        List<User> usersByRole = userService.getUsersByRole(Integer.valueOf(2));
        List<OrderCount> orderCountListOf7days = orderMapper.getOrderCountListOf7days();
        usersByRole.forEach(user -> {
            OrderCount orderCount = new OrderCount();
            orderCount.setCount(Long.valueOf(Long.parseLong("0")));
            orderCountListOf7days.forEach(orderCountList ->{
                if (orderCountList.getId()==user.getId()){
                    orderCount.setCount(orderCountList.getCount());
                }
            } );
            orderCount.setId(user.getId());
            orderCount.setUsername(user.getUsername());
            list.add(orderCount);
        });
        list.sort((x, y) -> Long.compare(x.getCount().longValue(), y.getCount().longValue()));
        Collections.reverse(list);
        return list;
    }

    public List<OrderTotalSum> getTotalSumListOf7days() {
        List<OrderTotalSum> list = new ArrayList<>();
        List<User> usersByRole = userService.getUsersByRole(Integer.valueOf(2));
        List<OrderTotalSum> totalSumListOf7days = orderMapper.getTotalSumListOf7days();
        usersByRole.forEach(user -> {
            OrderTotalSum orderTotalSum = new OrderTotalSum();
            orderTotalSum.setSum(0.0F);
            totalSumListOf7days.forEach(totalSum -> {
                if (totalSum.getId()==user.getId()){
                    orderTotalSum.setSum(totalSum.getSum());
                }
            });
            orderTotalSum.setId(user.getId());
            orderTotalSum.setUsername(user.getUsername());
            list.add(orderTotalSum);
        });
        list.sort((x, y) -> Float.compare(x.getSum(), y.getSum()));
        Collections.reverse(list);
        return list;
    }

    public Long[] getOrderCountListOf24hours() {
        Long[] longs = new Long[24];
        for (int i = 0; i < 24; i++)
            longs[i] = Long.valueOf(Long.parseLong("0"));
        List<OrderCount> orderCountListOf24hours = orderMapper.getOrderCountListOf24hours();
        orderCountListOf24hours.forEach(orderCount ->
                longs[orderCount.getId().intValue()] = orderCount.getCount()
        );
        return longs;
    }

    public List<Order> getTestOrder(){
        return orderMapper.getTestOrder();
    }
    public float[] getTotalSumListOf24hours() {
        float[] floats = new float[24];
        for (int i = 0; i < 24; i++)
            floats[i] = 0.0F;
        List<OrderTotalSum> totalSumListOf24hours = orderMapper.getTotalSumListOf24hours();
        totalSumListOf24hours.forEach(orderTotalSum ->
                floats[orderTotalSum.getId().intValue()] = orderTotalSum.getSum()
        );
        return floats;
    }
}
