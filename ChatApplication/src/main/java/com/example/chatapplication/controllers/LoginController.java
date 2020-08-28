package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.JwtUserDetailsService;
import com.example.chatapplication.services.dto.JwtLoginRequest;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.JwtTokenUtil;
import com.example.chatapplication.ultities.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
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

        authenticate(username, authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        Cookie cookie = new Cookie(Constants.COOKIE_NAME, token);
        cookie.setPath(Constants.SLACK);
        cookie.setMaxAge(Integer.MAX_VALUE);

        response.addCookie(cookie);

        Account account = accountService.getAccountByUsername(username);
        account.setLastLogin(LocalDateTime.now());
        accountService.updateInfoAccount(account);

        return "redirect:/chat-light-mode";
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


    /**
     * @return
     */
    @GetMapping("/signout")
    public String logout() {
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = accountService.getAccountByUsername(username);
        account.setLastLogout(LocalDateTime.now());
        accountService.updateInfoAccount(account);
        return "redirect:/logout";
    }
}