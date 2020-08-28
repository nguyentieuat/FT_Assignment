package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface AccountService {
    /**
     * Get account by username.
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    Account getAccountByUsername(String username) throws UsernameNotFoundException;

    /**
     * Update info for account.
     *
     * @param Account
     */
    void updateInfoAccount(Account Account);
}
