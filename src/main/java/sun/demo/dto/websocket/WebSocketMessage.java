package sun.demo.dto.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChatMessage.class, name = "CHAT_MESSAGE"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ERROR")
})
@NoArgsConstructor
public abstract class WebSocketMessage {
    private Long chatRoomId;
    private LocalDateTime timestamp;

    protected WebSocketMessage(Long chatRoomId, LocalDateTime timestamp) {
        this.chatRoomId = chatRoomId;
        this.timestamp = timestamp;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}