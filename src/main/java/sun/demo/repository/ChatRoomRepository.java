package sun.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sun.demo.model.ChatRoom;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 사용자가 속한 활성 채팅방 조회 (최신순, 페이징)
    @Query("""
        SELECT DISTINCT cr FROM ChatRoom cr 
        JOIN ChatRoomMember crm ON cr.id = crm.chatRoom.id 
        WHERE crm.user.id = :userId AND crm.isActive = true AND cr.isActive = true
        ORDER BY cr.updatedAt DESC
    """)
    Page<ChatRoom> findUserChatRooms(Long userId, Pageable pageable);

    // 모든 활성 채팅방 조회 (최신순)
    List<ChatRoom> findByIsActiveTrueOrderByCreatedAtDesc();

    // 이름으로 채팅방 검색 (대소문자 무시, 활성 채팅방만)
    List<ChatRoom> findByNameContainingIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(String name);
}
