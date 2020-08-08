package com.example.managersharefile.entities;

import com.example.managersharefile.utils.Constants;
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
@Entity
@Table(schema = "luannt19")
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String departmentId;
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "manager_username", referencedColumnName = "username")
    private Account managerAccount;
    @Column(length = 2)
    @ColumnDefault("1")
    private int status = Constants.StatusActive.ACTIVE;

    @OneToMany(mappedBy="department")
    private Set<Employee> employees;

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
