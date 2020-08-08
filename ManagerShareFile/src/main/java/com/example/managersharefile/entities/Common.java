package com.example.managersharefile.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@IdClass(CommonId.class)
public class Common implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int typeId;
    private String typeName;
    @Id
    private int typeSubId;
    private String typeSubName;

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
