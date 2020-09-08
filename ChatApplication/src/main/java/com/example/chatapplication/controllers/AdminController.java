package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.CaptureScreenService;
import com.example.chatapplication.services.MessageService;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import com.example.chatapplication.services.dto.MessageDto;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.SecurityUtils;
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
    private AccountService accountService;

    /**
     * Get page manager
     *
     * @return
     */
    @GetMapping(value = {"/manager-chat", "/manager"})
    public String chatApplication(HttpServletRequest request, @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String username = SecurityUtils.getAccountCurrentUserLogin().orElse(null);

        if (!Objects.isNull(username)) {
            Account account = accountService.getAccountByUsername(username);
            AccountDto accountDto = accountMapper.toDto(account);
            request.setAttribute(Constants.NameAttribute.CURRENT_USER, accountDto);
            List<AccountDto> accountDtos = accountService.findAllAccount(pageable);

            request.setAttribute(Constants.NameAttribute.LIST_ACCOUNT, accountDtos);
        }

        return "admin/manager-chat";
    }

    /**
     * Get all message by username
     *
     * @param request
     * @param username
     * @param pageable
     * @return
     */
    @GetMapping(value = {"/getAllMessage/{username}"})
    public String getAllMessageByUsername(HttpServletRequest request, @PathVariable String username,
                                          @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = accountService.getAccountByUsername(username);
        request.setAttribute(Constants.NameAttribute.CURRENT_USER, accountMapper.toDto(account));

        String keySearch = request.getParameter(Constants.KEY_SEARCH);

        List<MessageDto> messageDtoList;
        if (Objects.isNull(keySearch) || keySearch.isEmpty()) {
            messageDtoList = messageService.findAllByAccount(account, pageable);
        } else {
            messageDtoList = messageService.findAllByAccountAndContent(account, keySearch.trim(), pageable);
        }

        messageDtoList.forEach(messageDto -> {
            messageDto.setOwner(messageDto.getAccountSender().getUsername().equalsIgnoreCase(currentUsername));
        });
        request.setAttribute(Constants.KEY_SEARCH, keySearch);
        request.setAttribute(Constants.NameAttribute.MESSAGE_DTO_LIST, messageDtoList);

        return "admin/common/chat-body";
    }

    /**
     * Get all capture screen by username
     *
     * @param request
     * @param username
     * @param pageable
     * @return
     */
    @GetMapping(value = {"/getAllCapture/{username}"})
    public String getAllCaptureByUsername(HttpServletRequest request, @PathVariable String username,
                                          @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = accountService.getAccountByUsername(username);
        request.setAttribute(Constants.NameAttribute.CURRENT_USER, accountMapper.toDto(account));

        String createDateStr = request.getParameter(Constants.KEY_SEARCH);

        List<CaptureScreenDto> captureScreenDtos;
        if (Objects.isNull(createDateStr) || createDateStr.isEmpty()) {
            captureScreenDtos = captureScreenService.findAllByAccount(account, pageable);
        } else {
            captureScreenDtos = captureScreenService.findAllByAccountAndCreateDate(account, createDateStr.trim(), pageable);
        }
        captureScreenDtos.forEach(captureScreenDto -> {
            captureScreenDto.setOwner(captureScreenDto.getAccount().getUsername().equalsIgnoreCase(currentUsername));
        });
        request.setAttribute(Constants.KEY_SEARCH, createDateStr);
        request.setAttribute(Constants.NameAttribute.CAPTURE_DTO_LIST, captureScreenDtos);

        return "admin/common/capture-body";
    }

    /**
     * Search user in screen manage message
     * @param request
     * @param pageable
     * @return
     */
    @GetMapping("/searchUserTabMessage")
    public String searchUserTabMessage(HttpServletRequest request, @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String keySearch = request.getParameter(Constants.KEY_SEARCH).trim();

        List<AccountDto> accountDtos = accountService.findAllAccountContainUsername(keySearch, pageable);

        request.setAttribute(Constants.NameAttribute.LIST_ACCOUNT, accountDtos);
        request.setAttribute(Constants.KEY_SEARCH, keySearch);

        return "admin/common/tab-content-message";
    }

    /**
     * Search user in screen manage capture
     *
     * @param request
     * @return
     */
    @GetMapping("/searchUserTabCapture")
    public String searchUserTabCapture(HttpServletRequest request, @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String keySearch = request.getParameter(Constants.KEY_SEARCH).trim();

        List<AccountDto> accountDtos = accountService.findAllAccountContainUsername(keySearch, pageable);

        request.setAttribute(Constants.NameAttribute.LIST_ACCOUNT, accountDtos);
        request.setAttribute(Constants.KEY_SEARCH, keySearch);

        return "admin/common/tab-content-capture";
    }
}
