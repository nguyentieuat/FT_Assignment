package com.example.chatapplication.controllers;

import com.example.chatapplication.services.CaptureScreenService;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


@Controller
@Slf4j
public class CaptureScreenController {

    @Autowired
    private CaptureScreenService captureScreenService;

    @Value("${file.path}")
    private String rootDir;

    /**
     * Save image caprture screen
     *
     * @param dataImg
     */
    @PostMapping("/saveDesktopCapture")
    @ResponseBody
    public void saveDesktopCapture(@RequestBody String dataImg) {
        if (!Objects.isNull(dataImg) && !dataImg.isEmpty()) {
            captureScreenService.saveCapture(dataImg);
        }
    }

    /**
     * Load image capture on screen
     *
     * @param idCapture
     * @param response
     */
    @GetMapping(value = {"/loadCaptureScreen/{idCapture}"})
    public void loadCaptureScreen(@PathVariable Long idCapture, HttpServletResponse response) {

        CaptureScreenDto captureScreenDto = captureScreenService.findCaptureById(idCapture);
        if (!Objects.isNull(captureScreenDto)) {
            String path = new StringBuilder(rootDir)
                    .append(captureScreenDto.getPath())
                    .toString();

            File file = new File(path);
            if (file.exists()) {
                try (InputStream inputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file))) {
                    response.setContentType("image/jpeg");
                    FileCopyUtils.copy(inputStream, response.getOutputStream());
                } catch (IOException e) {
                    log.error("Can't find avatar " + e);
                } finally {
                    try {
                        response.getOutputStream().flush();
                        response.flushBuffer();
                    } catch (IOException e) {
                        log.error(ExceptionUtils.getStackTrace(e));
                    }
                }
            }

        }
    }
}
