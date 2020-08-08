package com.example.managersharefile.services.mapper;

import com.example.managersharefile.entities.FileDocument;
import com.example.managersharefile.entities.RuleAccessFile;
import com.example.managersharefile.services.dto.RuleAccessFileDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RuleAccessFileMapper extends EntityMapper<RuleAccessFileDto, RuleAccessFile> {
    default RuleAccessFile fromId(Integer id){
        if (id == null) {
            return null;
        }
        RuleAccessFile ruleAccessFile = new RuleAccessFile();
        return ruleAccessFile;
    }
}
