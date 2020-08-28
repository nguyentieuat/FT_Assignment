package com.example.chatapplication.services.mapper;


import com.example.chatapplication.domain.CaptureScreen;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CaptureScreenMapper extends EntityMapper<CaptureScreenDto, CaptureScreen> {
    default CaptureScreen fromId(Long id){
        if (id == null) {
            return null;
        }
        CaptureScreen captureScreen = new CaptureScreen();
        captureScreen.setId(id);
        return captureScreen;

    }
}
