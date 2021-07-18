package fun.kwok.rsss.mapper;

import fun.kwok.rsss.bean.Comment;
import fun.kwok.rsss.config.RedisCache;
import java.util.List;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@CacheNamespace(implementation = RedisCache.class)
public interface CommentMapper {
    @Select({"Select IFNULL(count(*),0) from Comment where status=0"})
    int getUnReadCommentCount();

    @Select({"SELECT IFNULL(COUNT(*),0) FROM Comment WHERE TO_DAYS(datatime)=TO_DAYS(NOW())"})
    int getCommentCountOfToday();

    @Select({"Select * from Comment where status=0"})
    List<Comment> getUnReadComment();

    @Select({"Select * from Comment where status=1"})
    List<Comment> getReadComment();

    @Update({"update Comment SET status=1 where id=#{id}"})
    int readComment(Long id);

    @Delete({"delete from comment where id=#{id}"})
    int deleteComment(Long id);

    @Insert({"insert into Comment(orderId,content,contact,status,datatime) values(#{orderId},#{content},#{contact},#{status},#{datatime})"})
    int addComment(Comment comment);
}
