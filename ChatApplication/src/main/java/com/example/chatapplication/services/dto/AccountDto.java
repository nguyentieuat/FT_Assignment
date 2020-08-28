package com.example.chatapplication.services.dto;

import com.example.chatapplication.domain.*;
import com.example.chatapplication.ultities.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AccountDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;

    @JsonIgnore
    private Employee employee;
    private int role;
    private ChatStatus chatStatus;

    private Set<Message> messages;

    private LocalDateTime lastLogin;
    private LocalDateTime lastLogout;
    private int status = Constants.Status.ACTIVE;

    private Set<CaptureScreen> captureScreens;

    private Set<ChatRoom> chatRooms;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;

    private long idAvatar;

    public long getIdAvatar() {
        if (!Objects.isNull(employee)){
            return employee.getAttachmentAvatar().getId();
        }
        return idAvatar;
    }
}
