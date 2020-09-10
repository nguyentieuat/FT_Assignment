package com.example.chatapplication.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommonDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int typeId;
    private String typeName;
    private int typeSubId;
    private String typeSubName;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;


}
