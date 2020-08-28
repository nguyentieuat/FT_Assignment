package com.example.chatapplication.services.impl;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;


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

}
