package com.example.chatapplication.services;

import com.example.chatapplication.services.dto.ChatRoomDto;
import org.springframework.data.domain.Pageable;

public interface ChatRomService {

    /**
     * Get chat room by id
     *
     * @param id
     * @param pageable
     * @return
     */
    ChatRoomDto getChatRoomById(long id, Pageable pageable);
}
