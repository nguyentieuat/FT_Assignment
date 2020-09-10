package com.example.managersharefile.services.dto;

import com.example.managersharefile.entities.Department;
import com.example.managersharefile.entities.Employee;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.entities.RuleAccessFile;
import com.example.managersharefile.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AccountDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String account;

    private Employee employee;

    private int status = Constants.StatusActive.ACTIVE;

    private Set<FileDocument> fileDocuments;

    private Set<RuleAccessFile> ruleAccessFiles;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;
}
