package sun.demo.dto.websocket;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
class ErrorMessage extends WebSocketMessage {
    private String message;
    private String code;




    @Builder
    public ErrorMessage(String message,
                        String code,
                        Long chatRoomId,
                        LocalDateTime timestamp) {
        super(chatRoomId, timestamp != null ? timestamp : LocalDateTime.now());
        this.message = message;
        this.code = code;
    }
}