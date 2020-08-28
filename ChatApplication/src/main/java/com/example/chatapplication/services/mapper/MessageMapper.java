package com.example.chatapplication.services.mapper;


import com.example.chatapplication.domain.Message;
import com.example.chatapplication.services.dto.MessageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MessageMapper extends EntityMapper<MessageDto, Message> {
    default Message fromId(Long id){
        if (id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;

    }
}
