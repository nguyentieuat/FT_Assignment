package com.example.managersharefile.repositories;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.FileDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FileDocumentRepository extends JpaRepository<FileDocument, Integer> {

    @Query(value = "Select count(*) from manager_share_file.file_document fd\n" +
            "Where DATE (fd.created_date) = DATE (:date) and fd.category = :category", nativeQuery = true)
    Integer countRecordCreatedInDate(LocalDateTime date, int category);

    Page findAll(Specification specification, Pageable pageable);

    FileDocument findByFileIdAndStatus(int fileId, int status);

    FileDocument findByAccountAndFileIdAndStatus(Account account, int fileId, int status);

    FileDocument findByAccountAndFileId(Account account, int fileId);
}
