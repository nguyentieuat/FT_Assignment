package com.example.chatapplication.services.mapper;


import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.services.dto.AttachmentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AttachmentMapper extends EntityMapper<AttachmentDto, Attachment> {
    default Attachment fromId(Long id){
        if (id == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return attachment;

    }
}
