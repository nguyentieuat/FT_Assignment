package com.example.chatapplication.services;

import com.example.chatapplication.services.dto.ChatRoomDto;

public interface ChatRomService {

    /**
     * Get chat room by id
     *
     * @param id
     * @return
     */
    ChatRoomDto getChatRoomById(long id);
}
