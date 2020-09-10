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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "luannt19")
@IdClass(RuleAccessFileId.class)
public class RuleAccessFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Account account;

    @Id
    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileDocument fileDocument;

    @Column(length = 2, nullable = false)
    @ColumnDefault("1")
    private int rule = Constants.Number.ONE;

    @Column(length = 2)
    @ColumnDefault("1")
    private int status = Constants.StatusActive.ACTIVE;

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
