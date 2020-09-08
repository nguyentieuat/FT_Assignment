package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.ultities.Common;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
@RunWith(PowerMockRunner.class)
public class JwtUserDetailsServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService();


    @Test
    public void loadUserByUsername() {
        String username = "emp001";
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("12345678");
        account.setRole(1);

        when(accountRepository.findByUsername(username)).thenReturn(account);

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + Common.convertNumberToString(account.getRole()));
        UserDetails userDetails = new User(account.getUsername(), account.getPassword(), grantList);

        Assert.assertEquals(username, jwtUserDetailsService.loadUserByUsername(username).getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameError() {
        String username = "emp001";
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("12345678");
        account.setRole(1);

        when(accountRepository.findByUsername(username)).thenReturn(null);

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + Common.convertNumberToString(account.getRole()));
        UserDetails userDetails = new User(account.getUsername(), account.getPassword(), grantList);

        Assert.assertEquals(username, jwtUserDetailsService.loadUserByUsername(username).getUsername());
    }
}