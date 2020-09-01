package com.example.chatapplication.ultities;

import com.example.chatapplication.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class FileUtilsUpload {

    @Value("${file.path}")
    private String rootDir;

    public String saveFileUpload(String account, Integer numberRecordInDay, LocalDateTime date, MultipartFile file) throws BusinessException {

        String dateStr = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE_UNDERSCORE).format(date);
        String pathResult = null;
        try {
            String fileName = FilenameUtils.getName(file.getOriginalFilename());
            StringBuilder sbPathResult = new StringBuilder();

            sbPathResult.append(account)
                    .append(File.separator).append(dateStr)
                    .append(File.separator).append(numberRecordInDay + Constants.Number.ONE)
                    .append(File.separator).append(fileName);

            String rootPath = new StringBuilder(rootDir)
                    .append(sbPathResult)
                    .toString();

            File filePath = new File(rootPath);
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }

            Path path = Paths.get(rootPath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Path locationResult = Paths.get(sbPathResult.toString());
            pathResult = locationResult.toString();

        } catch (Exception ex) {
            throw new BusinessException("Could not store file" + file.getOriginalFilename()
                    + ". Please try again!");
        }
        return pathResult;
    }
}
