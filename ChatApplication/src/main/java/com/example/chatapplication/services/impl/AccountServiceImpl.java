package com.example.chatapplication.services.impl;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Message;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.MessageRepository;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public Account getAccountByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (Objects.isNull(account)) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return account;
    }

    @Override
    public void updateInfoAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<AccountDto> findAllAccount(Pageable pageable) {
        List<Account> accounts = accountRepository.findAllByOrderByUsernameAsc(pageable);

        accounts.forEach(account -> {
            Message message = messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account);
            Set<Message> messageSet = new HashSet<>();
            messageSet.add(message);
            account.setMessages(messageSet);
        });

        return accounts.stream().map(accountMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> findAllAccountContainUsername(String keySearch, Pageable pageable) {
        List<Account> accounts = accountRepository.findAllByUsernameContainingIgnoreCaseOrderByUsernameAsc(keySearch, pageable);
        accounts.forEach(account -> {
            Message message = messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(account);
            Set<Message> messageSet = new HashSet<>();
            messageSet.add(message);
            account.setMessages(messageSet);
        });
        return accounts.stream().map(accountMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> getAccountOnline(Pageable pageable) {
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = accountRepository.findByUsername(username);
        List<Account> accounts = accountRepository.findAllByIsOnlineOrderByUsernameAsc(true, pageable);
        accounts.remove(account);
        accounts.forEach(acc -> {
            Message message = messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(acc);
            Set<Message> messageSet = new HashSet<>();
            messageSet.add(message);
            acc.setMessages(messageSet);
        });
        return accounts.stream().map(accountMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> getAccountOnline(String keySearch, Pageable pageable) {
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Account account = accountRepository.findByUsername(username);
        List<Account> accounts = accountRepository.findAllByUsernameContainingIgnoreCaseAndIsOnlineOrderByUsernameAsc(keySearch, true, pageable);
        accounts.remove(account);
        accounts.forEach(acc -> {
            Message message = messageRepository.findTop1ByAccountSenderOrderByCreatedDateDesc(acc);
            Set<Message> messageSet = new HashSet<>();
            messageSet.add(message);
            acc.setMessages(messageSet);
        });
        return accounts.stream().map(accountMapper::toDto).collect(Collectors.toList());
    }

}
