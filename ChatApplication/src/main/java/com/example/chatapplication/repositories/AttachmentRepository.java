package com.example.chatapplication.repositories;

import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.domain.ChatRoom;
import com.example.chatapplication.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List findAllByMessage(Message message);

    @Query(value = "Select count(*) from chat_application.attachment att\n" +
            "Where DATE (att.created_date) = DATE (:date) and att.created_by = :username", nativeQuery = true)
    Integer countRecordCreatedInDateByUser(LocalDateTime date, String username);

    List<Attachment> findAllByChatRoomOrChatRoomIsNullOrderByCreatedDateDesc(ChatRoom chatroom,Pageable pageable);

    void deleteAllByMessage(Message messageId);
}
