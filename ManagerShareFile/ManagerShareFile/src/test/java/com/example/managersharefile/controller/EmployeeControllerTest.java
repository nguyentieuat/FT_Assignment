package com.example.managersharefile.controller;

import com.example.managersharefile.services.AccountService;
import com.example.managersharefile.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * ProjectName: ManagerShareFile
 * ClassName: EmployeeControllerTest
 *
 * @author: LuanNT19
 * @since: 7/29/2020
 */
@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class EmployeeControllerTest {
    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private AccountService accountService;

    @Test
    public void findAccountOthers() {

        String username = "emp";
        List<String> list = new ArrayList();
        list.add("emp001");
        list.add("emp002");

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        when(accountService.findAccountByContainUsername(username)).thenReturn(list);

       ResponseEntity responseEntity =  ResponseEntity.ok(list);

        Assert.assertEquals(responseEntity.getStatusCode(),
                employeeController.findAccountOthers(username).getStatusCode());

    }
}