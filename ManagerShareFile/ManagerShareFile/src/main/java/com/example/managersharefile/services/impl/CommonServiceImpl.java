package com.example.managersharefile.services.impl;

import com.example.managersharefile.repositories.CommonRepository;
import com.example.managersharefile.services.CommonService;
import com.example.managersharefile.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonRepository commonRepository;

    @Override
    public List getCommonByType(int typeId) {
        if (typeId == Constants.Number.ZERO){
            return new ArrayList();
        }

       return commonRepository.findAllByTypeId(typeId);
    }
}
