package sun.demo.dto.chat;

import sun.demo.dto.user.UserDto;
import sun.demo.model.MessageType;

import java.time.LocalDateTime;

public record MessageDto(
        Long id,
        Long chatRoomId,
        UserDto sender,
        MessageType type,
        String content,
        Boolean isEdited,
        Boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime editedAt,
        Long sequenceNumber
) { }