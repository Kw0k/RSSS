package fun.kwok.rsss.mapper;

import fun.kwok.rsss.bean.Activity;
import fun.kwok.rsss.config.RedisCache;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
@CacheNamespace(implementation = RedisCache.class)
public interface ActivityMapper {
    @Select({"Select * from activity"})
    List<Activity> getAllActivity();

    @Update({"UPDATE activity SET `status`=#{status} WHERE id=#{id}"})
    int setStatusById(Long id, int status);

    @Delete({"DELETE FROM activity WHERE id=#{id}"})
    int delById(Long id);

    @Insert({"insert into Activity(type,title,leastTotal,discount,starttime,endtime,count,countType) values(#{type},#{title},#{leastTotal},#{discount},#{starttime},#{endtime},#{count},#{countType})"})
    int addActivity(Activity activity);
    @Select({"SELECT * FROM activity WHERE `status`=1 AND starttime<=NOW() AND endtime>=NOW() AND leastTotal<=#{Total} AND count>0 ORDER BY discount DESC"})
    List<Activity> getActivityByOrderTotal(float Total);
    @Update({"UPDATE activity set count=count-1 where id=#{id}"})
    int reduceCount(Long id);
}
