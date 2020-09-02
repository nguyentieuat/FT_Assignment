package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.dto.AccountDto;
import org.springframework.data.domain.Pageable;
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

    /**
     * Get all account
     *
     * @return
     */
    List<AccountDto> findAllAccount();

    /**
     * Get accounts contain key search
     *
     * @param keySearch
     * @return
     */
    List<AccountDto> findAllAccountContainUsername(String keySearch);

    /**
     * Get all account online
     *
     * @param pageable
     * @return
     */
    List<AccountDto> getAccountOnline(Pageable pageable);

    /**
     * Get account online contain key search
     *
     * @param trim
     * @param pageable
     * @return
     */
    List<AccountDto> getAccountOnline(String trim, Pageable pageable);
}
