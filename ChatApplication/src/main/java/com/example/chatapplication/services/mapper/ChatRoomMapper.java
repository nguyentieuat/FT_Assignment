package com.example.chatapplication.services.mapper;


import com.example.chatapplication.domain.ChatRoom;
import com.example.chatapplication.services.dto.ChatRoomDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ChatRoomMapper extends EntityMapper<ChatRoomDto, ChatRoom> {
    default ChatRoom fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(id);
        return chatRoom;
    }
}
