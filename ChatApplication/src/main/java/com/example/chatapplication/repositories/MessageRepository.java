package com.example.chatapplication.repositories;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByOrderByCreatedDateDesc(Pageable pageable);

    List<Message> findAllByContentContainingIgnoreCaseOrderByCreatedDateDesc(String content, Pageable pageable);

    Message findTop1ByAccountSenderOrderByCreatedDateDesc(Account account);
}
