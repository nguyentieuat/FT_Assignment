package com.example.managersharefile.controller;

import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.exception.BusinessException;
import com.example.managersharefile.services.FileDocumentService;
import com.example.managersharefile.services.RuleAccessFileService;
import com.example.managersharefile.services.dto.FileDocumentDto;
import com.example.managersharefile.services.dto.JwtLoginResponse;
import com.example.managersharefile.services.dto.ResponseEntityDto;
import com.example.managersharefile.services.dto.RuleAccessFileDto;
import com.example.managersharefile.services.mapper.FileDocumentMapper;
import com.example.managersharefile.utils.SecurityUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class FileDocumentControllerTest {

    @InjectMocks
    private FileDocumentController fileDocumentController;

    @Mock
    private FileDocumentService fileDocumentService;

    @Mock
    private RuleAccessFileService ruleAccessFileService;

    @Mock
    private FileDocumentMapper fileDocumentMapper;

    @Test
    public void getFileById() throws Exception {

        final int fileId = 1;

        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileId(fileId);
        FileDocumentDto fileDocumentDto = new FileDocumentDto();
        fileDocumentDto.setFileId(fileId);

        when(fileDocumentService.findByFileId(fileId)).thenReturn(fileDocument);

        when(fileDocumentMapper.toDto(fileDocument)).thenReturn(fileDocumentDto);

        ResponseEntity responseEntity = ResponseEntity.ok(fileDocumentDto);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.getFileById(fileId).getStatusCode());
    }

    @Test
    public void getFileByIdReturnNull() throws Exception {
        int fileId = 1;
        when(fileDocumentService.findByFileId(fileId)).thenReturn(null);

        ResponseEntity responseEntity = new ResponseEntity(fileId, HttpStatus.NOT_FOUND);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.getFileById(fileId).getStatusCode());
    }


    @Test
    public void uploadFile() throws FileUploadException {

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String accountCurrentUserLogin = SecurityUtils.getAccountCurrentUserLogin().get();

        FileDocumentDto fileDocumentDto = new FileDocumentDto();
        FileDocumentDto fileDocumentDtoResult = new FileDocumentDto();

        when(fileDocumentService.saveInfoFile(fileDocumentDto, accountCurrentUserLogin)).thenReturn(fileDocumentDtoResult);

        ResponseEntity responseEntity = ResponseEntity.ok(fileDocumentDtoResult);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.uploadFile(fileDocumentDto).getStatusCode());

    }

    @Test
    public void uploadFileHasException() throws FileUploadException {

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String accountCurrentUserLogin = SecurityUtils.getAccountCurrentUserLogin().get();

        FileDocumentDto fileDocumentDto = new FileDocumentDto();

        when(fileDocumentService.saveInfoFile(fileDocumentDto, accountCurrentUserLogin)).thenThrow(FileUploadException.class);

        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().body(fileDocumentDto);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.uploadFile(fileDocumentDto).getStatusCode());

    }

    @Test
    public void uploadFileReturnNull() throws FileUploadException {

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        String accountCurrentUserLogin = SecurityUtils.getAccountCurrentUserLogin().get();

        FileDocumentDto fileDocumentDto = new FileDocumentDto();

        when(fileDocumentService.saveInfoFile(fileDocumentDto, accountCurrentUserLogin)).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().body(fileDocumentDto);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.uploadFile(fileDocumentDto).getStatusCode());

    }

    @Test
    public void getAllFile() {

        String fileName = "";
        int category = 0;
        int status = 1;

        Sort sortDate = Sort.by(Sort.Direction.DESC, "create");
        Sort sortName = Sort.by(Sort.Direction.ASC, "fileName");
        Sort sort = sortDate.and(sortName);

        Pageable pageable = PageRequest.of(0, 10, sort);


        List list = new ArrayList();
        final Page<FileDocumentDto> page = new PageImpl<>(list);
        when(fileDocumentService.getAllFile(fileName, category, status, pageable)).thenReturn(page).thenReturn(page);

        ResponseEntity responseEntity = ResponseEntity.ok(page);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.getAllFile(fileName, category, status, pageable).getStatusCode());
    }

    @Test
    public void addRule() throws BusinessException {

        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();
        List<RuleAccessFileDto> ruleAccessFileDtosResult = new ArrayList<>();

        when(ruleAccessFileService.addRule(ruleAccessFileDtos)).thenReturn(ruleAccessFileDtosResult);

        ResponseEntity responseEntity = ResponseEntity.ok(ruleAccessFileDtosResult);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.addRule(ruleAccessFileDtos).getStatusCode());
    }

    @Test
    public void addRuleHasException() throws BusinessException {

        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();

        when(ruleAccessFileService.addRule(ruleAccessFileDtos)).thenThrow(BusinessException.class);

        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().body(ruleAccessFileDtos);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.addRule(ruleAccessFileDtos).getStatusCode());
    }

    @Test
    public void addRule1DtoHasException() throws BusinessException {

        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();

        when(ruleAccessFileService.addRule(ruleAccessFileDto)).thenThrow(BusinessException.class);

        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().body(ruleAccessFileDto);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.addRule(ruleAccessFileDto).getStatusCode());
    }

    @Test
    public void addRule1Dto() throws BusinessException {

        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        RuleAccessFileDto ruleAccessFileDtoResult = new RuleAccessFileDto();
        when(ruleAccessFileService.addRule(ruleAccessFileDto)).thenReturn(ruleAccessFileDtoResult);

        ResponseEntity responseEntity = ResponseEntity.ok(ruleAccessFileDto);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.addRule(ruleAccessFileDto).getStatusCode());
    }

    @Test
    public void removeRule() throws BusinessException {

        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        when(ruleAccessFileService.removeRule(ruleAccessFileDto)).thenReturn(true);
        ResponseEntityDto responseResult = new ResponseEntityDto();
        responseResult.setMessage("Degrant success");
        responseResult.setStatus(HttpStatus.OK.value());
        ResponseEntity responseEntity = ResponseEntity.ok(responseResult);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.removeRule(ruleAccessFileDto).getStatusCode());
    }

    @Test
    public void removeRuleHasException() throws BusinessException {

        RuleAccessFileDto ruleAccessFileDto = new RuleAccessFileDto();
        when(ruleAccessFileService.removeRule(ruleAccessFileDto)).thenThrow(BusinessException.class);

        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().body(ruleAccessFileDto);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.removeRule(ruleAccessFileDto).getStatusCode());
    }

    @Test
    public void removeRuleList() throws BusinessException {

        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();
        when(ruleAccessFileService.removeRule(ruleAccessFileDtos)).thenReturn(true);
        ResponseEntityDto responseResult = new ResponseEntityDto();
        responseResult.setMessage("Degrant success");
        responseResult.setStatus(HttpStatus.OK.value());
        ResponseEntity responseEntity = ResponseEntity.ok(responseResult);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.removeRule(ruleAccessFileDtos).getStatusCode());
    }

    @Test
    public void removeRuleListHasException() throws BusinessException {

        List<RuleAccessFileDto> ruleAccessFileDtos = new ArrayList<>();
        when(ruleAccessFileService.removeRule(ruleAccessFileDtos)).thenThrow(BusinessException.class);

        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().body(ruleAccessFileDtos);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.removeRule(ruleAccessFileDtos).getStatusCode());
    }

    @Test
    public void inactiveFile() throws BusinessException {
        Integer[] fileIds = new Integer[]{1, 2};

        ResponseEntityDto responseResult = new ResponseEntityDto();
        responseResult.setMessage("Inactive success");
        responseResult.setStatus(HttpStatus.OK.value());

        when(fileDocumentService.inactiveFile(fileIds)).thenReturn(true);
        ResponseEntity responseEntity = ResponseEntity.ok(responseResult);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.inactiveFile(fileIds).getStatusCode());
    }

    @Test
    public void inactiveFileHasException() throws BusinessException {
        Integer[] fileIds = new Integer[]{1, 2};

        when(fileDocumentService.inactiveFile(fileIds)).thenThrow(BusinessException.class);
        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().body(fileIds);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.inactiveFile(fileIds).getStatusCode());
    }

    @Test
    public void activeFile() throws BusinessException {
        Integer[] fileIds = new Integer[]{1, 2};

        ResponseEntityDto responseResult = new ResponseEntityDto();
        responseResult.setMessage("Active success");
        responseResult.setStatus(HttpStatus.OK.value());

        when(fileDocumentService.activeFile(fileIds)).thenReturn(true);
        ResponseEntity responseEntity = ResponseEntity.ok(responseResult);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.activeFile(fileIds).getStatusCode());
    }

    @Test
    public void activeFileHasException() throws BusinessException {
        Integer[] fileIds = new Integer[]{1, 2};

        when(fileDocumentService.activeFile(fileIds)).thenThrow(BusinessException.class);
        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().body(fileIds);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.activeFile(fileIds).getStatusCode());
    }

    @Test
    public void getFileShared() {
        String fileName = "";
        int category = 0;
        int status = 1;

        Sort sortDate = Sort.by(Sort.Direction.DESC, "create");
        Sort sortName = Sort.by(Sort.Direction.ASC, "fileName");
        Sort sort = sortDate.and(sortName);

        Pageable pageable = PageRequest.of(0, 10, sort);

        List list = new ArrayList();
        final Page<FileDocumentDto> page = new PageImpl<>(list);
        when(fileDocumentService.getFileShared(fileName, category, pageable)).thenReturn(page);

        ResponseEntity responseEntity = ResponseEntity.ok(page);

        Assert.assertEquals(responseEntity.getStatusCode(),
                fileDocumentController.getFileShared(fileName, category, pageable).getStatusCode());
    }
}