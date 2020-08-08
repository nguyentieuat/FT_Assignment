package com.example.managersharefile.entities;

import com.example.managersharefile.utils.Constants;
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
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "luannt19")
public class FileDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int fileId;

    @ManyToOne
    @JoinColumn(name = "username_upload", nullable = false)
    private Account account;
    @Column(nullable = false)
    private String fileName;
    @Column(length = 2)
    private int category;
    private String path;
    private float fileSize;
    @Column(length = 2)
    @ColumnDefault("1")
    private int status = Constants.StatusActive.ACTIVE;

    @OneToMany(mappedBy="fileDocument")
    private Set<RuleAccessFile> ruleAccessFiles;

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
