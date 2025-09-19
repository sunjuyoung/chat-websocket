package sun.demo.dto.chat;

import jakarta.validation.constraints.NotNull;
import sun.demo.model.MessageType;

public record SendMessageRequest(
        @NotNull(message = "채팅방 ID는 필수입니다")
        Long chatRoomId,

        @NotNull(message = "메시지 타입은 필수입니다")
        MessageType type,

        String content
) { }
