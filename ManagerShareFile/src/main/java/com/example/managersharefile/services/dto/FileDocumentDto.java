package com.example.managersharefile.services.dto;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.RuleAccessFile;
import com.example.managersharefile.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FileDocumentDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int fileId;

    @JsonIgnore
    private Account account;

    private String fileName;
    private int category;
    private String path;
    private float fileSize;
    private int status = Constants.StatusActive.ACTIVE;

    @JsonIgnore
    private Set<RuleAccessFile> ruleAccessFiles;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;

    private MultipartFile fileUpload;

    private String userCreate;
    private List<RuleAccessFileDto> usersIsGranted;

    public String getUserCreate() {
        if (!Objects.isNull(account)){
            return account.getUsername();
        }
        return userCreate;

    }

    public List<RuleAccessFileDto> getUsersIsGranted() {
        List usersIsGranted = new ArrayList();

        if (!Objects.isNull(ruleAccessFiles) && ruleAccessFiles.size() > 0){
            for (RuleAccessFile ruleAccessFile: ruleAccessFiles) {
                RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
                ruleAccessFileDto.setUsername(ruleAccessFile.getAccount().getUsername());
                ruleAccessFileDto.setRule(ruleAccessFile.getRule());
                ruleAccessFileDto.setFileId(ruleAccessFile.getFileDocument().getFileId());
                ruleAccessFileDto.setStatus(ruleAccessFile.getStatus());
                ruleAccessFileDto.setCreatedBy(ruleAccessFile.getCreatedBy());
                ruleAccessFileDto.setCreatedDate(ruleAccessFile.getCreatedDate());
                ruleAccessFileDto.setUpdatedBy(ruleAccessFile.getUpdatedBy());
                ruleAccessFileDto.setUpdatedDate(ruleAccessFile.getUpdatedDate());
                usersIsGranted.add(ruleAccessFileDto);
            }
        }
        return usersIsGranted;
    }
}
