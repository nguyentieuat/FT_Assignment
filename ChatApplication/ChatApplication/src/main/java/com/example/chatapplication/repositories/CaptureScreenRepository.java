package com.example.chatapplication.repositories;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.CaptureScreen;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaptureScreenRepository extends JpaRepository<CaptureScreen, Long> {

    List<CaptureScreen> findAllByAccountOrderByCreatedDateDesc(Account account, Pageable pageable);

    List<CaptureScreen> findAllByAccountAndCreatedDateAfterOrderByCreatedDateDesc(Account account, LocalDateTime time, Pageable pageable);

    @Query(value="SELECT * FROM capture_screen cc " +
            "where cc.created_by LIKE ?1 and cc.id < ?2 and cc.created_date < ?4 " +
            "ORDER BY cc.created_date DESC " +
            "LIMIT ?5 OFFSET ?3", nativeQuery = true)
    List<CaptureScreen> findAllByUsernameAndCreatedDateAfterOrderByCreatedDateDesc(String username, long lastId, int page, String createDateStr, int pageSize);
}
