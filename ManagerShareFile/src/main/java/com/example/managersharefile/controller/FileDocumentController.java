package com.example.managersharefile.controller;

import com.example.managersharefile.utils.SecurityUtils;
import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.exception.BusinessException;
import com.example.managersharefile.services.FileDocumentService;
import com.example.managersharefile.services.RuleAccessFileService;
import com.example.managersharefile.services.dto.FileDocumentDto;
import com.example.managersharefile.services.dto.ResponseEntityDto;
import com.example.managersharefile.services.dto.RuleAccessFileDto;
import com.example.managersharefile.services.mapper.FileDocumentMapper;
import com.example.managersharefile.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@Slf4j
public class FileDocumentController {
    @Autowired
    private FileDocumentService fileDocumentService;

    @Autowired
    private RuleAccessFileService ruleAccessFileService;

    @Autowired
    private FileDocumentMapper fileDocumentMapper;

    /**
     * Get file document by file id.
     * @param fileId
     * @return
     */
    @GetMapping("/file/{fileId}")
    public ResponseEntity getFileById(@PathVariable int fileId) {

        FileDocument fileDocument = fileDocumentService.findByFileId(fileId);

        if (!Objects.isNull(fileDocument)) {
            return ResponseEntity.ok(fileDocumentMapper.toDto(fileDocument));
        }
        return new ResponseEntity(fileId, HttpStatus.NOT_FOUND);

    }

