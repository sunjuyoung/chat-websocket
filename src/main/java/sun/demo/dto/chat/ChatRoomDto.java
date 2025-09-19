package sun.demo.dto.chat;


import sun.demo.dto.user.UserDto;
import sun.demo.model.ChatRoomType;

import java.time.LocalDateTime;

public record ChatRoomDto(
        Long id,
        String name,
        String description,
        ChatRoomType type,
        String imageUrl,
        Boolean isActive,
        Integer maxMembers,
        Integer memberCount,
        UserDto createdBy,
        LocalDateTime createdAt,
        MessageDto lastMessage
) { }

