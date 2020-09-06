package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.JwtUserDetailsService;
import com.example.chatapplication.services.dto.JwtLoginRequest;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
public class AdminLoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AdminLoginController adminLoginController;

    @Mock
    private UserDetails userDetails;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(adminLoginController)
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
    public void login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/signin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/signin"));
    }

    @Test
    public void testLogin() throws Exception {
        JwtLoginRequest authenticationRequest = new JwtLoginRequest();
        authenticationRequest.setUsername("emp001");
        authenticationRequest.setPassword("12345678");

        Account account = new Account();
        String token = jwtTokenUtil.generateToken(userDetails);

        Cookie cookie = new Cookie(Constants.COOKIE_NAME, token);

        Mockito.when(jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(userDetails);
        Mockito.when(accountService.getAccountByUsername("emp001")).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/signin")
                .cookie(cookie))
                .andExpect(status().isOk());
    }

}