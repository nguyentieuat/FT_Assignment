package com.example.chatapplication.domain;

import com.example.chatapplication.ultities.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
    @JoinColumn(name = "managerUsername", referencedColumnName = "username")
    private Account managerAccount;
    @Column(length = 2)
    @ColumnDefault("1")
    private int status = Constant.Status.ACTIVE;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

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
