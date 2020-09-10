package com.example.managersharefile.services.mapper;


import com.example.managersharefile.entities.Common;
import com.example.managersharefile.services.dto.CommonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CommonMapper extends EntityMapper<CommonDto, Common> {
    default Common fromId(Integer typeId){
        if (typeId == null) {
            return null;
        }
        Common common = new Common();
        common.setTypeId(typeId);
        return common;

    }
}
