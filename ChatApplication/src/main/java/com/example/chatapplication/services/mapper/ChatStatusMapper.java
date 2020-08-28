package com.example.chatapplication.services.mapper;


import com.example.chatapplication.domain.ChatStatus;
import com.example.chatapplication.services.dto.ChatStatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ChatStatusMapper extends EntityMapper<ChatStatusDto, ChatStatus> {
    default ChatStatus fromId(Long id){
        if (id == null) {
            return null;
        }
        ChatStatus chatStatus = new ChatStatus();
        chatStatus.setId(id);
        return chatStatus;
    }
}
