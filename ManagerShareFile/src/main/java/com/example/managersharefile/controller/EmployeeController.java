package com.example.managersharefile.controller;

import com.example.managersharefile.utils.SecurityUtils;
import com.example.managersharefile.services.AccountService;
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
public class EmployeeController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/getUsernameOthers")
    public ResponseEntity findAccountOthers(@RequestParam(value = Constants.Attribute.ACCOUNT, defaultValue = Constants.BLANK) String username){

        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        List accounts = accountService.findAccountByContainUsername(username);
        accounts.remove(currentUsername);
        return ResponseEntity.ok(accounts);
    }
}
