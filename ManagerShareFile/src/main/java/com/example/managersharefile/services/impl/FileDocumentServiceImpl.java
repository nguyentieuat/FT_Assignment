package com.example.managersharefile.services.impl;

import com.example.managersharefile.utils.SecurityUtils;
import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.Common;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.entities.RuleAccessFile;
import com.example.managersharefile.exception.BusinessException;
import com.example.managersharefile.repositories.AccountRepository;
import com.example.managersharefile.repositories.CommonRepository;
import com.example.managersharefile.repositories.FileDocumentRepository;
import com.example.managersharefile.repositories.RuleAccessFileRepository;
import com.example.managersharefile.services.FileDocumentService;
import com.example.managersharefile.services.dto.FileDocumentDto;
import com.example.managersharefile.services.mapper.FileDocumentMapper;
import com.example.managersharefile.utils.Constants;
import com.example.managersharefile.utils.FileUtilsUpload;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FileDocumentServiceImpl implements FileDocumentService {

    @Autowired
    private FileDocumentRepository fileDocumentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private RuleAccessFileRepository ruleAccessFileRepository;

    @Autowired
    private FileUtilsUpload fileUtilsUpload;

    @Autowired
    private FileDocumentMapper fileDocumentMapper;

    @Override
    public FileDocumentDto saveInfoFile(FileDocumentDto fileDocumentDto, String username) throws FileUploadException {

        FileDocument fileDocumentResult = null;

        LocalDateTime now = LocalDateTime.now();

        int category = fileDocumentDto.getCategory();
        Common commonCategory = commonRepository.findByTypeIdAndTypeSubId(Constants.CommonValue.CATEGORY_FILE, category);

        Integer numberRecordInDay = fileDocumentRepository.countRecordCreatedInDate(now, category);

        Account account = accountRepository.findByUsername(username);

        MultipartFile fileUpload =  fileDocumentDto.getFileUpload();
        if (!Objects.isNull(fileUpload) && !fileUpload.isEmpty()) {
            try {
                String path = fileUtilsUpload.saveFileUpload(username, commonCategory.getTypeSubName(), numberRecordInDay, now, fileUpload);
                FileDocument fileDocument = new FileDocument();
                fileDocument.setAccount(account);
                fileDocument.setFileName(fileUpload.getOriginalFilename());
                fileDocument.setPath(path);
                fileDocument.setFileSize(fileUpload.getSize());
                fileDocument.setCreatedBy(username);
                fileDocument.setUpdatedBy(username);
                fileDocument.setCreatedDate(now);
                fileDocument.setUpdatedDate(now);

                fileDocumentResult = fileDocumentRepository.save(fileDocument);
                return fileDocumentMapper.toDto(fileDocumentResult);
            } catch (BusinessException e) {
                throw new FileUploadException("Could not store file");
            }
        }

        return null;
    }

    @Override
    public Page getAllFile(String fileName, int category, int status, Pageable pageable) {

        Specification conditionFileName = null;
        if (!fileName.isEmpty()) {
            conditionFileName = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(Constants.Attribute.FILE_NAME)),
                            new StringBuilder().append(Constants.CHARACTER_PERCENT)
                                    .append(fileName.toLowerCase())
                                    .append(Constants.CHARACTER_PERCENT)
                                    .toString());
                }
            };
        }

        Specification conditionCategory = null;
        if (category != Constants.Number.ZERO) {
            conditionCategory = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get(Constants.Attribute.CATEGORY), category);
                }
            };
        }

        Specification conditionStatus = null;
        if (status != Constants.Number.NEGATIVE_ONE) {
            conditionStatus = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get(Constants.Attribute.STATUS), status);
                }
            };
        }


        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = accountRepository.findByUsername(username);

        Specification byCurrentUser = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(Constants.Attribute.ACCOUNT), account);
            }
        };

        Page<FileDocument> resultPage = fileDocumentRepository.findAll(Specification.where(conditionCategory).and(conditionFileName).and(conditionStatus).and(byCurrentUser), pageable);

        Page<FileDocumentDto> fileDocumentDtoPages = resultPage.map(fileDocumentMapper::toDto);

        return fileDocumentDtoPages;
    }

    @Override
    public Boolean inactiveFile(int fileId) throws BusinessException {

        processInactiveFile(fileId);
        return true;
    }

    private void processInactiveFile(int fileId) throws BusinessException {

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = accountRepository.findByUsername(currentUsername);

        FileDocument fileDocument = fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId);

        if (Objects.isNull(fileDocument)) {
            throw new BusinessException("Can't inactive file");
        }

        fileDocument.setStatus(Constants.StatusActive.INACTIVE);

        List<RuleAccessFile> ruleAccessFiles = ruleAccessFileRepository.findByFileDocument(fileDocument);
        if (!ruleAccessFiles.isEmpty()) {
            ruleAccessFiles.forEach(ruleAccessFile -> ruleAccessFile.setStatus(Constants.StatusActive.INACTIVE));
        }
        fileDocumentRepository.save(fileDocument);
        ruleAccessFileRepository.saveAll(ruleAccessFiles);
    }

    @Override
    public Boolean inactiveFile(Integer[] fileIds) throws BusinessException {
        for (Integer fileId : fileIds) {
            processInactiveFile(fileId);
        }
        return true;
    }

    @Override
    public Boolean activeFile(int fileId) throws BusinessException {

        processActiveFile(fileId);
        return true;
    }

    private void processActiveFile(int fileId) throws BusinessException {

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = accountRepository.findByUsername(currentUsername);

        FileDocument fileDocument = fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId);

        if (Objects.isNull(fileDocument)) {
            throw new BusinessException("Can't active file");
        }

        fileDocument.setStatus(Constants.StatusActive.ACTIVE);

        List<RuleAccessFile> ruleAccessFiles = ruleAccessFileRepository.findByFileDocument(fileDocument);
        if (!ruleAccessFiles.isEmpty()) {
            ruleAccessFiles.forEach(ruleAccessFile -> ruleAccessFile.setStatus(Constants.StatusActive.ACTIVE));
        }
        fileDocumentRepository.save(fileDocument);
        ruleAccessFileRepository.saveAll(ruleAccessFiles);
    }

    @Override
    public Boolean activeFile(Integer[] fileIds) throws BusinessException {
        for (Integer fileId : fileIds) {
            processActiveFile(fileId);
        }
        return true;
    }

    @Override
    public Page<FileDocumentDto> getFileShared(String fileName, int category, Pageable pageable) {

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = accountRepository.findByUsername(username);

        Specification conditionFileName = null;
        if (!fileName.isEmpty()) {
            conditionFileName = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(Constants.Attribute.FILE_NAME)),
                            new StringBuilder().append(Constants.CHARACTER_PERCENT)
                                    .append(fileName.toLowerCase())
                                    .append(Constants.CHARACTER_PERCENT)
                                    .toString());
                }
            };
        }

        Specification conditionCategory = null;
        if (category != Constants.Number.ZERO) {
            conditionCategory = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get(Constants.Attribute.CATEGORY), category);
                }
            };
        }

        Specification conditionAccountRule = null;
        conditionAccountRule = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("ruleAccessFiles");
                return criteriaBuilder.equal(join.get(Constants.Attribute.ACCOUNT), account);
            }
        };
        
        Specification conditionStatus = null;
        conditionStatus = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(Constants.Attribute.STATUS), Constants.StatusActive.ACTIVE);
            }
        };

        Page<FileDocument> fileDocumentShared = fileDocumentRepository.findAll(Specification.where(conditionAccountRule).and(conditionCategory).and(conditionFileName).and(conditionStatus), pageable);

        return fileDocumentShared.map(fileDocumentMapper::toDto);
    }

    @Override
    public FileDocument findByFileId(int fileId) {

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        FileDocument fileDocument  = fileDocumentRepository.findById(fileId).orElse(null);
        if (Objects.isNull(fileDocument)){
            return null;
        }

        //Check file is user's. If it's user's then check that file is granted for user. If is grated then status must active
        if (!fileDocument.getAccount().getUsername().equalsIgnoreCase(currentUsername)){
            Account currentAccount = accountRepository.findByUsername(currentUsername);
            RuleAccessFile ruleAccessFile = ruleAccessFileRepository.findByAccountAndFileDocument(currentAccount, fileDocument);
            if (Objects.isNull(ruleAccessFile) || ruleAccessFile.getStatus() != Constants.StatusActive.ACTIVE){
                return null;
            }
        }

        return fileDocument;
    }

    @Override
    public Boolean checkRuleAccessDownload(String currentUsername, FileDocument fileDocument) {
        Account currentAccount = accountRepository.findByUsername(currentUsername);
        RuleAccessFile ruleAccessFile = ruleAccessFileRepository.findByAccountAndFileDocument(currentAccount, fileDocument);

        //Check file is user's. If it's user's then check that file is granted for user, must rule write
        if (!fileDocument.getAccount().getUsername().equalsIgnoreCase(currentUsername)){

            Common common = commonRepository.findByTypeIdAndTypeSubId(Constants.CommonValue.RULE_ACCESS_FILE, Constants.CommonValue.RULE_ACCESS_FILE_WRITE);

            if (Objects.isNull(ruleAccessFile) || ruleAccessFile.getRule() != common.getTypeSubId()){
                return false;
            }
        }
        return true;
    }
}
