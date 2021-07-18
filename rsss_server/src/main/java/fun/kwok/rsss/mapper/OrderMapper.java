package fun.kwok.rsss.mapper;


import fun.kwok.rsss.bean.Order;
import fun.kwok.rsss.bean.OrderCount;
import fun.kwok.rsss.bean.OrderTotalSum;
import fun.kwok.rsss.config.RedisCache;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation= RedisCache.class)
public interface OrderMapper {

    @Select("<script>"+"SELECT * FROM `order`,User where `order`.user_id=User.id"
            +"<if test='userid!=null'> and `order`.user_id=#{userid} </if>"
            +"<if test='status!=null'> and `order`.status=#{status} </if>"
            +"<if test='id!=null'> and `order`.id=#{id} </if>"
            +"<if test='paytype!=null'> and `order`.paytype=#{paytype} </if>"
            +"</script>"
    )
    public List<Order> getOrders(@Param("userid") Long userid,@Param("status") Long status,@Param("id") Long id,@Param("paytype") Long paytype);

    @Select("Select id from `order` where activityComment='测试数据'")
    public List<Order> getTestOrder();

    @Delete({"DELETE FROM `order` where id=#{id}"})
    int delOrder(Long id);

    @Insert({"insert into `order`(total,amount,image,status,paytype,user_id,datatime,activityComment) values(#{total},#{amount},#{image},#{status},#{paytype},#{userId},#{datatime},#{activityComment})"})
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addOrder(Order order);

    @Update({"update `order` SET status=#{status} where id=#{id}"})
    int updateStatus(int id, int status);

    @Select({"SELECT IFNULL(SUM(total),0) FROM `Order` WHERE `status`=2 AND TO_DAYS(datatime)=TO_DAYS(NOW())"})
    float getTotalSumOfToday();

    @Select({"SELECT IFNULL(COUNT(*),0) FROM `Order` WHERE `status`=2 AND TO_DAYS(datatime)=TO_DAYS(NOW())"})
    int getOrderCountOfToday();

    @Select({"SELECT user_id as id,IFNULL(COUNT(*),0) as count FROM `Order` WHERE `status`=2 AND TO_DAYS(datatime)=TO_DAYS(NOW()) GROUP BY user_id"})
    List<OrderCount> getOrderCountListOfToday();

    @Select({"SELECT user_id as id,IFNULL(truncate(SUM(total),2),0) as sum FROM `Order` WHERE `status`=2 AND TO_DAYS(datatime)=TO_DAYS(NOW()) GROUP BY user_id"})
    List<OrderTotalSum> getTotalSumListOfToday();

    @Select({"SELECT user_id as id,IFNULL(COUNT(*),0) as count FROM `Order` WHERE `status`=2 AND DATE_SUB(CURDATE(),INTERVAL 7 DAY)<=DATE(datatime) GROUP BY user_id"})
    List<OrderCount> getOrderCountListOf7days();

    @Select({"SELECT user_id as id,IFNULL(truncate(SUM(total),2),0) as sum FROM `Order` WHERE `status`=2 AND DATE_SUB(CURDATE(),INTERVAL 7 DAY)<=DATE(datatime) GROUP BY user_id"})
    List<OrderTotalSum> getTotalSumListOf7days();

    @Select({"SELECT HOUR(datatime) as id,IFNULL(COUNT(*),0) as count FROM `Order` WHERE `status`=2 AND TO_DAYS(datatime)=TO_DAYS(NOW())  GROUP BY HOUR(datatime)"})
    List<OrderCount> getOrderCountListOf24hours();

    @Select({"SELECT HOUR(datatime) as id,IFNULL(truncate(SUM(total),2),0) as sum FROM `Order` WHERE `status`=2 AND TO_DAYS(datatime)=TO_DAYS(NOW())  GROUP BY HOUR(datatime)"})
    List<OrderTotalSum> getTotalSumListOf24hours();
}
