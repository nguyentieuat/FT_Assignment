package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.ultities.Common;
import com.example.chatapplication.ultities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (Objects.isNull(account) || account.getStatus() == Constants.Status.INACTIVE) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + Common.convertNumberToString(account.getRole()));
        grantList.add(authority);

        return new User(account.getUsername(), account.getPassword(), grantList);
    }
}