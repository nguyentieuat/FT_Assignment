package com.example.chatapplication.domain;

import com.example.chatapplication.ultities.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "luannt19")
public class ChatRoom {

    @Id
    private long id;
    private String name;

    @OneToOne
    @JoinColumn(name = "attachmentAvatar")
    private Attachment attachmentAvatar;

    @OneToMany(mappedBy = "chatRoom")
    private Set<Attachment> attachments;


    @OneToOne
    @JoinColumn(name = "chatStatusId")
    private ChatStatus chatStatus;
    @Column(length = 2)
    @ColumnDefault("1")
    private int status = Constants.Status.ACTIVE;

    @ManyToMany
    @JoinTable(name = "room_account", joinColumns = @JoinColumn(name = "room_id"),inverseJoinColumns = @JoinColumn(name = "username"))
    private Set<Account> accounts;

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
