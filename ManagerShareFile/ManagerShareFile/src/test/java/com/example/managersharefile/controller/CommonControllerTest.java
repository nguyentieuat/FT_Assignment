package com.example.managersharefile.controller;

import com.example.managersharefile.services.CommonService;
import com.example.managersharefile.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * ProjectName: ManagerShareFile
 * ClassName: CommonControllerTest
 *
 * @author: LuanNT19
 * @since: 7/29/2020
 */
@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class CommonControllerTest {

    @InjectMocks
    private CommonController commonController;

    @Mock
    private CommonService commonService;


    @Test
    public void getAllCategory() {

        int typeId = 1;
        List categories = new ArrayList();

        when(commonService.getCommonByType(typeId)).thenReturn(categories);

        ResponseEntity responseEntity =  ResponseEntity.ok(categories);

        Assert.assertEquals(responseEntity.getStatusCode(),
                commonController.getAllCategoryByType(typeId).getStatusCode());
    }
}