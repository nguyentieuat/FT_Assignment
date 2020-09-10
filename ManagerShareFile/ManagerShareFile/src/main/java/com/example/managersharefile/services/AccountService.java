package com.example.managersharefile.services;

import com.example.managersharefile.entities.Account;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface AccountService {
    /**
     * Get account by username.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    Account getAccountByUsername(String username) throws UsernameNotFoundException;

    /**
     * Update info for account.
     * @param Account
     */
    void updateInfoAccount(Account Account);

    /**
     * Get all username not contain current username.
     * @param username
     * @return
     */
    List findAccountByContainUsername(String username);
}
