package com.example.managersharefile.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RuleAccessFileId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String account;

    private int fileDocument;
}
