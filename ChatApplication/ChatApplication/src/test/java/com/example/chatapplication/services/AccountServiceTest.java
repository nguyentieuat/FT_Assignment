package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Message;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.MessageRepository;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.services.impl.AccountServiceImpl;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.SecurityUtils;
import org.junit.Assert;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService = new AccountServiceImpl();


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
        when(accountRepository.findByUsername(username)).thenReturn(null);
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
    public void findAllAccount() {
        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 10, sortCreatedDate);

        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setUsername("emp001");
        Account account2 = new Account();
        account2.setUsername("emp002");
        Account account3 = new Account();
        account3.setUsername("emp003");

        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);


        Message message = new Message();
        message.setContent("Anh nho em");

        when(messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account1)).thenReturn(message);
        when(messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account2)).thenReturn(message);
        when(messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account3)).thenReturn(message);

        when(accountRepository.findAllByOrderByUsernameAsc(pageable)).thenReturn(accounts);

        AccountDto accountDto1 = new AccountDto();
        accountDto1.setUsername("emp001");
        AccountDto accountDto2 = new AccountDto();
        accountDto2.setUsername("emp002");
        AccountDto accountDto3 = new AccountDto();
        accountDto3.setUsername("emp003");


        when(accountMapper.toDto(account1)).thenReturn(accountDto1);
        when(accountMapper.toDto(account2)).thenReturn(accountDto2);
        when(accountMapper.toDto(account3)).thenReturn(accountDto3);

        Assert.assertEquals(accounts.size(), accountService.findAllAccount(pageable).size());
    }

    @Test
    public void findAllAccountContainUsername() {
        String keySearch = "emp";
        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortCreatedDate);

        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setUsername("emp001");
        Account account2 = new Account();
        account2.setUsername("emp002");
        Account account3 = new Account();
        account3.setUsername("emp003");

        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);


        Message message = new Message();
        message.setContent("Anh nho em");

        when(messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account1)).thenReturn(message);
        when(messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account2)).thenReturn(message);
        when(messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account3)).thenReturn(message);

        when(accountRepository.findAllByUsernameContainingIgnoreCaseOrderByUsernameAsc(keySearch, pageable)).thenReturn(accounts);

        AccountDto accountDto1 = new AccountDto();
        accountDto1.setUsername("emp001");
        AccountDto accountDto2 = new AccountDto();
        accountDto2.setUsername("emp002");
        AccountDto accountDto3 = new AccountDto();
        accountDto3.setUsername("emp003");


        when(accountMapper.toDto(account1)).thenReturn(accountDto1);
        when(accountMapper.toDto(account2)).thenReturn(accountDto2);
        when(accountMapper.toDto(account3)).thenReturn(accountDto3);

        Assert.assertEquals(accounts.size(), accountService.findAllAccountContainUsername(keySearch, pageable).size());

    }

    @Test
    public void getAccountOnline() {
        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = new Account();
        account.setUsername("emp001");
        account.setOnline(true);

        Account account2 = new Account();
        account2.setUsername("emp002");
        account2.setOnline(true);

        when(accountRepository.findByUsername(username)).thenReturn(account);

        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortCreatedDate);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        when(accountRepository.findAllByIsOnlineOrderByUsernameAsc(true, pageable)).thenReturn(accounts);

        Message message = new Message();
        message.setContent("Anh nho em");

        when(messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account)).thenReturn(message);

        AccountDto accountDto1 = new AccountDto();
        accountDto1.setUsername("emp001");

        AccountDto accountDto2 = new AccountDto();
        accountDto2.setUsername("emp002");

        when(accountMapper.toDto(account)).thenReturn(accountDto1);
        when(accountMapper.toDto(account2)).thenReturn(accountDto2);
        accounts.remove(account);
        Assert.assertEquals(accounts.size(), accountService.getAccountOnline(pageable).size());
    }

    @Test
    public void testGetAccountOnline() {
        String keySearch = "emp";

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = new Account();
        account.setUsername("emp001");
        account.setOnline(true);

        Account account2 = new Account();
        account2.setUsername("emp002");
        account2.setOnline(true);

        when(accountRepository.findByUsername(username)).thenReturn(account);

        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortCreatedDate);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        when(accountRepository.findAllByUsernameContainingIgnoreCaseAndIsOnlineOrderByUsernameAsc(keySearch,true, pageable)).thenReturn(accounts);

        Message message = new Message();
        message.setContent("Anh nho em");

        when(messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account)).thenReturn(message);

        AccountDto accountDto1 = new AccountDto();
        accountDto1.setUsername("emp001");

        AccountDto accountDto2 = new AccountDto();
        accountDto2.setUsername("emp002");

        when(accountMapper.toDto(account)).thenReturn(accountDto1);
        when(accountMapper.toDto(account2)).thenReturn(accountDto2);
        accounts.remove(account);
        Assert.assertEquals(accounts.size(), accountService.getAccountOnline(keySearch,pageable).size());
    }
}