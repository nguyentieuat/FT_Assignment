package com.example.managersharefile.services;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.repositories.AccountRepository;
import com.example.managersharefile.services.impl.AccountServiceImpl;
import com.example.managersharefile.utils.Constants;
import com.example.managersharefile.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountService accountService = new AccountServiceImpl();

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void getAccountByUsername() {
        String username = "emp001";
        Account account = new Account();
        account.setUsername(username);

        when(accountRepository.findByUsername(username)).thenReturn(account);
        Assert.assertEquals(account, accountService.getAccountByUsername(username));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void getAccountByUsernameHasException() {
        String username = "emp001";
        when(accountRepository.findByUsername(username)).thenThrow(UsernameNotFoundException.class);

        accountService.getAccountByUsername(username);
    }

    @Test
    public void updateInfoAccount() {
        Account account = new Account();
        account.setUsername("emp001");

        accountService.updateInfoAccount(account);

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void findAccountByContainUsername() {
        List<Account> list = new ArrayList();
        List<String> result = new ArrayList<>();
        String username = "emp";

        Account account = new Account();
        account.setUsername("emp001");
        Account account1 = new Account();
        account1.setUsername("emp002");
        Account account2 = new Account();
        account2.setUsername("emp003");
        list.add(account);
        list.add(account1);
        list.add(account2);
        result.add("emp001");
        result.add("emp002");
        result.add("emp003");

        when(accountRepository.findAllByUsernameContainingIgnoreCaseAndStatus(username, Constants.Number.ONE))
                .thenReturn(list);

        Assert.assertEquals(result, accountService.findAccountByContainUsername(username));
    }
}