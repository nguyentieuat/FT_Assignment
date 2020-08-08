package com.example.managersharefile.services;

import java.util.List;

public interface CommonService {

    /**
     * Get all commons by type id.
     * @param typeId
     * @return
     */
    List getCommonByType(int typeId);
}
