package sun.demo.dto.chat;

import java.util.List;

public record MessagePageResponse(
        List<MessageDto> messages,
        Long nextCursor,
        Long prevCursor,
        boolean hasNext,
        boolean hasPrev
) { }
