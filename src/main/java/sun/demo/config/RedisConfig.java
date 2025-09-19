package sun.demo.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.Executors;

@Configuration
public class RedisConfig {

    @Bean("distributedObjectMapper")
    public ObjectMapper distributedObjectMapper() {
        //날짜를 숫자(타임스탬프)가 아니라 "yyyy-MM-ddTHH:mm:ss" 형식의 문자열로 변환.
        // 1751027620000 -> "2025-06-27T11:47:00"
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // JSON에 정의되지 않은 필드가 있어도 역직렬화 오류 발생 X.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        //메시지를 처리할 때 스레드풀(캐시 스레드풀) 사용.
        //각 스레드 이름을 redis-message-listener-container-<timestamp> 로 지정.
        container.setTaskExecutor(Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("redis-message-listener-container-" + System.currentTimeMillis());
            thread.setDaemon(true); // 데몬 스레드로 설정 (애플리케이션 종료 시 자동 종료)

            return thread;
        }));
        container.setErrorHandler(t -> {
            System.out.println("Redis Message Listener Error: " + t);
            t.printStackTrace();
        });
        return container;
    }
}