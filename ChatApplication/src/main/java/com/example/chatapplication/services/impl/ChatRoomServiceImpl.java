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
import com.example.chatapplication.ultities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    public ChatRoomDto getChatRoomById(long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).get();

        Pageable pageable = PageRequest.of(Constants.Number.ZERO, Constants.DEFAULT_SIZE_PAGE);
        List<Account> accounts = accountRepository.findAllByStatusOrderByUsernameAsc(Constants.Status.ACTIVE, pageable);
        chatRoom.setAccounts(new HashSet<>(accounts));

        List<Attachment> attachments = attachmentRepository.findAllByOrderByCreatedDateDesc(pageable);
        chatRoom.setAttachments(new HashSet<>(attachments));

        return chatRoomMapper.toDto(chatRoom);
    }
}
