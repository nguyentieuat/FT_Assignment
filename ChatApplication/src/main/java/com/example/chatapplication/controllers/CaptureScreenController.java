package com.example.chatapplication.controllers;

import com.example.chatapplication.services.CaptureScreenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@Slf4j
public class CaptureScreenController {

    @Autowired
    private CaptureScreenService captureScreenService;

    @Value("${file.path}")
    private String rootDir;

    @PostMapping("/saveDesktopCapture")
    @ResponseBody
    public void saveDesktopCapture(@RequestBody String dataImg) {
        captureScreenService.saveCapture(dataImg);
    }
}
