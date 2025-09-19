package sun.demo.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sun.demo.model.ChatRoomType;

public record CreateChatRoomRequest(
        @NotBlank(message = "채팅방 이름은 필수입니다")
        @Size(min = 1, max = 100, message = "채팅방 이름은 1-100자 사이여야 합니다")
        String name,

        String description,

        @NotNull(message = "채팅방 타입은 필수입니다")
        ChatRoomType type,

        String imageUrl,

        Integer maxMembers // 기본값 100 적용은 아래 compact ctor에서 처리
) {
    public CreateChatRoomRequest {
        if (maxMembers == null || maxMembers <= 0) {
            maxMembers = 100;
        }
    }
}