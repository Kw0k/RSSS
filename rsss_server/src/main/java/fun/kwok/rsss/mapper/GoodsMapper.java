package fun.kwok.rsss.mapper;

import fun.kwok.rsss.bean.Goods;
import fun.kwok.rsss.config.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
@CacheNamespace(implementation= RedisCache.class)
public interface GoodsMapper {

    @Select("select * from Goods")
    public List<Goods> getGoods();

    @Update("update Goods SET price=#{price} where id=#{id}")
    public int UpdateGoodsPrice(Long id,float price);

    @Select("select * from Goods where id=#{id}")
    public Goods getGoodsById(Long id);
}
