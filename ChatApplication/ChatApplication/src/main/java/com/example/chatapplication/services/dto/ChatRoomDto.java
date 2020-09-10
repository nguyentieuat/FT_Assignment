package com.example.chatapplication.services.dto;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.domain.ChatStatus;
import com.example.chatapplication.domain.Message;
import com.example.chatapplication.ultities.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    private long id;
    private String name;

    private Attachment attachmentAvatar;

    private Set<Attachment> attachments;
    private Set<Message> messages;

    private ChatStatus chatStatus;

    private int status = Constants.Status.ACTIVE;

    private List<Account> accounts;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;
}
