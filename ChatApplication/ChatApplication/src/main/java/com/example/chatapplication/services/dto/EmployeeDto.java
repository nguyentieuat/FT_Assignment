package com.example.chatapplication.services.dto;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Department;
import com.example.chatapplication.ultities.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String employeeId;
    private String ssn;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private int salary;

    private Account managerAccount;

    private Department department;

    private int jobTitle;
    private int status = Constants.Status.ACTIVE;
    private LocalDateTime dob;

    private Account account;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;

}
