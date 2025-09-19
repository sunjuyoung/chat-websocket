package sun.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sun.demo.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // 일반 페이징 (페이지 번호 기반) - 최신 메시지부터
    @Query("""
        SELECT m FROM Message m 
        JOIN FETCH m.sender s
        JOIN FETCH m.chatRoom cr
        WHERE m.chatRoom.id = :chatRoomId AND m.isDeleted = false 
        ORDER BY m.sequenceNumber DESC, m.createdAt DESC
    """)
    Page<Message> findByChatRoomId(Long chatRoomId, Pageable pageable);

    // 커서 기반 페이지네이션 - 이전 메시지들 (과거 방향)
    @Query("""
        SELECT m FROM Message m 
        JOIN FETCH m.sender s
        JOIN FETCH m.chatRoom cr
        WHERE m.chatRoom.id = :chatRoomId 
        AND m.isDeleted = false 
        AND m.id < :cursor
        ORDER BY m.sequenceNumber DESC, m.createdAt DESC
    """)
    List<Message> findMessagesBefore(Long chatRoomId, Long cursor, Pageable pageable);

    // 커서 기반 페이지네이션 - 이후 메시지들 (최신 방향)
    @Query("""
        SELECT m FROM Message m 
        JOIN FETCH m.sender s
        JOIN FETCH m.chatRoom cr
        WHERE m.chatRoom.id = :chatRoomId 
        AND m.isDeleted = false 
        AND m.id > :cursor
        ORDER BY m.sequenceNumber ASC, m.createdAt ASC
    """)
    List<Message> findMessagesAfter(Long chatRoomId, Long cursor, Pageable pageable);

    // 최신 메시지들 (커서 없을 때)
    @Query("""
        SELECT m FROM Message m 
        JOIN FETCH m.sender s
        JOIN FETCH m.chatRoom cr
        WHERE m.chatRoom.id = :chatRoomId 
        AND m.isDeleted = false 
        ORDER BY m.sequenceNumber DESC, m.createdAt DESC
    """)
    List<Message> findLatestMessages(Long chatRoomId, Pageable pageable);

    // 네이티브 쿼리로 최신 메시지 1개 조회 (캐시 가능)
    @Query(value = """
        SELECT * FROM messages m 
        WHERE m.chat_room_id = :chatRoomId AND m.is_deleted = false 
        ORDER BY m.sequence_number DESC, m.created_at DESC 
        LIMIT 1
    """, nativeQuery = true)
    Optional<Message> findLatestMessage(Long chatRoomId);
}
