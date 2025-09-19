package sun.demo.dto.chat;

// 커서 기반 페이지네이션 요청/응답
public record MessagePageRequest(
        Long chatRoomId,
        Long cursor,
        Integer limit,
        MessageDirection direction
) {
    public MessagePageRequest {
        if (limit == null || limit <= 0) limit = 50;
        if (direction == null) direction = MessageDirection.BEFORE;
    }
}