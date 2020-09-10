package com.example.managersharefile.controller;

import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.services.FileDocumentService;
import com.example.managersharefile.utils.SecurityUtils;
import org.apache.catalina.connector.CoyoteOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * ProjectName: ManagerShareFile
 * ClassName: DownloadControllerTest
 *
 * @author: LuanNT19
 * @since: 7/29/2020
 */
@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityUtils.class, FileCopyUtils.class})
public class DownloadControllerTest {
    @InjectMocks
    private DownloadController downloadController;

    @Mock
    private FileDocumentService fileDocumentService;

    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletOutputStream servletOutputStream;

    @Before
    public void init(){
        ReflectionTestUtils.setField(downloadController, // inject into this object
                "rootDir", // assign to this field
                "D:\\ManagerShareFile"); // object to be injected


        Path path = Paths.get("D:\\ManagerShareFile\\emp001\\document\\2020_07_23\\5\\test.txt");
        File file = new File(path.toString());
        try {
            if (!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadFile() throws IOException {
        PowerMockito.mockStatic(FileCopyUtils.class);
        int fileId = 1 ;

        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileId(fileId);
        fileDocument.setFileName("test.txt");
        fileDocument.setPath("emp001\\document\\2020_07_23\\5");

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        when(fileDocumentService.findByFileId(fileId)).thenReturn(fileDocument);
        when(fileDocumentService.checkRuleAccessDownload(currentUsername, fileDocument)).thenReturn(true);
        when(FileCopyUtils.copy(any(InputStream.class), any(ServletOutputStream.class))).thenReturn(1);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        Assert.assertEquals(responseEntity.getStatusCode(),
                downloadController.downloadFile(fileId, response).getStatusCode());

    }

    @Test
    public void downloadFileNotPermission() {

        FileDocument fileDocument = new FileDocument();

        int fileId = 1 ;

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();


        when(fileDocumentService.findByFileId(fileId)).thenReturn(fileDocument);
        when(fileDocumentService.checkRuleAccessDownload(currentUsername, fileDocument)).thenReturn(false);

       ResponseEntity responseEntity =  new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

        Assert.assertEquals(responseEntity.getStatusCode(),
                downloadController.downloadFile(fileId, response).getStatusCode());


    }

    @Test
    public void downloadFileNotFound() {

        int fileId = 1 ;

        when(fileDocumentService.findByFileId(fileId)).thenReturn(null);

        ResponseEntity responseEntity =  new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        Assert.assertEquals(responseEntity.getStatusCode(),
                downloadController.downloadFile(fileId, response).getStatusCode());


    }
}