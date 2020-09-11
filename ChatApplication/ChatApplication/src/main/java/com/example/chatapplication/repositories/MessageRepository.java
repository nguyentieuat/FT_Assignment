package com.example.chatapplication.repositories;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByOrderByCreatedDateDesc(Pageable pageable);

    List<Message> findAllByContentContainingIgnoreCaseOrderByCreatedDateDesc(String content, Pageable pageable);

    Message findTop1ByAccountSenderOrderByCreatedDateDesc(Account account);

    List<Message> findAllByAccountSenderOrderByCreatedDateDesc(Account account, Pageable pageable);


    List<Message> findAllByAccountSenderAndContentContainingIgnoreCaseOrderByCreatedDateDesc(Account account,String content, Pageable pageable);

    @Query(value="SELECT * FROM message m WHERE m.content LIKE ?2 AND m.id < ?1 ORDER BY m.created_date DESC limit ?4 offset ?3", nativeQuery = true)
    List<Message> findAllByContentContainingIgnoreCaseOrderByCreatedDateDesc(long lastId, String keySearch, int i, int pageSize);

    @Query(value="SELECT * FROM message m WHERE m.sender like ?1  AND m.content LIKE ?4 AND m.id < ?2 ORDER BY m.created_date DESC limit ?5 offset ?3", nativeQuery = true)
    List<Message> findAllByUsernameAndCreatedDateAfterOrderByCreatedDateDesc(String username, long lastId, int i, String keySearch, int pageSize);
}
