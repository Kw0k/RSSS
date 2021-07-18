package fun.kwok.rsss.mapper;

import fun.kwok.rsss.bean.User;
import fun.kwok.rsss.config.RedisCache;
import org.apache.ibatis.annotations.*;

import java.util.List;

@CacheNamespace(implementation= RedisCache.class)
@Mapper
public interface UserMapper {
    @Select("select id,username,lastlogintime,role from user where id=#{id}")
    public User getUser(Long id);
    @Select("Select * from user where username=#{username}")
    public User getUserByName(String username);

    @Select("select id,username,lastlogintime,role from user where role=#{role}")
    public List<User> getUsersByRole(Integer role);

    @Select("select * from user where username=#{username} and password=#{password}")
    public User getUserByLogin(String username,String password);

    @Update("update user set lastlogintime=#{lastlogintime} where id=#{id}")
    public int UpdatLastLoginTime(User user);

    @Update("<script>"+"update user set username=#{username}," +
            "<if test='password!=null'>password=#{password},</if>"+
            "role=#{role} where id=#{id}"
            +"</script>")
    public int UpdatUser(User user);

    @Select("select id,username,lastlogintime,role from User")
    public List<User> getAllUser();

    @Delete("DELETE FROM user where id=#{id}")
    public int delUser(Long id);

    @Insert("INSERT INTO user(username,password,role) VALUES(#{username},#{password},#{role})")
    public int addUser(User user);
}
