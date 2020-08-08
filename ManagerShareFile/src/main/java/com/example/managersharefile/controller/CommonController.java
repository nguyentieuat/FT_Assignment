package com.example.managersharefile.controller;

import com.example.managersharefile.services.CommonService;
import com.example.managersharefile.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     * Get all commom by type id.
     * @param typeId
     * @return
     */
    @GetMapping("/types")
    public ResponseEntity getAllCategoryByType(@RequestParam(value = Constants.Attribute.TYPE_ID, defaultValue = Constants.Number.ZERO + Constants.BLANK) int typeId ){

        List categories =  commonService.getCommonByType(typeId);
        return ResponseEntity.ok(categories);
    }

}
