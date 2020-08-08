package com.example.managersharefile.controller;

import com.example.managersharefile.utils.JwtTokenUtil;
import com.example.managersharefile.entities.Account;
import com.example.managersharefile.services.AccountService;
import com.example.managersharefile.services.JwtUserDetailsService;
import com.example.managersharefile.services.dto.JwtLoginRequest;
import com.example.managersharefile.services.dto.JwtLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("/api")
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
     * Login to application by account, password.
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtLoginRequest authenticationRequest) throws Exception {
        String username = authenticationRequest.getUsername();

        authenticate(username, authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        Account account = accountService.getAccountByUsername(username);
        account.setLastLogin(LocalDateTime.now());
        accountService.updateInfoAccount(account);

        return ResponseEntity.ok(new JwtLoginResponse(token));
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
}
