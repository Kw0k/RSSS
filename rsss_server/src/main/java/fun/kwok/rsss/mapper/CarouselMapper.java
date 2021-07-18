package fun.kwok.rsss.mapper;


import fun.kwok.rsss.bean.Carousel;
import fun.kwok.rsss.config.RedisCache;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation= RedisCache.class)
public interface CarouselMapper {

    @Select("SELECT * FROM Carousel")
    public List<Carousel> getAllCarousel();

    @Select("SELECT * FROM Carousel WHERE `status`=1")
    public List<Carousel> getOnCarousel();

    @Select("SELECT COUNT(*) FROM carousel WHERE `status`=#{status}")
    public int getCountByStatus(int status);

    @Select("SELECT `status` FROM carousel WHERE id=#{id}")
    public int getStatusById(Long id);

    @Update("UPDATE carousel SET `status`=#{status} WHERE id=#{id}")
    public int setStatusById(Long id,int status);

    @Delete("DELETE FROM carousel WHERE id=#{id}")
    public int delById(Long id);

    @Insert("INSERT INTO carousel(image) VALUES(#{image})")
    public  int add(String image);
}
