package com.example.managersharefile.services.dto;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.Employee;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class RuleAccessFileDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int rule = Constants.Number.ONE;

    @JsonIgnore
    private Account account;

    @JsonIgnore
    private FileDocument fileDocument;

    private int status = Constants.StatusActive.ACTIVE;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;

    public String username;

    public Integer fileId;


    public String getUsername() {
        if (Objects.isNull(account)){
            return username;
        }
        return account.getUsername();
    }

    public Integer getFileId() {
        if (Objects.isNull(account)){
            return fileId;
        }
       return fileDocument.getFileId();
    }
}
