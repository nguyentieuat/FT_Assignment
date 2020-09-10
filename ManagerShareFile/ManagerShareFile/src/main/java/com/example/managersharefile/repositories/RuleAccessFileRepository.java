package com.example.managersharefile.repositories;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.entities.RuleAccessFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleAccessFileRepository extends JpaRepository<RuleAccessFile, Integer> {

    RuleAccessFile findByAccountAndFileDocument(Account account, FileDocument fileDocument);

    List findByFileDocument(FileDocument fileDocument);

}
