package fun.kwok.rsss.mapper;



import fun.kwok.rsss.bean.AliPaySetting;
import fun.kwok.rsss.config.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@CacheNamespace(implementation= RedisCache.class)
public interface AliPaySettingMapper {

    @Select("select * from AliPaySetting where id=1")
    public AliPaySetting getAliPaySetting();

    @Update("update AliPaySetting SET Appid=#{Appid},Pid=#{Pid},PrivateKey=#{PrivateKey},PublicKey=#{PublicKey},AlipayPublicKey=#{AlipayPublicKey},CallBackDomain=#{CallBackDomain} where id=1")
    public int updateAliPaySetting(AliPaySetting aliPaySetting);
}
