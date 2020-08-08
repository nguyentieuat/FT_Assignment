package com.example.managersharefile.entities;

import com.example.managersharefile.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "luannt19")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 11)
    private String employeeId;
    @Column(length = 11, nullable = false)
    private String ssn;
    @Column(length = 25, nullable = false)
    private String firstName;
    @Column(length = 25, nullable = false)
    private String lastName;
    @Column(length = 15, nullable = false, unique = true)
    private String phone;
    @Column(nullable = false, unique = true)
    private String email;
    private String address;
    private int salary;

    @OneToOne
    @JoinColumn(name="username")
    private Account account;

    @ManyToOne
    @JoinColumn(name="manager_username")
    private Account managerAccount;

    @ManyToOne
    @JoinColumn(name="department_id", nullable = true)
    private Department department;

    @Column(length = 2)
    private int jobTitle;
    @Column(length = 2)
    @ColumnDefault("1")
    private int status = Constants.StatusActive.ACTIVE;
    private LocalDateTime dob;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Column(length = 25, nullable = false)
    private String createdBy;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedDate;
    @Column(length = 25, nullable = false)
    private String updatedBy;

}
