package com.example.managersharefile.controller;

import com.example.managersharefile.utils.SecurityUtils;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.services.FileDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


@RestController
@RequestMapping("/api")
@Slf4j
public class DownloadController {

    @Autowired
    private FileDocumentService fileDocumentService;

    @Value("${upload.path}")
    private String rootDir;

    /**
     * Download file by file id.
     *
     * @param fileId
     * @param response
     * @return
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity downloadFile(@PathVariable int fileId, HttpServletResponse response) {
        FileDocument fileDocument = fileDocumentService.findByFileId(fileId);

        if (!Objects.isNull(fileDocument)) {

            String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();
            if (!fileDocumentService.checkRuleAccessDownload(currentUsername, fileDocument)) {
                return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            }

            String path = new StringBuilder(rootDir)
                    .append(File.separator)
                    .append(fileDocument.getPath())
                    .append(File.separator)
                    .append(fileDocument.getFileName())
                    .toString();
            Path fullPath = Paths.get(path);
            File file = fullPath.toFile();
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
                 InputStream inputStream = new BufferedInputStream(byteArrayInputStream)) {
                    String CONTENT_DISPOSITION_FORMAT = "attachment; filename=\"%s\"; filename*=UTF-8''%s";

                    String fileName = fileDocument.getFileName();
                    String filenameEncode = MimeUtility.encodeText(fileName, String.valueOf(Charset.forName("UTF-8")), null);

                    // Set mimeType return
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType(String.valueOf(MediaType.APPLICATION_OCTET_STREAM));
                    // Set info return
                    String header = String.format(CONTENT_DISPOSITION_FORMAT, fileName, filenameEncode);

                    response.setHeader("Content-disposition", header);
                    response.setContentLength((int) fileDocument.getFileSize());
                    FileCopyUtils.copy(inputStream, response.getOutputStream());
                    return new ResponseEntity(HttpStatus.OK);
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            } finally {
                try {
                    response.getOutputStream().flush();
                    response.flushBuffer();
                } catch (IOException e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
