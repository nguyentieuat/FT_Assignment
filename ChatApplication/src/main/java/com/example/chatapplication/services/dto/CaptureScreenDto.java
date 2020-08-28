package com.example.chatapplication.services.dto;

import com.example.chatapplication.domain.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CaptureScreenDto {

    private long id;
    private String path;

    private Account account;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;
}
