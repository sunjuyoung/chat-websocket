package sun.demo.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import sun.demo.dto.websocket.ChatMessage;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//메세지 캐시
//중복된 메시지, 받지 않을 메시지 필터링
@Service
public class RedisMessageBroker implements MessageListener {

    /*
    RedisMessageListenerContainer → “이 채널을 구독해라” 관리.
    MessageListener → “메시지가 오면 어떻게 처리할지” 정의.
     */

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisMessageListenerContainer messageListenerContainer;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RedisMessageBroker.class);
    private final String serverId = System.getenv("HOSTNAME") != null
            ? System.getenv("HOSTNAME")
            : "server-" + System.currentTimeMillis();

    private final Map<String, Long> processedMessages = new ConcurrentHashMap<>();
    private final Set<Long> subscribeRooms = ConcurrentHashMap.newKeySet();

    //로컬 새션에 메세지 전달
    private LocalMessageHandler localMessageHandler;

    public RedisMessageBroker(RedisTemplate<String, String> redisTemplate,
                              RedisMessageListenerContainer messageListenerContainer,
                              ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.messageListenerContainer = messageListenerContainer;
        this.objectMapper = objectMapper;
    }

    public String getServerId() {
        return serverId;
    }

    @PostConstruct
    public void initialize() {
        logger.info("Initializing RedisMessageListenerContainer");
        //주기적으로 쓰레드 생성
        // 30초마다 1분 지난 메세지 삭제
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(30000);
                cleanUpProcessedMessages();
            } catch (Exception e) {
                logger.error("Error in initializing RedisMessageListenerContainer", e);
            }
        });
        thread.setDaemon(true);
        thread.setName("redis-broker-cleanup");
        thread.start();
    }

    //서버 중지되는 상황에 메모리 값들을 정리
    //구독중인 채팅방 모두 구독 해지
    @PreDestroy
    public void cleanup() {
        subscribeRooms.forEach(this::unsubscribeFromRoom);
        logger.info("Removing RedisMessageListenerContainer");
    }

    public void setLocalMessageHandler(LocalMessageHandler handler) {
        this.localMessageHandler = handler;
    }

    public void subscribeToRoom(Long roomId) {
        if (subscribeRooms.add(roomId)) {
            ChannelTopic topic = new ChannelTopic("chat.room." + roomId);
            messageListenerContainer.addMessageListener(this, topic);
            logger.info("Subscribed to " + roomId);
        } else {
            logger.error("Room " + roomId + " does not exist");
        }
    }

    public void unsubscribeFromRoom(Long roomId) {
        if (subscribeRooms.remove(roomId)) {
            ChannelTopic topic = new ChannelTopic("chat.room." + roomId);
            messageListenerContainer.removeMessageListener(this, topic);
            logger.info("Unsubscribed from " + roomId);
        } else {
            logger.error("Room " + roomId + " does not exist");
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            String json = new String(message.getBody());
            DistributedMessage distributedMessage = objectMapper.readValue(json, DistributedMessage.class);

        }catch (Exception e) {

        }
    }


    private void cleanUpProcessedMessages() {
        long now = System.currentTimeMillis(); // 현재 시간 밀리초

        // 1분 지난 메세지 삭제
        //processedMessages 들어오는 메시지마다 누적된다
        // 서버가 오래 돌아가면 메모리 문제 발생
        processedMessages.entrySet()
                .removeIf(entry -> now - entry.getValue() > 60000);

        if (!processedMessages.isEmpty()) {
            logger.info("Cleaned processedMessages, remaining size: " + processedMessages.size());
        }
    }

    @FunctionalInterface
    public interface LocalMessageHandler {
        void handle(Long roomId, ChatMessage chatMessage);
    }

    public static class DistributedMessage {
        private String id;
        private String serverId;
        private Long roomId;
        private String excludeSeverId;
        private LocalDateTime timestamp;
        private ChatMessage payload;

        public DistributedMessage() {}

        public DistributedMessage(String id, String serverId, Long roomId,
                                  String excludeSeverId, LocalDateTime timestamp,
                                  ChatMessage payload) {
            this.id = id;
            this.serverId = serverId;
            this.roomId = roomId;
            this.excludeSeverId = excludeSeverId;
            this.timestamp = timestamp;
            this.payload = payload;
        }

        public String getId() { return id; }
        public String getServerId() { return serverId; }
        public Long getRoomId() { return roomId; }
        public String getExcludeSeverId() { return excludeSeverId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public ChatMessage getPayload() { return payload; }
    }
}
