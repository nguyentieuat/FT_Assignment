package com.example.chatapplication.services;

import com.example.chatapplication.services.dto.MessageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    List<MessageDto> getAllMessage(Pageable pageable);

    MessageDto saveMessage(MessageDto messageDto);

    MessageDto findByMessageId(long l);

    void deleteMessageByID(long idMessage);

    List<MessageDto> findByContent(String keySearch, Pageable pageable);
}
