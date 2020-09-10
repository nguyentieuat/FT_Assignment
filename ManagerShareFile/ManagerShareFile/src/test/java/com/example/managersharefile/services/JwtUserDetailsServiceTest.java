package com.example.managersharefile.services;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.repositories.AccountRepository;
import com.example.managersharefile.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * ProjectName: ManagerShareFile
 * ClassName: JwtUserDetailsServiceTest
 *
 * @author: LuanNT19
 * @since: 7/29/2020
 */
@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void loadUserByUsername() {
        String username = "emp001";
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("12345678");
        User user = new User(account.getUsername(), account.getPassword(),
                new ArrayList<>());


        Mockito.when(accountRepository.findByUsername(username)).thenReturn(account);
        Assert.assertEquals(user, jwtUserDetailsService.loadUserByUsername(username));
    }
    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameNotFound() {
        String username = "emp001";
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("12345678");
        User user = new User(account.getUsername(), account.getPassword(),
                new ArrayList<>());

        Mockito.when(accountRepository.findByUsername(username)).thenReturn(null);
        jwtUserDetailsService.loadUserByUsername(username);
    }

}