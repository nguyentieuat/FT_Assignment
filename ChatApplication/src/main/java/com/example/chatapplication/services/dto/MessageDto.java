package com.example.chatapplication.services.dto;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.domain.ChatRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String type;
    private String content;

    @JsonIgnore
    private Set<Attachment> attachments;

    @JsonIgnore
    private Account accountSender;

    @JsonIgnore
    private Account accountReceiver;

    private ChatRoom chatRoom;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;

    private String usernameSender;
    private String usernameReceiver;
    private long idAvatar;
    private boolean isOwner;
    private Map<Long, String> infoAttachment;
    private List<MultipartFile> files;


    public String getUsernameSender() {
        if (!Objects.isNull(accountSender)) {
            return accountSender.getUsername();
        }
        return usernameSender;
    }

    public String getUsernameReceiver() {
        if (!Objects.isNull(accountReceiver)) {
            return accountReceiver.getUsername();
        }
        return usernameReceiver;
    }

    public long getIdAvatar() {
        if (!Objects.isNull(accountSender)) {
            Attachment attachmentAvatar = accountSender.getEmployee().getAttachmentAvatar();
            if (!Objects.isNull(attachmentAvatar)) {
                return attachmentAvatar.getId();
            }
        }
        return idAvatar;
    }

    public Map<Long, String> getInfoAttachment() {
        if (!Objects.isNull(attachments)) {
            infoAttachment = new HashMap<>();
            attachments.forEach(attachment -> {
                infoAttachment.put(attachment.getId(), attachment.getFileName());
            });
        }
        return infoAttachment;
    }
}
