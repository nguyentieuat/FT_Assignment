package com.example.chatapplication.services.dto;

import com.example.chatapplication.ultities.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatStatusDto {

    private long id;

    private String title;
    private String content;

    private int status = Constant.Status.ACTIVE;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;
}
