package sun.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sun.demo.model.Users;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE Users u SET u.lastSeenAt = :lastSeenAt WHERE u.id = :userId")
    void updateLastSeenAt(Long userId, LocalDateTime lastSeenAt);

    @Query("SELECT u FROM Users u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.displayName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Users> searchUsers(String query, Pageable pageable);
}
