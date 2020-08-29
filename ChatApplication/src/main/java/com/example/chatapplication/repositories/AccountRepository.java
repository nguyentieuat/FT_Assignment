package com.example.chatapplication.repositories;

import com.example.chatapplication.domain.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByUsername(String username);

    List<Account> findAllByOrderByUsernameAsc(Pageable pageable);
}
