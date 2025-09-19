package sun.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "chat_room_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"chat_room_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_chat_room_member_user_id", columnList = "user_id"),
                @Index(name = "idx_chat_room_member_chat_room_id", columnList = "chat_room_id"),
                @Index(name = "idx_chat_room_member_active", columnList = "is_active"),
                @Index(name = "idx_chat_room_member_role", columnList = "role")
        }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role = MemberRole.MEMBER;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column
    private Long lastReadMessageId;

    @Column(nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Column
    private LocalDateTime leftAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}