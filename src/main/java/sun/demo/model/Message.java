package sun.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "messages",
        indexes = {
                @Index(name = "idx_message_chat_room_id", columnList = "chat_room_id"),
                @Index(name = "idx_message_sender_id", columnList = "sender_id"),
                @Index(name = "idx_message_created_at", columnList = "created_at"),
                @Index(name = "idx_message_room_time", columnList = "chat_room_id,created_at"),
                @Index(name = "idx_message_room_sequence", columnList = "chat_room_id,sequence_number")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Users sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType type = MessageType.TEXT;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean isEdited = false;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false)
    private Long sequenceNumber = 0L;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime editedAt;

}