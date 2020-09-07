package com.example.managersharefile.services;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.Common;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.entities.RuleAccessFile;
import com.example.managersharefile.exception.BusinessException;
import com.example.managersharefile.repositories.AccountRepository;
import com.example.managersharefile.repositories.CommonRepository;
import com.example.managersharefile.repositories.FileDocumentRepository;
import com.example.managersharefile.repositories.RuleAccessFileRepository;
import com.example.managersharefile.services.dto.FileDocumentDto;
import com.example.managersharefile.services.impl.FileDocumentServiceImpl;
import com.example.managersharefile.services.mapper.FileDocumentMapper;
import com.example.managersharefile.utils.Constants;
import com.example.managersharefile.utils.FileUtilsUpload;
import com.example.managersharefile.utils.SecurityUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class FileDocumentServiceTest {
    @InjectMocks
    private FileDocumentService fileDocumentService = new FileDocumentServiceImpl();

    @Mock
    private FileDocumentRepository fileDocumentRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CommonRepository commonRepository;

    @Mock
    private RuleAccessFileRepository ruleAccessFileRepository;

    @Mock
    private FileUtilsUpload fileUtilsUpload;

    @Mock
    private FileDocumentMapper fileDocumentMapper;

    @Test
    public void saveInfoFile() throws BusinessException, FileUploadException {
        int category = 1;

        FileDocumentDto fileDocumentDto = new FileDocumentDto();
        MockMultipartFile uploadFile = new MockMultipartFile("test.txt", "test.txt", null, "content".getBytes());
        fileDocumentDto.setFileUpload(uploadFile);
        fileDocumentDto.setCategory(category);


        String username = "emp001";

        Common commonCategory = new Common();
        commonCategory.setTypeSubName("subName1");
        when(commonRepository.findByTypeIdAndTypeSubId(Constants.CommonValue.CATEGORY_FILE, category))
                .thenReturn(commonCategory);

        Integer numberRecordInDay = 1;
        when(fileDocumentRepository.countRecordCreatedInDate(any(), eq(category)))
                .thenReturn(numberRecordInDay);

        Account account = new Account();
        when(accountRepository.findByUsername(username))
                .thenReturn(account);

        String path = "D:\\ManagerShareFile\\emp001\\document\\2020_07_23\\5\\test.txt";

        MultipartFile fileUpload = fileDocumentDto.getFileUpload();

        when(fileUtilsUpload.saveFileUpload(eq(username), eq(commonCategory.getTypeSubName()), eq(numberRecordInDay), any(), eq(fileUpload)))
                .thenReturn(path);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.save(any(FileDocument.class)))
                .thenReturn(fileDocument);


        FileDocumentDto fileDocumentDtoResult = new FileDocumentDto();
        when(fileDocumentMapper.toDto(fileDocument))
                .thenReturn(fileDocumentDtoResult);


        Assert.assertEquals(fileDocumentDtoResult,
                fileDocumentService.saveInfoFile(fileDocumentDto, username));

    }

    @Test(expected = FileUploadException.class)
    public void saveInfoFileHasError() throws BusinessException, FileUploadException {
        int category = 1;

        FileDocumentDto fileDocumentDto = new FileDocumentDto();
        MockMultipartFile uploadFile = new MockMultipartFile("test.txt", "test.txt", null, "content".getBytes());
        fileDocumentDto.setFileUpload(uploadFile);
        fileDocumentDto.setCategory(category);


        String username = "emp001";

        LocalDateTime now = LocalDateTime.now();

        Common commonCategory = new Common();
        commonCategory.setTypeSubName("subName1");
        when(commonRepository.findByTypeIdAndTypeSubId(Constants.CommonValue.CATEGORY_FILE, category))
                .thenReturn(commonCategory);

        Integer numberRecordInDay = 1;
        when(fileDocumentRepository.countRecordCreatedInDate(any(), eq(category)))
                .thenReturn(numberRecordInDay);

        Account account = new Account();
        when(accountRepository.findByUsername(username))
                .thenReturn(account);

        String path = "D:\\ManagerShareFile\\emp001\\document\\2020_07_23\\5\\test.txt";

        MultipartFile fileUpload = fileDocumentDto.getFileUpload();

        when(fileUtilsUpload.saveFileUpload(eq(username), eq(commonCategory.getTypeSubName()), eq(numberRecordInDay), any(), eq(fileUpload)))
                .thenThrow(BusinessException.class);

        fileDocumentService.saveInfoFile(fileDocumentDto, username);
    }

    @Test(expected = FileUploadException.class)
    public void saveInfoFileHasError2() throws BusinessException, FileUploadException {
        int category = 1;

        FileDocumentDto fileDocumentDto = new FileDocumentDto();
        MockMultipartFile uploadFile = new MockMultipartFile("test.txt", "test.txt", null, "content".getBytes());
        fileDocumentDto.setFileUpload(uploadFile);
        fileDocumentDto.setCategory(category);


        String username = "emp001";

        LocalDateTime now = LocalDateTime.now();

        Common commonCategory = new Common();
        commonCategory.setTypeSubName("subName1");
        when(commonRepository.findByTypeIdAndTypeSubId(Constants.CommonValue.CATEGORY_FILE, category))
                .thenReturn(commonCategory);

        Integer numberRecordInDay = 1;
        when(fileDocumentRepository.countRecordCreatedInDate(any(), eq(category)))
                .thenReturn(numberRecordInDay);

        Account account = new Account();
        when(accountRepository.findByUsername(username))
                .thenReturn(null);

        String path = "D:\\ManagerShareFile\\emp001\\document\\2020_07_23\\5\\test.txt";

        MultipartFile fileUpload = fileDocumentDto.getFileUpload();

        when(fileUtilsUpload.saveFileUpload(eq(username), eq(commonCategory.getTypeSubName()), eq(numberRecordInDay), any(), eq(fileUpload)))
                .thenThrow(BusinessException.class);

        Assert.assertNull(fileDocumentService.saveInfoFile(fileDocumentDto, username));
    }

    @Test
    public void getAllFile() {

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(account);

        String fileName = "f";
        int category = 0;
        int status = 1;

        Sort sortDate = Sort.by(Sort.Direction.DESC, "create");
        Sort sortName = Sort.by(Sort.Direction.ASC, "fileName");
        Sort sort = sortDate.and(sortName);

        Pageable pageable = PageRequest.of(0, 10, sort);


        List list = new ArrayList();
        final Page<FileDocument> page = new PageImpl<>(list);

        when(fileDocumentRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        FileDocument fileDocumentResult = new FileDocument();
        FileDocumentDto fileDocumentDtoResult = new FileDocumentDto();
        when(fileDocumentMapper.toDto(fileDocumentResult))
                .thenReturn(fileDocumentDtoResult);

        Assert.assertEquals(page.getTotalElements(),
                fileDocumentService.getAllFile(fileName, category, status, pageable).getTotalElements());

    }

    @Test
    public void inactiveFile() throws BusinessException {
        int fileId = 1;

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(fileDocument);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        Assert.assertTrue(fileDocumentService.inactiveFile(fileId));
    }

    @Test(expected = BusinessException.class)
    public void inactiveFileHasError() throws BusinessException {
        int fileId = 1;

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(null);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        fileDocumentService.inactiveFile(fileId);
    }

    @Test()
    public void inactiveFile2() throws BusinessException {
        int fileId = 1;

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(fileDocument);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(2);
        ruleAccessFile.setFileDocument(fileDocument);
        ruleAccessFiles.add(ruleAccessFile);

        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        Assert.assertTrue(fileDocumentService.inactiveFile(fileId));
    }

    @Test
    public void activeFile() throws BusinessException {
        int fileId = 1;

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(fileDocument);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        Assert.assertTrue(fileDocumentService.activeFile(fileId));
    }

    @Test
    public void activeFile2() throws BusinessException {
        int fileId = 1;

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(fileDocument);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(2);
        ruleAccessFile.setFileDocument(fileDocument);
        ruleAccessFiles.add(ruleAccessFile);
        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        Assert.assertTrue(fileDocumentService.activeFile(fileId));
    }

    @Test(expected = BusinessException.class)
    public void activeFileHasError() throws BusinessException {
        int fileId = 1;

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(null);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        fileDocumentService.activeFile(fileId);
    }

    @Test
    public void testInactiveFile() throws BusinessException {
        Integer[] fileIds = {1};

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        int fileId = fileIds[0];
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(fileDocument);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(2);
        ruleAccessFile.setFileDocument(fileDocument);
        ruleAccessFiles.add(ruleAccessFile);

        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        Assert.assertTrue(fileDocumentService.inactiveFile(fileIds));
    }

    @Test(expected = BusinessException.class)
    public void testInactiveFileHasError() throws BusinessException {
        Integer[] fileIds = {1};

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        int fileId = fileIds[0];
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(null);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(2);
        ruleAccessFile.setFileDocument(fileDocument);
        ruleAccessFiles.add(ruleAccessFile);

        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        fileDocumentService.inactiveFile(fileIds);
    }

    @Test
    public void testActiveFile() throws BusinessException {

        Integer[] fileIds = {1};

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        int fileId = fileIds[0];
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(fileDocument);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(2);
        ruleAccessFile.setFileDocument(fileDocument);
        ruleAccessFiles.add(ruleAccessFile);

        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        Assert.assertTrue(fileDocumentService.activeFile(fileIds));
    }

    @Test(expected = BusinessException.class)
    public void testActiveFileHasError() throws BusinessException {

        Integer[] fileIds = {1};

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        int fileId = fileIds[0];
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, fileId))
                .thenReturn(null);

        List<RuleAccessFile> ruleAccessFiles = new ArrayList<>();
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(2);
        ruleAccessFile.setFileDocument(fileDocument);
        ruleAccessFiles.add(ruleAccessFile);

        when(ruleAccessFileRepository.findByFileDocument(fileDocument)).thenReturn(ruleAccessFiles);

        fileDocumentService.activeFile(fileIds);
    }

    @Test
    public void getFileShared() {

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = new Account();
        when(accountRepository.findByUsername(username)).thenReturn(account);

        String fileName = "f";
        int category = 0;

        Sort sortDate = Sort.by(Sort.Direction.DESC, "create");
        Sort sortName = Sort.by(Sort.Direction.ASC, "fileName");
        Sort sort = sortDate.and(sortName);

        Pageable pageable = PageRequest.of(0, 10, sort);


        List list = new ArrayList();
        final Page<FileDocument> page = new PageImpl<>(list);

        when(fileDocumentRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        FileDocument fileDocumentResult = new FileDocument();
        FileDocumentDto fileDocumentDtoResult = new FileDocumentDto();
        when(fileDocumentMapper.toDto(fileDocumentResult))
                .thenReturn(fileDocumentDtoResult);


        Assert.assertEquals(page.getTotalElements(),
                fileDocumentService.getFileShared(fileName, category, pageable).getTotalElements());
    }

    @Test
    public void findByFileIdOwner() {
        Integer fileId = 1;
        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsername(currentUsername)).thenReturn(currentAccount);


        FileDocument fileDocument  = new FileDocument();
        fileDocument.setAccount(currentAccount);
        Optional<FileDocument> optionalFileDocument = Optional.of(fileDocument);
        when(fileDocumentRepository.findById(fileId))
                .thenReturn(optionalFileDocument);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setStatus(Constants.StatusActive.ACTIVE);
        when(ruleAccessFileRepository.findByAccountAndFileDocument(currentAccount, fileDocument)).thenReturn(ruleAccessFile);

        Assert.assertEquals(fileDocument,
                fileDocumentService.findByFileId(fileId));
    }

    @Test
    public void findByFileIdNotOwner() {
        Integer fileId = 1;
        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsername(currentUsername)).thenReturn(currentAccount);


        FileDocument fileDocument  = new FileDocument();
        Account account = new Account();
        account.setUsername("emp002");
        fileDocument.setAccount(account);
        Optional<FileDocument> optionalFileDocument = Optional.of(fileDocument);
        when(fileDocumentRepository.findById(fileId))
                .thenReturn(optionalFileDocument);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setStatus(Constants.StatusActive.ACTIVE);
        when(ruleAccessFileRepository.findByAccountAndFileDocument(currentAccount, fileDocument)).thenReturn(ruleAccessFile);

        Assert.assertEquals(fileDocument,
                fileDocumentService.findByFileId(fileId));
    }

    @Test
    public void findByFileIdNotFound() {
        Integer fileId = 1;
        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsername(currentUsername)).thenReturn(currentAccount);


        FileDocument fileDocument  = new FileDocument();
        Account account = new Account();
        account.setUsername("emp002");
        fileDocument.setAccount(account);

        when(fileDocumentRepository.findById(fileId))
                .thenReturn(Optional.empty());

        Assert.assertNull(
                fileDocumentService.findByFileId(fileId));
    }

    @Test
    public void findByFileIdNotRuleAccess() {
        Integer fileId = 1;
        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsername(currentUsername)).thenReturn(currentAccount);


        FileDocument fileDocument  = new FileDocument();
        Account account = new Account();
        account.setUsername("emp002");
        fileDocument.setAccount(account);
        Optional<FileDocument> optionalFileDocument = Optional.of(fileDocument);
        when(fileDocumentRepository.findById(fileId))
                .thenReturn(optionalFileDocument);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setStatus(Constants.StatusActive.ACTIVE);
        when(ruleAccessFileRepository.findByAccountAndFileDocument(currentAccount, fileDocument))
                .thenReturn(null);

        Assert.assertNull(
                fileDocumentService.findByFileId(fileId));
    }

    @Test
    public void checkRuleAccessDownloadOwner() {
        String currentUsername = "emp001";
        FileDocument fileDocument = new FileDocument();

        Account currentAccount = new Account();
        currentAccount.setUsername(currentUsername);
        when(accountRepository.findByUsername(currentUsername)).thenReturn(currentAccount);


        fileDocument.setAccount(currentAccount);
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        when(ruleAccessFileRepository.findByAccountAndFileDocument(currentAccount, fileDocument))
                .thenReturn(ruleAccessFile);

        Common common = new Common();
        common.setTypeSubId(1);
        when(commonRepository.findByTypeIdAndTypeSubId(Constants.CommonValue.RULE_ACCESS_FILE, Constants.CommonValue.RULE_ACCESS_FILE_WRITE))
                .thenReturn(common);

        Assert.assertTrue(fileDocumentService.checkRuleAccessDownload(currentUsername, fileDocument));
    }

    @Test
    public void checkRuleAccessDownloadNotOwner() {
        String currentUsername = "emp001";
        FileDocument fileDocument = new FileDocument();

        Account currentAccount = new Account();
        currentAccount.setUsername(currentUsername);
        when(accountRepository.findByUsername(currentUsername)).thenReturn(currentAccount);

        Account account = new Account();
        account.setUsername("currentUsername");
        fileDocument.setAccount(account);
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(2);
        when(ruleAccessFileRepository.findByAccountAndFileDocument(currentAccount, fileDocument))
                .thenReturn(ruleAccessFile);

        Common common = new Common();
        common.setTypeSubId(2);
        when(commonRepository.findByTypeIdAndTypeSubId(Constants.CommonValue.RULE_ACCESS_FILE, Constants.CommonValue.RULE_ACCESS_FILE_WRITE))
                .thenReturn(common);

        Assert.assertTrue(fileDocumentService.checkRuleAccessDownload(currentUsername, fileDocument));
    }

    @Test
    public void checkRuleAccessDownloadNotRuleAccess() {
        String currentUsername = "emp001";
        FileDocument fileDocument = new FileDocument();

        Account currentAccount = new Account();
        currentAccount.setUsername(currentUsername);
        when(accountRepository.findByUsername(currentUsername)).thenReturn(currentAccount);

        Account account = new Account();
        account.setUsername("currentUsername");
        fileDocument.setAccount(account);
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(1);
        when(ruleAccessFileRepository.findByAccountAndFileDocument(currentAccount, fileDocument))
                .thenReturn(ruleAccessFile);

        Common common = new Common();
        common.setTypeSubId(2);
        when(commonRepository.findByTypeIdAndTypeSubId(Constants.CommonValue.RULE_ACCESS_FILE, Constants.CommonValue.RULE_ACCESS_FILE_WRITE))
                .thenReturn(common);

        Assert.assertFalse(fileDocumentService.checkRuleAccessDownload(currentUsername, fileDocument));
    }
}