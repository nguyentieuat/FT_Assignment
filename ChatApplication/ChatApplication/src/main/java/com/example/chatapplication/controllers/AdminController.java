package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.CaptureScreenService;
import com.example.chatapplication.services.MessageService;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import com.example.chatapplication.services.dto.MessageDto;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.Constant;
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
    public String chatApplication(HttpServletRequest request, @PageableDefault(size = Constant.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String username = SecurityUtils.getAccountCurrentUserLogin().orElse(null);

        if (!Objects.isNull(username)) {
            Account account = accountService.getAccountByUsername(username);
            AccountDto accountDto = accountMapper.toDto(account);
            request.setAttribute(Constant.NameAttribute.CURRENT_USER, accountDto);
            List<AccountDto> accountDtos = accountService.findAllAccount(pageable);

            request.setAttribute(Constant.NameAttribute.LIST_ACCOUNT, accountDtos);
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
                                          @PageableDefault(size = Constant.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = accountService.getAccountByUsername(username);
        request.setAttribute(Constant.NameAttribute.CURRENT_USER, accountMapper.toDto(account));

        String keySearch = request.getParameter(Constant.KEY_SEARCH);

        List<MessageDto> messageDtos;
        if (Objects.isNull(keySearch) || keySearch.isEmpty()) {
            messageDtos = messageService.findAllByAccount(account, pageable);
        } else {
            messageDtos = messageService.findAllByAccountAndContent(account, keySearch.trim(), pageable);
        }

        messageDtos.forEach(messageDto -> {
            messageDto.setOwner(messageDto.getAccountSender().getUsername().equalsIgnoreCase(currentUsername));
        });
        request.setAttribute(Constant.KEY_SEARCH, keySearch);
        request.setAttribute(Constant.NameAttribute.MESSAGE_DTO_LIST, messageDtos);
        request.setAttribute(Constant.NameAttribute.PAGE, Constant.Number.ONE);

        if (!messageDtos.isEmpty()) {
            request.setAttribute(Constant.NameAttribute.LAST_ID, messageDtos.get(messageDtos.size() - 1).getId());
        }

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
                                          @PageableDefault Pageable pageable) {
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = accountService.getAccountByUsername(username);
        request.setAttribute(Constant.NameAttribute.CURRENT_USER, accountMapper.toDto(account));

        String createDateStr = request.getParameter(Constant.KEY_SEARCH);

        List<CaptureScreenDto> captureScreenDtos;
        if (Objects.isNull(createDateStr) || createDateStr.isEmpty()) {
            captureScreenDtos = captureScreenService.findAllByAccount(account, pageable);
        } else {
            captureScreenDtos = captureScreenService.findAllByAccountAndCreateDate(account, createDateStr.trim(), pageable);
        }
        captureScreenDtos.forEach(captureScreenDto -> {
            captureScreenDto.setOwner(captureScreenDto.getAccount().getUsername().equalsIgnoreCase(currentUsername));
        });
        request.setAttribute(Constant.KEY_SEARCH, createDateStr);
        request.setAttribute(Constant.NameAttribute.CAPTURE_DTO_LIST, captureScreenDtos);
        request.setAttribute(Constant.NameAttribute.PAGE, Constant.Number.ONE);

        if (!captureScreenDtos.isEmpty()) {
            request.setAttribute(Constant.NameAttribute.LAST_ID, captureScreenDtos.get(captureScreenDtos.size() - 1).getId());
        }
        return "admin/common/capture-body";
    }

    @GetMapping(value = {"/loadMoreCapture/{username}/{lastId}/{page}"})
    public String loadMoreCapture(HttpServletRequest request, @PathVariable String username, @PathVariable int page, @PathVariable long lastId,
                                  @PageableDefault(size = Constant.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = accountService.getAccountByUsername(username);
        request.setAttribute(Constant.NameAttribute.CURRENT_USER, accountMapper.toDto(account));

        String createDateStr = request.getParameter(Constant.KEY_SEARCH);
        createDateStr = Objects.isNull(createDateStr) ? Constant.BLANK : createDateStr.trim();

        List<CaptureScreenDto> captureScreenDtos = captureScreenService.loadMoreCapture(username, lastId, page, createDateStr, pageable);
        captureScreenDtos.forEach(captureScreenDto -> {
            captureScreenDto.setOwner(captureScreenDto.getAccount().getUsername().equalsIgnoreCase(currentUsername));
        });
        request.setAttribute(Constant.NameAttribute.CAPTURE_DTO_LIST, captureScreenDtos);

        return "admin/common/capture";
    }

    @GetMapping(value = {"/loadMoreMessage/{username}/{lastId}/{page}"})
    public String loadMoreMessage(HttpServletRequest request, @PathVariable String username, @PathVariable int page, @PathVariable long lastId,
                                  @PageableDefault(size = Constant.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = accountService.getAccountByUsername(username);
        request.setAttribute(Constant.NameAttribute.CURRENT_USER, accountMapper.toDto(account));

        String keySearch = request.getParameter(Constant.KEY_SEARCH);
        keySearch = Objects.isNull(keySearch) ? Constant.BLANK : keySearch.trim();

        List<MessageDto> messageDtos = messageService.loadMoreMessage(username, lastId, page, keySearch, pageable);
        messageDtos.forEach(messageDto -> {
            messageDto.setOwner(messageDto.getAccountSender().getUsername().equalsIgnoreCase(currentUsername));
        });
        request.setAttribute(Constant.NameAttribute.MESSAGE_DTO_LIST, messageDtos);

        return "admin/common/message";
    }

    /**
     * Search user in screen manage message
     *
     * @param request
     * @param pageable
     * @return
     */
    @GetMapping("/searchUserTabMessage")
    public String searchUserTabMessage(HttpServletRequest request, @PageableDefault(size = Constant.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String keySearch = request.getParameter(Constant.KEY_SEARCH);
        keySearch = Objects.isNull(keySearch) ? Constant.BLANK : keySearch.trim();

        List<AccountDto> accountDtos = accountService.findAllAccountContainUsername(keySearch, pageable);
        request.setAttribute(Constant.NameAttribute.LIST_ACCOUNT, accountDtos);
        request.setAttribute(Constant.KEY_SEARCH, keySearch);

        return "admin/common/tab-content-message";
    }

    /**
     * Search user in screen manage capture
     *
     * @param request
     * @return
     */
    @GetMapping("/searchUserTabCapture")
    public String searchUserTabCapture(HttpServletRequest request, @PageableDefault(size = Constant.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String keySearch = request.getParameter(Constant.KEY_SEARCH);
        keySearch = Objects.isNull(keySearch) ? Constant.BLANK : keySearch.trim();

        List<AccountDto> accountDtos = accountService.findAllAccountContainUsername(keySearch, pageable);

        request.setAttribute(Constant.NameAttribute.LIST_ACCOUNT, accountDtos);
        request.setAttribute(Constant.KEY_SEARCH, keySearch);

        return "admin/common/tab-content-capture";
    }
}