    /**
     * Upload a file.
     * @param fileDocumentDto
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@ModelAttribute FileDocumentDto fileDocumentDto) {

        String accountCurrentUserLogin = SecurityUtils.getAccountCurrentUserLogin().get();

        FileDocumentDto fileDocumentDtoResult = null;
        try {
            fileDocumentDtoResult = fileDocumentService.saveInfoFile(fileDocumentDto, accountCurrentUserLogin);

        } catch (FileUploadException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.unprocessableEntity().body(fileDocumentDto);
        }

        if (Objects.isNull(fileDocumentDtoResult)) {
            return ResponseEntity.unprocessableEntity().body(fileDocumentDto);
        }

        return ResponseEntity.ok(fileDocumentDtoResult);
    }

    /**
     * Get all file by conditions.
     * @param fileName
     * @param category
     * @param status
     * @param pageable
     * @return
     */
    @GetMapping("/getAllFile")
    public ResponseEntity getAllFile(@RequestParam(value = Constants.Attribute.FILE_NAME, defaultValue = Constants.BLANK) String fileName,
                                     @RequestParam(value = Constants.Attribute.CATEGORY, defaultValue = Constants.Number.ZERO + Constants.BLANK) int category,
                                     @RequestParam(value = Constants.Attribute.STATUS, defaultValue = Constants.Number.NEGATIVE_ONE + Constants.BLANK) int status,
                                     @PageableDefault @SortDefault.SortDefaults({@SortDefault(value = Constants.Attribute.CREATED_DATE, direction = Sort.Direction.DESC),
                                             @SortDefault(value = Constants.Attribute.FILE_NAME, direction = Sort.Direction.ASC)})
                                             Pageable pageable) {

        Page page = fileDocumentService.getAllFile(fileName, category, status, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Add rule access file for users.
     * @param ruleAccessFileDtos
     * @return
     */
    @PostMapping("/addRuleAccessFiles")
    public ResponseEntity addRule(@RequestBody List<RuleAccessFileDto> ruleAccessFileDtos) {

        try {
            List<RuleAccessFileDto> ruleAccessFileDtosResult = ruleAccessFileService.addRule(ruleAccessFileDtos);
            return ResponseEntity.ok(ruleAccessFileDtosResult);
        } catch (BusinessException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.unprocessableEntity().body(ruleAccessFileDtos);
        }
    }

    /**
     * Add rule access file for user.
     * @param ruleAccessFileDto
     * @return
     */
    @PostMapping("/addRuleAccessFile")
    public ResponseEntity addRule(@RequestBody RuleAccessFileDto ruleAccessFileDto) {

        try {
            RuleAccessFileDto ruleAccessFileDtoResult = ruleAccessFileService.addRule(ruleAccessFileDto);
            return ResponseEntity.ok(ruleAccessFileDtoResult);
        } catch (BusinessException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.unprocessableEntity().body(ruleAccessFileDto);
        }

    }

    /**
     * Remove rule access file for user.
     * @param ruleAccessFileDto
     * @return
     */
    @DeleteMapping("/removeRuleAccessFile")
    public ResponseEntity removeRule(@RequestBody RuleAccessFileDto ruleAccessFileDto) {
        ResponseEntityDto responseResult = new ResponseEntityDto();
        try {
            Boolean result = ruleAccessFileService.removeRule(ruleAccessFileDto);
            if (result) {
                responseResult.setMessage("Revoke success");
                responseResult.setStatus(HttpStatus.OK.value());
                return ResponseEntity.ok(responseResult);
            }
        } catch (BusinessException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return ResponseEntity.unprocessableEntity().body(ruleAccessFileDto);
    }

    /**
     * Remove rule access file for users.
     * @param ruleAccessFileDtos
     * @return
     */
    @DeleteMapping("/removeRuleAccessFiles")
    public ResponseEntity removeRule(@RequestBody List<RuleAccessFileDto> ruleAccessFileDtos) {
        ResponseEntityDto responseResult = new ResponseEntityDto();
        try {
            Boolean result = ruleAccessFileService.removeRule(ruleAccessFileDtos);
            if (result) {
                responseResult.setMessage("Revoke success");
                responseResult.setStatus(HttpStatus.OK.value());
                return ResponseEntity.ok(responseResult);
            }
        } catch (BusinessException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return ResponseEntity.unprocessableEntity().body(ruleAccessFileDtos);
    }

    /**
     * Inactive files.
     * @param fileIds
     * @return
     */
    @DeleteMapping("/inActiveFile/{fileIds}")
    public ResponseEntity inactiveFile(@PathVariable Integer[] fileIds) {
        ResponseEntityDto responseResult = new ResponseEntityDto();
        try {
            Boolean result = fileDocumentService.inactiveFile(fileIds);
            if (result) {
                responseResult.setMessage("Inactive File success");
                responseResult.setStatus(HttpStatus.OK.value());
                return ResponseEntity.ok(responseResult);
            }
        } catch (BusinessException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.unprocessableEntity().body(fileIds);
    }

    /**
     * Active files.
     * @param fileIds
     * @return
     */
    @PutMapping("/activeFile/{fileIds}")
    public ResponseEntity activeFile(@PathVariable Integer[] fileIds) {
        ResponseEntityDto responseResult = new ResponseEntityDto();
        try {
            Boolean result = fileDocumentService.activeFile(fileIds);
            if (result) {
                responseResult.setMessage("Active File success");
                responseResult.setStatus(HttpStatus.OK.value());
                return ResponseEntity.ok(responseResult);
            }
        } catch (BusinessException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.unprocessableEntity().body(fileIds);
    }

    /**
     * Get all files are shared.
     * @param fileName
     * @param category
     * @param pageable
     * @return
     */
    @GetMapping("/filesShared")
    public ResponseEntity getFileShared(@RequestParam(value = Constants.Attribute.FILE_NAME, defaultValue = Constants.BLANK) String fileName,
                                        @RequestParam(value = Constants.Attribute.CATEGORY, defaultValue = Constants.Number.ZERO + Constants.BLANK) int category,
                                        @PageableDefault @SortDefault.SortDefaults({@SortDefault(value = Constants.Attribute.CREATED_DATE, direction = Sort.Direction.DESC),
                                                @SortDefault(value = Constants.Attribute.FILE_NAME, direction = Sort.Direction.ASC)})
                                                Pageable pageable) {
        Page<FileDocumentDto> fileDocumentDtos = fileDocumentService.getFileShared(fileName, category, pageable);

        return ResponseEntity.ok(fileDocumentDtos);
    }
}

