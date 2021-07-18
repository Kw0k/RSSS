package fun.kwok.rsss.mapper;

import fun.kwok.rsss.bean.OrderItem;
import fun.kwok.rsss.config.RedisCache;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation= RedisCache.class)
public interface OrderItemMapper {

    @Select("select o.id, o.price,o.goods_id,o.order_id,o.amount,  g.name goods_name from orderItem AS o,Goods as g where o.order_id=#{id} and o.goods_id=g.id")
    public List<OrderItem> getOrderItems(Long id);


    @Delete("DELETE FROM OrderItem WHERE order_id=#{id}")
    public int delOrderItem(Long id);

    @Insert("insert into OrderItem(price,amount,goods_id,order_id)values(#{price},#{amount},#{goodsId},#{orderId})")
    public int addOrderItem(OrderItem orderItem);
}
