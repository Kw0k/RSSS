package fun.kwok.rsss.config;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RedisCache implements Cache {

    private static final Logger LOG = LoggerFactory.getLogger(RedisCache.class);
    private static final int DEFAULT_REDIS_EXPIRE = 10;
    private static RedisTemplate<String, Object> redisTemplate = null;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private String id = null;
    public RedisCache(final String id) {
        if (null == id) {
            throw new IllegalArgumentException("MybatisRedisCache Instance Require An Id...");
        }
        LOG.info("MybatisRedisCache: " + id);
        this.id = id;
    }
    @Override
    public String getId() {
        return this.id;
    }
    @Override
    public void putObject(Object key, Object value) {
        if (null != value) {
            LOG.info("putObject key: " + key.toString());
            redisTemplate.opsForValue().set(key.toString(), value, DEFAULT_REDIS_EXPIRE, TimeUnit.MINUTES);
        }
    }
    @Override
    public Object getObject(Object key) {
        try {
            if (null != key) {
                LOG.info("getObject key: " + key.toString());
                return redisTemplate.opsForValue().get(key.toString());
            }
        } catch (Exception e) {
            LOG.error("getFromRedis: " + key.toString() + " failed!");
        }
        LOG.info("getObject null...");
        return null;
    }
    @Override
    public Object removeObject(Object keyObject) {
        if (null != keyObject) {
            redisTemplate.delete(keyObject.toString());
        }
        return null;
    }
    @Override
    public void clear() {
        LOG.info("clear......");
        try {
            Set<String> keys = redisTemplate.keys("*:" + this.id + "*");
            LOG.info("keys size: " + keys.size());
            for (String key : keys) {
                LOG.info("key : " + key);
            }
            if (!CollectionUtils.isEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            LOG.error("clear failed!", e);
        }
    }
    @Override
    public int getSize() {
        Long size = (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
        LOG.info("getSize: " + size.intValue());
        return size.intValue();
    }
    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisCache.redisTemplate = redisTemplate;
    }
}
