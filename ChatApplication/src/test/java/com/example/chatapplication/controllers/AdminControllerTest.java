package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.CaptureScreenService;
import com.example.chatapplication.services.JwtUserDetailsService;
import com.example.chatapplication.services.MessageService;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.JwtTokenUtil;
import com.example.chatapplication.ultities.SecurityUtils;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

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

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/manager-chat?page=0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/manager-chat"))
                .andExpect(request().attribute("accountDtos", hasSize(1)));
    }

    @Test
    public void getAllMessageByUsername() {
    }

    @Test
    public void getAllCaptureByUsername() {
    }

    @Test
    public void searchUserTabMessage() {
    }

    @Test
    public void searchUserTabCapture() {
    }

    @Test
    public void loadMoreCapture() {
    }

}