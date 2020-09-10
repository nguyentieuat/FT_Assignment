package com.example.managersharefile.services.mapper;


import com.example.managersharefile.entities.Account;
import com.example.managersharefile.services.dto.AccountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AccountMapper extends EntityMapper<AccountDto, Account> {
    default Account fromId(String username){
        if (username == null) {
            return null;
        }
        Account Account = new Account();
        Account.setUsername(username);
        return Account;

    }
}
