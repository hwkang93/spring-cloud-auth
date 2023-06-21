package hwkang.study.auth.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisSessionUserService {

    private final JedisPool jedisPool;

    public SessionUser findById(String sessionKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> stringStringMap = jedis.hgetAll(sessionKey);
            String userId = stringStringMap.get("userId");
            String userName = stringStringMap.get("userName");
            String userSeCode = stringStringMap.get("userSeCode");
            String insttCode = stringStringMap.get("insttCode");

            return new SessionUser(userId, userName, userSeCode, insttCode);
        }
    }
}
