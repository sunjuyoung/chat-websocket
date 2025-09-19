package sun.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
    /*
    캐시 이름마다 다른 ttl 설정 가능
    users: 1시간
    chatRooms: 15분
    chatRoomMembers: 10분
    messages: 5분
    기본 ttl: 30분

    null 값 캐싱 안함
    key는 문자열 직렬화.
    value는 JSON 직렬화.
     */

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(objectMapper)))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(configuration)
                .withCacheConfiguration("users", configuration.entryTtl(Duration.ofHours(1)))         // 사용자 정보 1시간
                .withCacheConfiguration("chatRooms", configuration.entryTtl(Duration.ofMinutes(15)))  // 채팅방 정보 15분
                .withCacheConfiguration("chatRoomMembers", configuration.entryTtl(Duration.ofMinutes(10))) // 멤버 정보 10분
                .withCacheConfiguration("messages", configuration.entryTtl(Duration.ofMinutes(5)))    // 메시지 5분
                .build();
    }
}