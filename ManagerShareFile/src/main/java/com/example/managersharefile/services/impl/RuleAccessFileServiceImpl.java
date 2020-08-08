package com.example.managersharefile.services.impl;

import com.example.managersharefile.utils.SecurityUtils;
import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.entities.RuleAccessFile;
import com.example.managersharefile.exception.BusinessException;
import com.example.managersharefile.repositories.AccountRepository;
import com.example.managersharefile.repositories.FileDocumentRepository;
import com.example.managersharefile.repositories.RuleAccessFileRepository;
import com.example.managersharefile.services.RuleAccessFileService;
import com.example.managersharefile.services.dto.RuleAccessFileDto;
import com.example.managersharefile.services.mapper.RuleAccessFileMapper;
import com.example.managersharefile.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RuleAccessFileServiceImpl implements RuleAccessFileService {

    @Autowired
    private RuleAccessFileRepository ruleAccessFileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FileDocumentRepository fileDocumentRepository;

    @Autowired
    private RuleAccessFileMapper ruleAccessFileMapper;


    @Override
    public List<RuleAccessFileDto> addRule(List<RuleAccessFileDto> ruleAccessFileDtos) throws BusinessException {

        List ruleAccessFiles = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        String currentAccount = SecurityUtils.getAccountCurrentUserLogin().get();

        for (RuleAccessFileDto ruleAccessFileDto : ruleAccessFileDtos) {
            RuleAccessFile ruleAccessFile = setInfo4RuleAccessFile(ruleAccessFileDto, currentAccount, now);
            if (!Objects.isNull(ruleAccessFile)) {
                ruleAccessFiles.add(ruleAccessFile);
            } else {
                log.error("Can't grant " + ruleAccessFileDto.getUsername() + " for file_id" + ruleAccessFileDto.getFileId());
            }
        }

        if (ruleAccessFiles.size() > Constants.Number.ZERO) {

            List<RuleAccessFile> list = ruleAccessFileRepository.saveAll(ruleAccessFiles);
            List<RuleAccessFileDto> ruleAccessFileDtosResult = list.stream().map(ruleAccessFileMapper::toDto).collect(Collectors.toList());

            return ruleAccessFileDtosResult;
        } else {
            throw new BusinessException("Can't grant");
        }

    }

    private RuleAccessFile setInfo4RuleAccessFile(RuleAccessFileDto ruleAccessFileDto, String currentUsername, LocalDateTime now) {

        String username = ruleAccessFileDto.getUsername();
        Account account = accountRepository.findByUsernameAndStatus(username, Constants.StatusActive.ACTIVE);

        Account currentAccount = accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE);
        FileDocument fileDocument = fileDocumentRepository.findByAccountAndFileIdAndStatus(currentAccount, ruleAccessFileDto.getFileId(), Constants.StatusActive.ACTIVE);

        if (!Objects.isNull(account) && !Objects.isNull(fileDocument)) {
            RuleAccessFile ruleAccessFile = new RuleAccessFile();
            ruleAccessFile.setRule(ruleAccessFileDto.getRule());
            ruleAccessFile.setAccount(account);
            ruleAccessFile.setFileDocument(fileDocument);
            ruleAccessFile.setCreatedBy(currentUsername);
            ruleAccessFile.setCreatedDate(now);
            ruleAccessFile.setUpdatedBy(currentUsername);
            ruleAccessFile.setUpdatedDate(now);
            return ruleAccessFile;
        }
        return null;
    }

    @Override
    public RuleAccessFileDto addRule(RuleAccessFileDto ruleAccessFileDto) throws BusinessException {

        LocalDateTime now = LocalDateTime.now();
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        RuleAccessFile ruleAccessFile = setInfo4RuleAccessFile(ruleAccessFileDto, currentUsername, now);
        if (!Objects.isNull(ruleAccessFile)) {
            RuleAccessFile ruleAccessFileResult = ruleAccessFileRepository.save(ruleAccessFile);
            return ruleAccessFileMapper.toDto(ruleAccessFileResult);
        } else {
            throw new BusinessException("Can't grant");
        }

    }

    @Override
    public Boolean removeRule(RuleAccessFileDto ruleAccessFileDto) throws BusinessException {

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE);
        ruleAccessFileRepository.delete(revokeAccess(ruleAccessFileDto, currentAccount));
        return true;
    }

    private RuleAccessFile revokeAccess(RuleAccessFileDto ruleAccessFileDto, Account currentAccount) throws BusinessException {
        Account account = accountRepository.findByUsername(ruleAccessFileDto.getUsername());
        FileDocument fileDocument = fileDocumentRepository.findByAccountAndFileId(currentAccount, ruleAccessFileDto.getFileId());
        if (!Objects.isNull(account) && !Objects.isNull(fileDocument)) {
            RuleAccessFile ruleAccessFile = ruleAccessFileRepository.findByAccountAndFileDocument(account, fileDocument);
            return ruleAccessFile;
        } else {
            throw new BusinessException("Can't revoke");
        }
    }

    @Override
    public Boolean removeRule(List<RuleAccessFileDto> ruleAccessFileDtos) throws BusinessException {
        List ruleAccessFiles = new ArrayList<>();

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE);

        for (RuleAccessFileDto ruleAccessFileDto : ruleAccessFileDtos) {
            ruleAccessFiles.add(revokeAccess(ruleAccessFileDto, currentAccount));
        }
        ruleAccessFileRepository.deleteAll(ruleAccessFiles);
        return true;
    }
}
