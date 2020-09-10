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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityUtils.class, FileCopyUtils.class})
public class AdminControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private CaptureScreenService captureScreenService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;

    @Mock
    private AccountService accountService;

    @InjectMocks
    AdminController adminController;

    MockMvc mockMvc;

    @Before
    public void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(adminController)
                .setViewResolvers(viewResolver())
                .build();
    }

    private ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("classpath:templates/");
        viewResolver.setSuffix(".html");

        return viewResolver;
    }

    @Test
    public void chatApplication() throws Exception {
        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = new Account();
        when(accountService.getAccountByUsername(username)).thenReturn(account);

        AccountDto accountDto = new AccountDto();
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(accountDto);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        when(accountService.findAllAccount(pageable)).thenReturn(accountDtos);

//        mockMvc.perform(MockMvcRequestBuilders.get("/admin/manager-chat?page=0"))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/manager-chat"))
//                .andExpect(request().attribute("accountDtos", hasSize(1)));
        Assert.assertEquals("admin/manager-chat", adminController.chatApplication(request, pageable));
    }

    @Test
    public void getAllMessageByUsername() {
        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = new Account();
        account.setUsername(username);
        when(accountService.getAccountByUsername(username)).thenReturn(account);
        String keySearch = "anh nho em";
        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(keySearch);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1l);
        messageDto.setAccountSender(account);
        List<MessageDto> messageDtos = new ArrayList<>();
        messageDtos.add(messageDto);

        when(messageService.findAllByAccount(account, pageable)).thenReturn(messageDtos);
        when(messageService.findAllByAccountAndContent(account, keySearch.trim(), pageable)).thenReturn(messageDtos);
        Assert.assertEquals("admin/common/chat-body", adminController.getAllMessageByUsername(request, username, pageable));
    }

    @Test
    public void getAllMessageByUsername1() {
        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = new Account();
        account.setUsername(username);
        when(accountService.getAccountByUsername(username)).thenReturn(account);
        String keySearch = "";
        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(keySearch);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1l);
        messageDto.setAccountSender(account);
        List<MessageDto> messageDtos = new ArrayList<>();
        messageDtos.add(messageDto);

        when(messageService.findAllByAccount(account, pageable)).thenReturn(messageDtos);
        when(messageService.findAllByAccountAndContent(account, keySearch.trim(), pageable)).thenReturn(messageDtos);
        Assert.assertEquals("admin/common/chat-body", adminController.getAllMessageByUsername(request, username, pageable));
    }


    @Test
    public void getAllCaptureByUsername() {

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = new Account();
        account.setUsername(username);
        when(accountService.getAccountByUsername(username)).thenReturn(account);
        String keySearch = "2020-09-10 01:24:05";
        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(keySearch);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        CaptureScreenDto captureScreenDto = new CaptureScreenDto();
        captureScreenDto.setId(1l);
        captureScreenDto.setAccount(account);

        List<CaptureScreenDto> captureScreenDtos = new ArrayList<>();
        captureScreenDtos.add(captureScreenDto);
        when(captureScreenService.findAllByAccount(account, pageable)).thenReturn(captureScreenDtos);
        when(captureScreenService.findAllByAccountAndCreateDate(account, keySearch.trim(), pageable)).thenReturn(captureScreenDtos);

        Assert.assertEquals("admin/common/capture-body", adminController.getAllCaptureByUsername(request, username, pageable));
    }

    @Test
    public void getAllCaptureByUsername1() {

        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = new Account();
        account.setUsername(username);
        when(accountService.getAccountByUsername(username)).thenReturn(account);
        String keySearch = "";
        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        CaptureScreenDto captureScreenDto = new CaptureScreenDto();
        captureScreenDto.setId(1l);
        captureScreenDto.setAccount(account);

        List<CaptureScreenDto> captureScreenDtos = new ArrayList<>();
        captureScreenDtos.add(captureScreenDto);
        when(captureScreenService.findAllByAccount(account, pageable)).thenReturn(captureScreenDtos);
        when(captureScreenService.findAllByAccountAndCreateDate(account, keySearch.trim(), pageable)).thenReturn(captureScreenDtos);

        Assert.assertEquals("admin/common/capture-body", adminController.getAllCaptureByUsername(request, username, pageable));
    }

    @Test
    public void loadMoreCapture() {
        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = new Account();
        account.setUsername(username);
        when(accountService.getAccountByUsername(username)).thenReturn(account);
        String createDateStr = "2020-09-10 01:24:05";
        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(createDateStr);
        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        CaptureScreenDto captureScreenDto = new CaptureScreenDto();
        captureScreenDto.setId(1l);
        captureScreenDto.setAccount(account);

        List<CaptureScreenDto> captureScreenDtos = new ArrayList<>();
        captureScreenDtos.add(captureScreenDto);
        long lastId = 1l;
        int page = 1;

        when(captureScreenService.loadMoreCapture(username, lastId, page, createDateStr, pageable)).thenReturn(captureScreenDtos);
        Assert.assertEquals("admin/common/capture", adminController.loadMoreCapture(request, username, page, lastId, pageable));
    }


    @Test
    public void searchUserTabMessage() {

        String keySearch = "emp";
        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(keySearch);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        List<AccountDto> accountDtos = new ArrayList<>();
        when(accountService.findAllAccountContainUsername(keySearch, pageable)).thenReturn(accountDtos);
        Assert.assertEquals("admin/common/tab-content-message", adminController.searchUserTabMessage(request, pageable));
    }

    @Test
    public void searchUserTabCapture() {
        String keySearch = "emp";
        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(keySearch);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        List<AccountDto> accountDtos = new ArrayList<>();
        when(accountService.findAllAccountContainUsername(keySearch, pageable)).thenReturn(accountDtos);
        Assert.assertEquals("admin/common/tab-content-capture", adminController.searchUserTabCapture(request, pageable));
    }

}