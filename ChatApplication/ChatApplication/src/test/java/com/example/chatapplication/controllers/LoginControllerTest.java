package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.JwtUserDetailsService;
import com.example.chatapplication.services.dto.JwtLoginRequest;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.JwtTokenUtil;
import com.example.chatapplication.ultities.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.Cookie;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private AccountService accountService;

    @Mock
    private UserDetails userDetails;


    MockMvc mockMvc;

    @BeforeEach
    void setup(){

        mockMvc = MockMvcBuilders
                .standaloneSetup(loginController)
                .setViewResolvers(viewResolver())
                .build();
    }

    private ViewResolver viewResolver()
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("classpath:templates/");
        viewResolver.setSuffix(".html");

        return viewResolver;
    }

    @Test
    void login() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/signin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("signin"));

    }

    @Test
    void testLogin() throws Exception {
        JwtLoginRequest authenticationRequest = new JwtLoginRequest();
        authenticationRequest.setUsername("emp001");
        authenticationRequest.setPassword("12345678");

        Account account = new Account();
        String token = jwtTokenUtil.generateToken(userDetails);

        Cookie cookie = new Cookie(Constants.COOKIE_NAME, token);

        Mockito.when(jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(userDetails);
        Mockito.when(accountService.getAccountByUsername("emp001")).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/signin")
                                .cookie(cookie))
                .andExpect(status().isOk());
    }


    @Test
    void logout() throws Exception {

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String currentUsername = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = new Account();
        when(accountService.getAccountByUsername(currentUsername)).thenReturn(account);
        doNothing().when(accountService).updateInfoAccount(any(Account.class));


        mockMvc.perform(MockMvcRequestBuilders.get("/signout"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/logout"));
    }
}