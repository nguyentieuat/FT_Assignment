package com.example.managersharefile.services;

import com.example.managersharefile.entities.Common;
import com.example.managersharefile.repositories.CommonRepository;
import com.example.managersharefile.services.impl.CommonServiceImpl;
import com.example.managersharefile.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class CommonServiceTest {
    @InjectMocks
    private CommonService commonService = new CommonServiceImpl();

    @Mock
    private CommonRepository commonRepository;

    @Test
    public void getCommonByType() {

        int typeId = 1;
        List<Common> list = new ArrayList();
        Common common = new Common();
        list.add(common);

        when(commonRepository.findAllByTypeId(typeId)).thenReturn(list);
        Assert.assertEquals(list, commonService.getCommonByType(typeId));
    }

    @Test
    public void getCommonByTypeLike0() {

        int typeId = 0;
        List<Common> list = new ArrayList();
        when(commonRepository.findAllByTypeId(typeId)).thenReturn(list);
        Assert.assertEquals(list, commonService.getCommonByType(typeId));
    }
}