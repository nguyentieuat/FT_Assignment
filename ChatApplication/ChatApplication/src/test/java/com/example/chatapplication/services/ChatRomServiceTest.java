package com.example.chatapplication.services;

import com.example.chatapplication.domain.ChatRoom;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.AttachmentRepository;
import com.example.chatapplication.repositories.ChatRoomRepository;
import com.example.chatapplication.services.dto.ChatRoomDto;
import com.example.chatapplication.services.impl.ChatRoomServiceImpl;
import com.example.chatapplication.services.mapper.ChatRoomMapper;
import com.example.chatapplication.ultities.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class ChatRomServiceTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AttachmentRepository attachmentRepository;


    @Mock
    private ChatRoomMapper chatRoomMapper;

    @InjectMocks
    private ChatRomService chatRomService = new ChatRoomServiceImpl();

    @Test
    public void getChatRoomById() {
        long idChatroom = 1l;
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(idChatroom);

        Pageable pageable = PageRequest.of(0, 50);

        when(chatRoomRepository.findById(idChatroom)).thenReturn(Optional.of(chatRoom));

        when(accountRepository.findAllByStatusOrderByUsernameAsc(Constants.Status.ACTIVE, pageable)).thenReturn(new ArrayList<>());

        when(attachmentRepository.findAllByChatRoomOrChatRoomIsNullOrderByCreatedDateDesc(chatRoom, pageable)).thenReturn(new ArrayList<>());

        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setId(idChatroom);
        when(chatRoomMapper.toDto(chatRoom)).thenReturn(chatRoomDto);

        Assert.assertEquals(idChatroom, chatRomService.getChatRoomById(idChatroom, pageable).getId());
    }
}