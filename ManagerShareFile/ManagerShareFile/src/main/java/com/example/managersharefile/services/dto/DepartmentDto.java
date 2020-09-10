package com.example.managersharefile.services.dto;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.Employee;
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
public class DepartmentDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String departmentId;
    private String departmentName;

    private Account managerAccount;
    private int status = Constants.StatusActive.ACTIVE;

    private Set<Employee> employees;

    private LocalDateTime createdDate;
    private String createdBy;

    private LocalDateTime updatedDate;
    private String updatedBy;

}
