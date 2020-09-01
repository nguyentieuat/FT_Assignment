package com.example.chatapplication.services.impl;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.CaptureScreen;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.CaptureScreenRepository;
import com.example.chatapplication.services.CaptureScreenService;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class CaptureScreenServiceImpl implements CaptureScreenService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CaptureScreenRepository captureScreenRepository;

    @Value("${file.path}")
    private String rootDir;

    @Override
    public void saveCapture(String dataImg) {
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        LocalDateTime now = LocalDateTime.now();
        String dateStrMinute = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE_SAVE_CAPTURE).format(now);
        String filePath = new StringBuilder(rootDir)
                .append(Constants.PATH_CAPTURE_DESKTOP)
                .append(File.separator)
                .append(username)
                .append(File.separator)
                .append(dateStrMinute)
                .append(Constants.FILE_NAME_EXTENSION_PNG)
                .toString();

        String data = dataImg.split(Constants.COMMA)[1].replace(" ", "+");
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            // create a buffered image
            BufferedImage image = null;
            byte[] imageByte;

            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(data);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();

            // write the image to a file
            ImageIO.write(image, "png", file);


            Account account = accountRepository.findByUsername(username);
            CaptureScreen captureScreen = new CaptureScreen();
            captureScreen.setAccount(account);
            captureScreen.setPath(filePath);
            captureScreen.setCreatedBy(username);
            captureScreen.setCreatedDate(now);
            captureScreen.setUpdatedBy(username);
            captureScreen.setUpdatedDate(now);

            captureScreenRepository.save(captureScreen);

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
