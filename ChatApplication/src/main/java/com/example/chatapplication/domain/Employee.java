package com.example.chatapplication.domain;

import com.example.chatapplication.ultities.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(name = "attachmentAvatar")
    private Attachment attachmentAvatar;
    private int salary;

    @OneToOne
    @JoinColumn(name = "username")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "managerUsername")
    private Account managerAccount;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @Column(length = 2)
    private int jobTitle;
    @Column(length = 2)
    @ColumnDefault("1")
    private int status = Constants.Status.ACTIVE;
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
