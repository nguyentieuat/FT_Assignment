package com.example.chatapplication.controllers;

import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.JwtUserDetailsService;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private AccountService accountService;

    /**
     * Get page manager
     *
     * @return
     */
    @GetMapping(value = {"/manager-chat"})
    public String chatApplication(HttpServletRequest request, HttpServletResponse response) {

        List<AccountDto> accountDtos =  accountService.findAllAccount();

        request.setAttribute(Constants.NameAttribute.LIST_ACCOUNT, accountDtos);




        return "admin/manage-chat";
    }
}
