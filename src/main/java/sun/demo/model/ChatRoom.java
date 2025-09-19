package sun.demo.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "chat_rooms",
        indexes = {
                @Index(name = "idx_chat_room_created_by", columnList = "created_by"),
                @Index(name = "idx_chat_room_type", columnList = "type"),
                @Index(name = "idx_chat_room_active", columnList = "is_active")
        }
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChatRoomType type = ChatRoomType.GROUP;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Integer maxMembers = 100;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Users createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    }