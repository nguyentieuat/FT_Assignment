package com.example.managersharefile.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CommonId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int typeId;

    private int typeSubId;
}
