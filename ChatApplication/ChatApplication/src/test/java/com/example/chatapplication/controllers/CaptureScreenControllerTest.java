package com.example.chatapplication.controllers;

import com.example.chatapplication.services.CaptureScreenService;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import com.example.chatapplication.ultities.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityUtils.class, FileCopyUtils.class})
public class CaptureScreenControllerTest {
    @Mock
    private CaptureScreenService captureScreenService;
    @InjectMocks
    private CaptureScreenController captureScreenController;

    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletOutputStream servletOutputStream;

    @Before
    public void init() {
        ReflectionTestUtils.setField(captureScreenController, // inject into this object
                "rootDir", // assign to this field
                "D:\\ChatApplication\\"); // object to be injected
    }

    @Test
    public void saveDesktopCapture() {
        String dataImg = "asdasdas";
        doNothing().when(captureScreenService).saveCapture(dataImg);
        captureScreenController.saveDesktopCapture(dataImg);
    }

    @Test
    public void loadCaptureScreen() throws IOException {
        long idCapture = 1l;
        CaptureScreenDto captureScreenDto = new CaptureScreenDto();
        captureScreenDto.setId(1l);
        captureScreenDto.setPath("desktop_capture\\emp002\\2020_09_08\\21_36_00.png");

        when(captureScreenService.findCaptureById(idCapture)).thenReturn(captureScreenDto);
        PowerMockito.mockStatic(FileCopyUtils.class);
        when(FileCopyUtils.copy(any(InputStream.class), any(ServletOutputStream.class))).thenReturn(1);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        captureScreenController.loadCaptureScreen(idCapture, response);
    }

    @Test
    public void loadCaptureScreenError() throws IOException {
        long idCapture = 1l;
        CaptureScreenDto captureScreenDto = new CaptureScreenDto();
        captureScreenDto.setId(1l);
        captureScreenDto.setPath("desktop_capture\\emp002\\2020_09_08\\21_36_00.png");

        when(captureScreenService.findCaptureById(idCapture)).thenReturn(captureScreenDto);
        PowerMockito.mockStatic(FileCopyUtils.class);
        when(FileCopyUtils.copy(any(InputStream.class), any(ServletOutputStream.class))).thenThrow(IOException.class);
        when(response.getOutputStream()).thenThrow(IOException.class);

        captureScreenController.loadCaptureScreen(idCapture, response);


    }
}