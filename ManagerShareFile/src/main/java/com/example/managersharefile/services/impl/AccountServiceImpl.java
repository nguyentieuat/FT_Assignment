package com.example.managersharefile.services.impl;

import com.example.managersharefile.entities.Account;
import com.example.managersharefile.repositories.AccountRepository;
import com.example.managersharefile.services.AccountService;
import com.example.managersharefile.services.mapper.AccountMapper;
import com.example.managersharefile.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

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
    public List findAccountByContainUsername(String usernameName) {

        List<Account> Accounts = accountRepository.findAllByUsernameContainingIgnoreCaseAndStatus(usernameName, Constants.Number.ONE);
        List<String> accountList = Accounts.stream().map(Account::getUsername).collect(Collectors.toList());

        return accountList;
    }

}
