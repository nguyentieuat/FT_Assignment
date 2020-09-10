package com.example.chatapplication.domain;

import com.example.chatapplication.ultities.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 25)
    private String username;

    @OneToOne(mappedBy = "account")
    @JoinColumn(name = "username", unique = true)
    private Employee employee;

    @OneToMany(mappedBy = "managerAccount")
    private List<Employee> employees;

    @Column(length = 2)
    private int role;

    @OneToOne
    @JoinColumn(name = "chatStatusId")
    private ChatStatus chatStatus;

    @OneToMany(mappedBy = "accountSender")
    private List<Message> messages;

    @OneToMany(mappedBy = "account")
    private List<CaptureScreen> captureScreens;

    @ManyToMany(mappedBy = "accounts")
    private List<ChatRoom> chatRooms;

    @Column(nullable = false)
    @JsonIgnore
    private String password;
    private LocalDateTime lastLogin;
    private LocalDateTime lastLogout;
    @Column(length = 2)
    @ColumnDefault("1")
    private int status = Constants.Status.ACTIVE;

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

    @Column(nullable = true)
    private boolean isOnline;
}
