package com.example.chatapplication.services.dto;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.domain.ChatStatus;
import com.example.chatapplication.ultities.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    private long id;

    private ChatStatus chatStatus;
    private int status = Constants.Status.ACTIVE;

    @JsonIgnore
    private Set<Account> accounts;
    private Set<Attachment> attachments;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;
}
