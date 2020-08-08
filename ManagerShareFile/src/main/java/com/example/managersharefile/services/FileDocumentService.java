package com.example.managersharefile.services;

import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.exception.BusinessException;
import com.example.managersharefile.services.dto.FileDocumentDto;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FileDocumentService {

    /**
     * Save info file.
     * @param fileDocumentDto
     * @param account
     * @return
     * @throws FileUploadException
     */
    FileDocumentDto saveInfoFile(FileDocumentDto fileDocumentDto, String account) throws FileUploadException;

    /**
     * Get a page info of file document.
     * @param fileName
     * @param category
     * @param status
     * @param pageable
     * @return
     */
    Page getAllFile(String fileName, int category, int status, Pageable pageable);

    /**
     * Inactive file.
     * @param fileId
     * @return
     * @throws BusinessException
     */
    Boolean inactiveFile(int fileId) throws BusinessException;

    /**
     * Active file.
     * @param fileId
     * @return
     * @throws BusinessException
     */
    Boolean activeFile(int fileId) throws BusinessException;

    /**
     * Inactive files.
     * @param fileIds
     * @return
     * @throws BusinessException
     */
    Boolean inactiveFile(Integer[] fileIds) throws BusinessException;

    /**
     * Active files.
     * @param fileIds
     * @return
     * @throws BusinessException
     */
    Boolean activeFile(Integer[] fileIds) throws BusinessException;

    /**
     * Get a page info of file document is shared.
     * @param fileName
     * @param category
     * @param pageable
     * @return
     */
    Page<FileDocumentDto> getFileShared(String fileName, int category, Pageable pageable);

    /**
     * Find file document of current account of is shared.
     * @param fileId
     * @return
     */
    FileDocument findByFileId(int fileId);

    /**
     * Check permission download a file document.
     * @param currentUsername
     * @param fileDocument
     * @return
     */
    Boolean checkRuleAccessDownload(String currentUsername, FileDocument fileDocument);
}
