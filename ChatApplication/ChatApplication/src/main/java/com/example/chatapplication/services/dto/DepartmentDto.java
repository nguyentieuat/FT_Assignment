package com.example.chatapplication.services.dto;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Employee;
import com.example.chatapplication.ultities.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String departmentId;
    private String departmentName;

    private Account managerAccount;
    private int status = Constant.Status.ACTIVE;

    private List<Employee> employees;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;

}
