package sun.demo.dto.websocket;

import lombok.*;
import sun.demo.model.MessageType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends WebSocketMessage {
    private Long id;
    private String content;
    private MessageType type;
    private Long senderId;
    private String senderName;
    private Long sequenceNumber;



    @Builder
    public ChatMessage(Long id,
                       String content,
                       MessageType type,
                       Long senderId,
                       String senderName,
                       Long sequenceNumber,
                       Long chatRoomId,
                       LocalDateTime timestamp) {
        super(chatRoomId, timestamp != null ? timestamp : LocalDateTime.now());
        this.id = id;
        this.content = content;
        this.type = type;
        this.senderId = senderId;
        this.senderName = senderName;
        this.sequenceNumber = sequenceNumber;
    }
}