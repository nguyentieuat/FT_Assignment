package com.example.chatapplication.services.dto;

import com.example.chatapplication.domain.ChatRoom;
import com.example.chatapplication.domain.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AttachmentDto {

    private long id;
    private String pathAttachment;
    private String fileName;
    private long size;
    private Message message;

    private ChatRoom chatRoom;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;
}
