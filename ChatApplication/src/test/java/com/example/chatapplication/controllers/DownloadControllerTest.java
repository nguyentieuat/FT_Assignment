package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.services.AttachmentService;
import com.example.chatapplication.ultities.SecurityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityUtils.class, FileCopyUtils.class})
public class DownloadControllerTest {
    @InjectMocks
    private DownloadController downloadController;

    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletOutputStream servletOutputStream;
    @Mock
    private AttachmentService attachmentService;

    @Before
    public void init() {
        ReflectionTestUtils.setField(downloadController, // inject into this object
                "rootDir", // assign to this field
                "D:\\ChatApplication\\"); // object to be injected
    }

    @Test
    public void downloadFile() throws IOException {
        PowerMockito.mockStatic(FileCopyUtils.class);
        Long attachmentId = 1l;
        Attachment attachment = new Attachment();
        attachment.setId(attachmentId);
        attachment.setFileName("17_31_14.png");
        attachment.setPathAttachment("desktop_capture\\emp001\\2020_09_02\\17_31_14.png");


        when(attachmentService.findAttachmentById(attachmentId)).thenReturn(attachment);

        when(FileCopyUtils.copy(any(InputStream.class), any(ServletOutputStream.class))).thenReturn(1);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        Assert.assertEquals(responseEntity.getStatusCode(),
                downloadController.downloadFile(attachmentId, response).getStatusCode());

    }
    @Test
    public void downloadFileError() throws IOException {
        PowerMockito.mockStatic(FileCopyUtils.class);
        Long attachmentId = 1l;
        Attachment attachment = new Attachment();
        attachment.setId(attachmentId);
        attachment.setFileName("17_31_14.png");
        attachment.setPathAttachment("desktop_capture\\emp001\\2020_09_02\\17_31_14111.png");


        when(attachmentService.findAttachmentById(attachmentId)).thenReturn(attachment);

        when(FileCopyUtils.copy(any(InputStream.class), any(ServletOutputStream.class))).thenReturn(1);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(responseEntity.getStatusCode(),
                downloadController.downloadFile(attachmentId, response).getStatusCode());

    }

}