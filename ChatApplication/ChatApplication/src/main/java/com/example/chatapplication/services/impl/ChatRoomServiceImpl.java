package com.example.chatapplication.services.impl;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.domain.ChatRoom;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.AttachmentRepository;
import com.example.chatapplication.repositories.ChatRoomRepository;
import com.example.chatapplication.services.ChatRomService;
import com.example.chatapplication.services.dto.ChatRoomDto;
import com.example.chatapplication.services.mapper.ChatRoomMapper;
import com.example.chatapplication.ultities.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;


    @Autowired
    private ChatRoomMapper chatRoomMapper;

    @Override
    public ChatRoomDto getChatRoomById(long id, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).get();

        //Chat public contain all account is active
        List<Account> accounts = accountRepository.findAllByStatusOrderByUsernameAsc(Constant.Status.ACTIVE, pageable);
        chatRoom.setAccounts(accounts);

        //File attach in room public can set id is room public's id or no set id
        // and to easy process, file attachment no set id.
        List<Attachment> attachments = attachmentRepository.findAllByChatRoomOrChatRoomIsNullOrderByCreatedDateDesc(chatRoom, pageable);
        chatRoom.setAttachments(attachments);

        return chatRoomMapper.toDto(chatRoom);
    }
}
