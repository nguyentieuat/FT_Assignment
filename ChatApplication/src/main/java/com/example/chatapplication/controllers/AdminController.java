package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.CaptureScreenService;
import com.example.chatapplication.services.JwtUserDetailsService;
import com.example.chatapplication.services.MessageService;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import com.example.chatapplication.services.dto.MessageDto;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private CaptureScreenService captureScreenService;

    @Autowired
    private AccountMapper accountMapper;

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
    public String chatApplication(HttpServletRequest request) {

        List<AccountDto> accountDtos = accountService.findAllAccount();

        request.setAttribute(Constants.NameAttribute.LIST_ACCOUNT, accountDtos);


        return "admin/manage-chat";
    }

    @GetMapping(value = {"/getAllMessage/{username}"})
    public String getAllMessageByUsername(HttpServletRequest request, @PathVariable String username,
                                          @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        Account account = accountService.getAccountByUsername(username);
        request.setAttribute(Constants.NameAttribute.CURRENT_USER, accountMapper.toDto(account));

        String keySearch = request.getParameter(Constants.KEY_SEARCH);

        List<MessageDto> messageDtoList;
        if (Objects.isNull(keySearch) || keySearch.isEmpty()){
            messageDtoList = messageService.findAllByAccount(account, pageable);
        } else {
            messageDtoList = messageService.findAllByAccountAndContent(account, keySearch.trim(), pageable);
        }

        messageDtoList.forEach(messageDto -> {
            messageDto.setOwner(messageDto.getAccountSender().getUsername().equalsIgnoreCase(username));
        });
        request.setAttribute(Constants.NameAttribute.MESSAGE_DTO_LIST, messageDtoList);

        return "admin/common/chat-body";
    }

    @GetMapping(value = {"/getAllCapture/{username}"})
    public String getAllCaptureByUsername(HttpServletRequest request, @PathVariable String username,
                                          @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        Account account = accountService.getAccountByUsername(username);
        request.setAttribute(Constants.NameAttribute.CURRENT_USER, accountMapper.toDto(account));

        String keySearch = request.getParameter(Constants.KEY_SEARCH);

        List<CaptureScreenDto> captureScreenDtos;
        if (Objects.isNull(keySearch) || keySearch.isEmpty()){
            captureScreenDtos = captureScreenService.findAllByAccount(account, pageable);
        } else {
            captureScreenDtos = captureScreenService.findAllByAccountAndCreateDate(account, keySearch.trim(), pageable);
        }
        captureScreenDtos.forEach(captureScreenDto -> {
            captureScreenDto.setOwner(captureScreenDto.getAccount().getUsername().equalsIgnoreCase(username));
        });
        request.setAttribute(Constants.NameAttribute.CAPTURE_DTO_LIST, captureScreenDtos);

        return "admin/common/capture-body";
    }


    @GetMapping("/searchUserTabMessage")
    public String searchUserTabMessage(HttpServletRequest request) {
        String keySearch = request.getParameter(Constants.KEY_SEARCH).trim();

        List<AccountDto> accountDtos = accountService.findAllAccountContainUsername(keySearch);

        request.setAttribute(Constants.NameAttribute.LIST_ACCOUNT, accountDtos);
        request.setAttribute(Constants.KEY_SEARCH, keySearch);

        return "admin/common/tab-content-message";
    }

    @GetMapping("/searchUserTabCapture")
    public String searchUserTabCapture(HttpServletRequest request) {
        String keySearch = request.getParameter(Constants.KEY_SEARCH).trim();

        List<AccountDto> accountDtos = accountService.findAllAccountContainUsername(keySearch);

        request.setAttribute(Constants.NameAttribute.LIST_ACCOUNT, accountDtos);
        request.setAttribute(Constants.KEY_SEARCH, keySearch);

        return "admin/common/tab-content-capture";
    }
}
