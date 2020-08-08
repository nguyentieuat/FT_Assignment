package com.example.managersharefile.services;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.entities.RuleAccessFile;
import com.example.managersharefile.exception.BusinessException;
import com.example.managersharefile.repositories.AccountRepository;
import com.example.managersharefile.repositories.FileDocumentRepository;
import com.example.managersharefile.repositories.RuleAccessFileRepository;
import com.example.managersharefile.services.dto.RuleAccessFileDto;
import com.example.managersharefile.services.impl.RuleAccessFileServiceImpl;
import com.example.managersharefile.services.mapper.RuleAccessFileMapper;
import com.example.managersharefile.utils.Constants;
import com.example.managersharefile.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class RuleAccessFileServiceTest {

    @InjectMocks
    private RuleAccessFileService ruleAccessFileService = new RuleAccessFileServiceImpl();

    @Mock
    private RuleAccessFileRepository ruleAccessFileRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FileDocumentRepository fileDocumentRepository;

    @Mock
    private RuleAccessFileMapper ruleAccessFileMapper;


    @Test
    public void addRule() throws BusinessException {
        List ruleAccessFiles = new ArrayList<>();

        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();
        RuleAccessFileDto att1 = new RuleAccessFileDto();
        att1.setFileId(1);
        att1.setUsername("emp002");

        ruleAccessFileDtos.add(att1);


        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        RuleAccessFileDto ruleAccessFileDtoSub = ruleAccessFileDtos.get(0);
        String username = ruleAccessFileDtoSub.getUsername();

        Account accountSub = new Account();
        accountSub.setUsername("emp002");
        when(accountRepository.findByUsernameAndStatus(username, Constants.StatusActive.ACTIVE)).thenReturn(accountSub);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileId(1);
        when(fileDocumentRepository.findByAccountAndFileIdAndStatus(currentAccount, ruleAccessFileDtoSub.getFileId(), Constants.StatusActive.ACTIVE)).thenReturn(fileDocument);

        when(ruleAccessFileRepository.saveAll(ruleAccessFiles)).thenReturn(ruleAccessFiles);

        RuleAccessFileDto resultMapper = new RuleAccessFileDto();
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        when(ruleAccessFileMapper.toDto(ruleAccessFile)).thenReturn(resultMapper);

        Assert.assertEquals(ruleAccessFiles.size(),
                ruleAccessFileService.addRule(ruleAccessFileDtos).size());

    }

    @Test(expected = BusinessException.class)
    public void addRuleHasError() throws BusinessException {
        List ruleAccessFiles = new ArrayList<>();

        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();
        RuleAccessFileDto att1 = new RuleAccessFileDto();
        att1.setFileId(1);
        att1.setUsername("emp002");

        ruleAccessFileDtos.add(att1);


        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        RuleAccessFileDto ruleAccessFileDtoSub = ruleAccessFileDtos.get(0);
        String username = ruleAccessFileDtoSub.getUsername();

        when(accountRepository.findByUsernameAndStatus(username, Constants.StatusActive.ACTIVE)).thenReturn(null);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE)).thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileId(1);
        when(fileDocumentRepository.findByAccountAndFileIdAndStatus(currentAccount, ruleAccessFileDtoSub.getFileId(), Constants.StatusActive.ACTIVE)).thenReturn(fileDocument);

        when(ruleAccessFileRepository.saveAll(ruleAccessFiles)).thenReturn(ruleAccessFiles);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        RuleAccessFileDto resultMapper = new RuleAccessFileDto();
        when(ruleAccessFileMapper.toDto(ruleAccessFile)).thenReturn(resultMapper);

        ruleAccessFileService.addRule(ruleAccessFileDtos).size();
    }

    @Test(expected = BusinessException.class)
    public void addRuleHasError2() throws BusinessException {
        List ruleAccessFiles = new ArrayList<>();

        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();
        RuleAccessFileDto att1 = new RuleAccessFileDto();
        att1.setFileId(1);
        att1.setUsername("emp002");

        ruleAccessFileDtos.add(att1);


        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        RuleAccessFileDto ruleAccessFileDtoSub = ruleAccessFileDtos.get(0);
        String username = ruleAccessFileDtoSub.getUsername();

        Account accountSub = new Account();
        accountSub.setUsername("emp002");
        when(accountRepository.findByUsernameAndStatus(username, Constants.StatusActive.ACTIVE)).thenReturn(accountSub);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE)).thenReturn(currentAccount);

        when(fileDocumentRepository.findByAccountAndFileIdAndStatus(currentAccount, ruleAccessFileDtoSub.getFileId(), Constants.StatusActive.ACTIVE)).thenReturn(null);


        when(ruleAccessFileRepository.saveAll(ruleAccessFiles)).thenReturn(ruleAccessFiles);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        RuleAccessFileDto resultMapper = new RuleAccessFileDto();
        when(ruleAccessFileMapper.toDto(ruleAccessFile)).thenReturn(resultMapper);

        ruleAccessFileService.addRule(ruleAccessFileDtos).size();
    }

    @Test
    public void add1Rule() throws BusinessException {
        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        ruleAccessFileDto.setFileId(1);
        ruleAccessFileDto.setUsername("emp002");


        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        RuleAccessFileDto ruleAccessFileDtoSub = ruleAccessFileDto;
        String username = ruleAccessFileDtoSub.getUsername();

        Account accountSub = new Account();
        accountSub.setUsername("emp002");
        when(accountRepository.findByUsernameAndStatus(username, Constants.StatusActive.ACTIVE))
                .thenReturn(accountSub);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE))
                .thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileId(1);
        when(fileDocumentRepository.findByAccountAndFileIdAndStatus(currentAccount, ruleAccessFileDtoSub.getFileId(), Constants.StatusActive.ACTIVE))
                .thenReturn(fileDocument);
        LocalDateTime localDateTime = LocalDateTime.now();
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        ruleAccessFile.setRule(ruleAccessFileDto.getRule());
        ruleAccessFile.setAccount(accountSub);
        ruleAccessFile.setFileDocument(fileDocument);
        ruleAccessFile.setCreatedBy(currentUsername);
        ruleAccessFile.setCreatedDate(localDateTime);
        ruleAccessFile.setUpdatedBy(currentUsername);
        ruleAccessFile.setUpdatedDate(localDateTime);

        when(ruleAccessFileRepository.save(any(RuleAccessFile.class)))
                .thenReturn(ruleAccessFile);

        RuleAccessFileDto resultMapper = new RuleAccessFileDto();
        when(ruleAccessFileMapper.toDto(ruleAccessFile))
                .thenReturn(resultMapper);

        Assert.assertEquals(resultMapper,
                ruleAccessFileService.addRule(ruleAccessFileDto));

    }

    @Test(expected = BusinessException.class)
    public void add1RuleHasError() throws BusinessException {
        RuleAccessFile ruleAccessFile = new RuleAccessFile();

        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        ruleAccessFileDto.setFileId(1);
        ruleAccessFileDto.setUsername("emp002");


        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        RuleAccessFileDto ruleAccessFileDtoSub = ruleAccessFileDto;
        String username = ruleAccessFileDtoSub.getUsername();

        Account accountSub = new Account();
        accountSub.setUsername("emp002");
        when(accountRepository.findByUsernameAndStatus(username, Constants.StatusActive.ACTIVE))
                .thenReturn(accountSub);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE))
                .thenReturn(currentAccount);

        when(fileDocumentRepository.findByAccountAndFileIdAndStatus(currentAccount, ruleAccessFileDtoSub.getFileId(), Constants.StatusActive.ACTIVE))
                .thenReturn(null);

        when(ruleAccessFileRepository.save(ruleAccessFile))
                .thenReturn(ruleAccessFile);

        RuleAccessFileDto resultMapper = new RuleAccessFileDto();
        when(ruleAccessFileMapper.toDto(ruleAccessFile))
                .thenReturn(resultMapper);

        ruleAccessFileService.addRule(ruleAccessFileDto);

    }

    @Test(expected = BusinessException.class)
    public void add1RuleHasError2() throws BusinessException {
        RuleAccessFile ruleAccessFile = new RuleAccessFile();

        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        ruleAccessFileDto.setFileId(1);
        ruleAccessFileDto.setUsername("emp002");


        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        RuleAccessFileDto ruleAccessFileDtoSub = ruleAccessFileDto;
        String username = ruleAccessFileDtoSub.getUsername();

        when(accountRepository.findByUsernameAndStatus(username, Constants.StatusActive.ACTIVE))
                .thenReturn(null);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        currentAccount.setUsername("emp001");
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE))
                .thenReturn(currentAccount);

        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileId(1);
        when(fileDocumentRepository.findByAccountAndFileIdAndStatus(currentAccount, ruleAccessFileDtoSub.getFileId(), Constants.StatusActive.ACTIVE))
                .thenReturn(fileDocument);

        when(ruleAccessFileRepository.save(ruleAccessFile))
                .thenReturn(ruleAccessFile);

        RuleAccessFileDto resultMapper = new RuleAccessFileDto();
        when(ruleAccessFileMapper.toDto(ruleAccessFile))
                .thenReturn(resultMapper);

        ruleAccessFileService.addRule(ruleAccessFileDto);

    }


    @Test
    public void removeRule() throws BusinessException {
        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        ruleAccessFileDto.setFileId(1);
        ruleAccessFileDto.setUsername("emp002");

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE))
                .thenReturn(currentAccount);

        Account account = new Account();
        when(accountRepository.findByUsername(ruleAccessFileDto.getUsername()))
                .thenReturn(account);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, ruleAccessFileDto.getFileId()))
                .thenReturn(fileDocument);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        when(ruleAccessFileRepository.findByAccountAndFileDocument(account, fileDocument))
                .thenReturn(ruleAccessFile);

        doNothing().when(ruleAccessFileRepository).delete(ruleAccessFile);

        Assert.assertTrue(ruleAccessFileService.removeRule(ruleAccessFileDto));
    }

    @Test(expected = BusinessException.class)
    public void removeRuleHasException() throws BusinessException {
        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        ruleAccessFileDto.setFileId(1);
        ruleAccessFileDto.setUsername("emp002");

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE))
                .thenReturn(currentAccount);

        Account account = new Account();
        when(accountRepository.findByUsername(ruleAccessFileDto.getUsername()))
                .thenReturn(null);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, ruleAccessFileDto.getFileId()))
                .thenReturn(fileDocument);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        when(ruleAccessFileRepository.findByAccountAndFileDocument(account, fileDocument))
                .thenReturn(ruleAccessFile);

        doNothing().when(ruleAccessFileRepository).delete(ruleAccessFile);

        ruleAccessFileService.removeRule(ruleAccessFileDto);
    }

    @Test(expected = BusinessException.class)
    public void removeRuleHasException2() throws BusinessException {
        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        ruleAccessFileDto.setFileId(1);
        ruleAccessFileDto.setUsername("emp002");

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account currentAccount = new Account();
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE))
                .thenReturn(currentAccount);

        Account account = new Account();
        when(accountRepository.findByUsername(ruleAccessFileDto.getUsername()))
                .thenReturn(account);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, ruleAccessFileDto.getFileId()))
                .thenReturn(null);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        when(ruleAccessFileRepository.findByAccountAndFileDocument(account, fileDocument))
                .thenReturn(ruleAccessFile);

        doNothing().when(ruleAccessFileRepository).delete(ruleAccessFile);

        ruleAccessFileService.removeRule(ruleAccessFileDto);
    }


    @Test
    public void testRemoveRule() throws BusinessException {
        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();
        RuleAccessFileDto att1 = new RuleAccessFileDto();
        att1.setFileId(1);
        att1.setUsername("emp002");
        ruleAccessFileDtos.add(att1);

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        RuleAccessFileDto ruleAccessFileDto = ruleAccessFileDtos.get(0);

        Account currentAccount = new Account();
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE))
                .thenReturn(currentAccount);

        Account account = new Account();
        when(accountRepository.findByUsername(ruleAccessFileDto.getUsername()))
                .thenReturn(account);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, ruleAccessFileDto.getFileId()))
                .thenReturn(fileDocument);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        when(ruleAccessFileRepository.findByAccountAndFileDocument(account, fileDocument))
                .thenReturn(ruleAccessFile);

        List ruleAccessFiles = new ArrayList<>();
        doNothing().when(ruleAccessFileRepository).deleteAll(ruleAccessFiles);

        Assert.assertTrue(ruleAccessFileService.removeRule(ruleAccessFileDtos));
    }

    @Test(expected = BusinessException.class)
    public void testRemoveRuleHasError() throws BusinessException {
        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();
        RuleAccessFileDto att1 = new RuleAccessFileDto();
        att1.setFileId(1);
        att1.setUsername("emp002");
        ruleAccessFileDtos.add(att1);

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        RuleAccessFileDto ruleAccessFileDto = ruleAccessFileDtos.get(0);

        Account currentAccount = new Account();
        when(accountRepository.findByUsernameAndStatus(currentUsername, Constants.StatusActive.ACTIVE))
                .thenReturn(currentAccount);

        Account account = new Account();
        when(accountRepository.findByUsername(ruleAccessFileDto.getUsername()))
                .thenReturn(null);

        FileDocument fileDocument = new FileDocument();
        when(fileDocumentRepository.findByAccountAndFileId(currentAccount, ruleAccessFileDto.getFileId()))
                .thenReturn(fileDocument);

        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        when(ruleAccessFileRepository.findByAccountAndFileDocument(account, fileDocument))
                .thenReturn(ruleAccessFile);

        List ruleAccessFiles = new ArrayList<>();
        doNothing().when(ruleAccessFileRepository).deleteAll(ruleAccessFiles);

        ruleAccessFileService.removeRule(ruleAccessFileDtos);
    }
}