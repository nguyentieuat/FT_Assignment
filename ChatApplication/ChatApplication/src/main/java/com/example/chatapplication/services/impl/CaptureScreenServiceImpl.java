package com.example.chatapplication.services.impl;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.CaptureScreen;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.CaptureScreenRepository;
import com.example.chatapplication.services.CaptureScreenService;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import com.example.chatapplication.services.mapper.CaptureScreenMapper;
import com.example.chatapplication.ultities.Constant;
import com.example.chatapplication.ultities.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaptureScreenServiceImpl implements CaptureScreenService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CaptureScreenRepository captureScreenRepository;

    @Autowired
    private CaptureScreenMapper captureScreenMapper;

    @Value("${file.path}")
    private String rootDir;

    @Override
    public void saveCapture(String dataImg) {
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        LocalDateTime now = LocalDateTime.now();
        String dateStrMinute = DateTimeFormatter.ofPattern(Constant.FORMAT_DATE_SAVE_CAPTURE).format(now);

        String path = new StringBuilder()
                .append(Constant.PATH_CAPTURE_DESKTOP)
                .append(File.separator)
                .append(username)
                .append(File.separator)
                .append(dateStrMinute)
                .append(Constant.FILE_NAME_EXTENSION_PNG)
                .toString();

        String filePath = new StringBuilder(rootDir)
                .append(path)
                .toString();

        String data = dataImg.split(Constant.COMMA)[1].replace(Constant.SPACE, Constant.PLUS);
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
            captureScreen.setPath(path);
            captureScreen.setCreatedBy(username);
            captureScreen.setCreatedDate(now);
            captureScreen.setUpdatedBy(username);
            captureScreen.setUpdatedDate(now);

            captureScreenRepository.save(captureScreen);

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public List<CaptureScreenDto> findAllByAccount(Account account, Pageable pageable) {

        List<CaptureScreen> captureScreens = captureScreenRepository.findAllByAccountOrderByCreatedDateDesc(account, pageable);
        List<CaptureScreenDto> captureScreenDtos = captureScreens.stream().map(captureScreenMapper::toDto).collect(Collectors.toList());
        Collections.reverse(captureScreenDtos);
        return captureScreenDtos;
    }

    @Override
    public CaptureScreenDto findCaptureById(Long idCapture) {
        CaptureScreen captureScreen = captureScreenRepository.findById(idCapture).get();

        return captureScreenMapper.toDto(captureScreen);
    }

    @Override
    public List<CaptureScreenDto> findAllByAccountAndCreateDate(Account account, String dateStr, Pageable pageable) {

        LocalDateTime time = LocalDateTime.parse(dateStr);
        List<CaptureScreen> captureScreens = captureScreenRepository.findAllByAccountAndCreatedDateAfterOrderByCreatedDateDesc(account, time, pageable);
        List<CaptureScreenDto> captureScreenDtos = captureScreens.stream().map(captureScreenMapper::toDto).collect(Collectors.toList());
        Collections.reverse(captureScreenDtos);
        return captureScreenDtos;
    }

    @Override
    public List<CaptureScreenDto> loadMoreCapture(String username, long lastId, int page, String createDateStr, Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.FORMAT_DATE_TIME);
        if (!createDateStr.isEmpty()) {
            LocalDateTime time = LocalDateTime.parse(createDateStr);
            createDateStr = time.format(formatter);
        } else {
            createDateStr = LocalDateTime.now().format(formatter);
        }
        int pageSize = pageable.getPageSize();
        List<CaptureScreen> captureScreens = captureScreenRepository.findAllByUsernameAndCreatedDateAfterOrderByCreatedDateDesc
                (username, lastId, page * pageSize, createDateStr, pageSize);
        List<CaptureScreenDto> captureScreenDtos = captureScreens.stream().map(captureScreenMapper::toDto).collect(Collectors.toList());
        Collections.reverse(captureScreenDtos);
        return captureScreenDtos;
    }


}
