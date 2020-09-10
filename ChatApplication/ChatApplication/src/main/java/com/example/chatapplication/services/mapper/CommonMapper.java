package com.example.chatapplication.services.mapper;


import com.example.chatapplication.domain.Common;
import com.example.chatapplication.services.dto.CommonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CommonMapper extends EntityMapper<CommonDto, Common> {
    default Common fromId(Integer typeId) {
        if (typeId == null) {
            return null;
        }
        Common common = new Common();
        common.setTypeId(typeId);
        return common;

    }
}
