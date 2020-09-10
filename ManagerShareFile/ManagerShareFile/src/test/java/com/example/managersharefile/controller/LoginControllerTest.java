package com.example.managersharefile.controller;

import com.example.managersharefile.utils.JwtTokenUtil;
import com.example.managersharefile.entities.Account;
import com.example.managersharefile.services.AccountService;
import com.example.managersharefile.services.JwtUserDetailsService;
import com.example.managersharefile.services.dto.JwtLoginRequest;
import com.example.managersharefile.services.dto.JwtLoginResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

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

    @Test
    public void createAuthenticationToken() throws Exception {
        JwtLoginRequest authenticationRequest = new JwtLoginRequest();
        authenticationRequest.setUsername("emp001");
        authenticationRequest.setPassword("12345678");

        Account account = new Account();
        String token = jwtTokenUtil.generateToken(userDetails);
        ResponseEntity responseEntity = ResponseEntity.ok(new JwtLoginResponse(token));

        Mockito.when(jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(userDetails);
        Mockito.when(accountService.getAccountByUsername("emp001")).thenReturn(account);

        Assert.assertEquals(responseEntity.getStatusCode(),
                loginController.createAuthenticationToken(authenticationRequest).getStatusCode());
    }

    @Test(expected = Exception.class)
    public void createAuthenticationTokenHasDisabledException() throws Exception {
        JwtLoginRequest authenticationRequest = new JwtLoginRequest();
        authenticationRequest.setUsername("emp001");
        authenticationRequest.setPassword("12345678");

        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())))
                .thenThrow(DisabledException.class);

        loginController.createAuthenticationToken(authenticationRequest).getStatusCode();
    }

    @Test(expected = Exception.class)
    public void createAuthenticationTokenHasBadCredentialsException() throws Exception {
        JwtLoginRequest authenticationRequest = new JwtLoginRequest();
        authenticationRequest.setUsername("emp001");
        authenticationRequest.setPassword("12345678");

        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())))
                .thenThrow(BadCredentialsException.class);

        loginController.createAuthenticationToken(authenticationRequest).getStatusCode();
    }
}
