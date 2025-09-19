package sun.demo.dto.user;

import sun.demo.model.Users;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String username,
        String displayName,
        String profileImageUrl,
        String status,
        Boolean isActive,
        LocalDateTime lastSeenAt,
        LocalDateTime createdAt
) {

    static public UserDto fromEntity(Users user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getProfileImageUrl(),
                user.getStatus(),
                user.getIsActive(),
                user.getLastSeenAt(),
                user.getCreatedAt()
        );
    }
}