package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.JwtUserDetailsService;
import com.example.chatapplication.services.dto.JwtLoginRequest;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.JwtTokenUtil;
import com.example.chatapplication.ultities.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private AccountService accountService;

    /**
     * Get page login
     *
     * @return
     */
    @GetMapping(value = {"", "/", "/signin"})
    public String login() {
        return "signin";
    }

    /**
     * Login to application by account, password.
     *
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/signin")
    public String login(JwtLoginRequest authenticationRequest, HttpServletResponse response) throws Exception {
        String username = authenticationRequest.getUsername();

        boolean authenticate = authenticate(username, authenticationRequest.getPassword());

        //if login fail then redirect to page login
        if (!authenticate) {
            return "redirect:/signin";
        }

        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        Cookie cookie = new Cookie(Constants.COOKIE_NAME, token);
        cookie.setPath(Constants.SLACK);
        cookie.setMaxAge(Integer.MAX_VALUE);

        response.addCookie(cookie);

        Account account = accountService.getAccountByUsername(username);
        account.setOnline(true);
        account.setLastLogin(LocalDateTime.now());
        accountService.updateInfoAccount(account);

        return "redirect:/chat-light-mode";
    }

    private boolean authenticate(String username, String password) throws Exception {
        boolean result = false;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            result = true;
        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
            log.error(ExceptionUtils.getStackTrace(e));
        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }


    /**
     * When user click button logout then spring security has processed
     *
     * @return
     */
    @GetMapping("/signout")
    public String logout() {
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = accountService.getAccountByUsername(username);
        account.setOnline(false);
        account.setLastLogout(LocalDateTime.now());
        accountService.updateInfoAccount(account);
        return "redirect:/logout";
    }
}