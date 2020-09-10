package com.example.managersharefile.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntityDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;
    private String message;

}
