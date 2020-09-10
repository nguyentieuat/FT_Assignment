package com.example.managersharefile.services.dto;

import com.example.managersharefile.entities.*;
import com.example.managersharefile.utils.Constants;
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
    private int status = Constants.StatusActive.ACTIVE;
    private LocalDateTime dob;

    private Account account;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;

}
