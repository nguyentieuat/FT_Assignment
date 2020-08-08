package com.example.managersharefile.services.mapper;

import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.services.dto.FileDocumentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface FileDocumentMapper extends EntityMapper<FileDocumentDto, FileDocument> {
    default FileDocument fromId(Integer fileId){
        if (fileId == null) {
            return null;
        }
        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileId(fileId);
        return fileDocument;

    }
}
