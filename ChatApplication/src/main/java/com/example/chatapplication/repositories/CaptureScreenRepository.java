package com.example.chatapplication.repositories;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.CaptureScreen;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaptureScreenRepository extends JpaRepository<CaptureScreen, Long> {

    List<CaptureScreen> findAllByAccountOrderByCreatedDateDesc(Account account, Pageable pageable);

    List<CaptureScreen> findAllByAccountAndCreatedDateAfterOrderByCreatedDateDesc(Account account, LocalDateTime time, Pageable pageable);

}
