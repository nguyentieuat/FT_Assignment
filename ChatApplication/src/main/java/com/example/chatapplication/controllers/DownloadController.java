package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.services.AttachmentService;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Controller
@Slf4j
public class DownloadController {

    @Autowired
    private AttachmentService attachmentService;

    @Value("${file.path}")
    private String rootDir;


    @GetMapping("/download/{attachmentId}")
    public ResponseEntity downloadFile(@PathVariable long attachmentId, HttpServletResponse response) {

        Attachment attachment = attachmentService.findAttachmentById(attachmentId);

        if (!Objects.isNull(attachment)) {
            String path = new StringBuilder(rootDir)
                    .append(File.separator)
                    .append(attachment.getPathAttachment())
                    .toString()                    ;
            Path fullPath = Paths.get(path);
            File file = fullPath.toFile();
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
                 InputStream inputStream = new BufferedInputStream(byteArrayInputStream)) {
                String CONTENT_DISPOSITION_FORMAT = "attachment; filename=\"%s\"; filename*=UTF-8''%s";

                String fileName = attachment.getFileName();
                String filenameEncode = MimeUtility.encodeText(fileName, String.valueOf(Charset.forName("UTF-8")), null);

                // Set mimeType return
                response.setCharacterEncoding("UTF-8");
                response.setContentType(String.valueOf(MediaType.APPLICATION_OCTET_STREAM));
                // Set info return
                String header = String.format(CONTENT_DISPOSITION_FORMAT, fileName, filenameEncode);

                response.setHeader("Content-disposition", header);
                response.setContentLength((int) attachment.getSize());
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
