package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.AttachmentService;
import com.example.chatapplication.services.ChatRomService;
import com.example.chatapplication.services.MessageService;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.services.dto.ChatRoomDto;
import com.example.chatapplication.services.dto.MessageDto;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.Constant;
import com.example.chatapplication.ultities.SecurityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityUtils.class, FileCopyUtils.class})

public class ChatApplicationControllerTest {
    @Mock
    private AccountService accountService;

    @Mock
    private MessageService messageService;

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private ChatRomService chatRomService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private ServletOutputStream servletOutputStream;
    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @InjectMocks
    private ChatApplicationController chatApplicationController;

    @Before
    public void init() {
        ReflectionTestUtils.setField(chatApplicationController, // inject into this object
                "rootDir", // assign to this field
                "D:\\ChatApplication\\"); // object to be injected
    }


    @Test
    public void chatApplication() {
        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = new Account();
        account.setUsername(username);
        when(accountService.getAccountByUsername(username)).thenReturn(account);
        AccountDto accountDto = new AccountDto();
        accountDto.setUsername(username);
        when(accountMapper.toDto(account)).thenReturn(accountDto);
        MessageDto messageDto = new MessageDto();
        messageDto.setAccountSender(account);
        List<MessageDto> messageDtoList = new ArrayList<>();
        messageDtoList.add(messageDto);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);
        when(messageService.getAllMessage(pageable)).thenReturn(messageDtoList);

        ChatRoomDto chatRoomDto = new ChatRoomDto();
        when(chatRomService.getChatRoomById(Constant.ID_CHAT_ROOM_ALL_USER, pageable)).thenReturn(chatRoomDto);

        Assert.assertEquals("chat-light-mode", chatApplicationController.chatApplication(request, pageable));
    }

    @Test
    public void loadMore() {
        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        List<MessageDto> messageDtoList = new ArrayList<>();
        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);
        long lastId = 1l;
        int page = 1;
        String keySearch = "2020-09-10 01:24:05";

        when(messageService.loadMoreMessage(lastId, page, keySearch, pageable)).thenReturn(messageDtoList);

        Assert.assertEquals("common/chat-content", chatApplicationController.loadMore(request, page, lastId, pageable));
    }


    @Test
    public void getAvatar() throws IOException {
        PowerMockito.mockStatic(FileCopyUtils.class);
        Long attachmentId = 1l;
        Attachment attachment = new Attachment();
        attachment.setId(attachmentId);
        attachment.setFileName("17_31_14.png");
        attachment.setPathAttachment("desktop_capture\\emp001\\2020_09_02\\17_31_14.png");


        when(attachmentService.findAttachmentById(attachmentId)).thenReturn(attachment);

        when(FileCopyUtils.copy(any(InputStream.class), any(ServletOutputStream.class))).thenReturn(1);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        chatApplicationController.getAvatar(1l, response);
    }

    @Test
    public void getAvatarError() throws IOException {
        PowerMockito.mockStatic(FileCopyUtils.class);
        Long attachmentId = 1l;
        Attachment attachment = new Attachment();
        attachment.setId(attachmentId);
        attachment.setFileName("17_31_14.png");
        attachment.setPathAttachment("desktop_capture\\emp001\\2020_09_02\\17_31_14.png");


        when(attachmentService.findAttachmentById(attachmentId)).thenReturn(attachment);

        when(FileCopyUtils.copy(any(InputStream.class), any(ServletOutputStream.class))).thenThrow(IOException.class);
        when(response.getOutputStream()).thenThrow(IOException.class);

        chatApplicationController.getAvatar(1l, response);
    }


    @Test
    public void chatUploadFile() {

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = new Account();
        account.setUsername(username);

        MessageDto messageDto = new MessageDto();
        messageDto.setAccountSender(account);

        List<MessageDto> messageDtoList = new ArrayList<>();
        messageDtoList.add(messageDto);

        when(messageService.saveMessage(messageDto)).thenReturn(messageDto);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        Assert.assertEquals("common/chat-content", chatApplicationController.chatUploadFile(messageDto, request, pageable));
    }

    @Test(expected = NullPointerException.class)
    public void chatUploadFileError() {

        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = new Account();
        account.setUsername(username);

        MessageDto messageDto = new MessageDto();
        messageDto.setAccountSender(account);

        List<MessageDto> messageDtoList = new ArrayList<>();
        messageDtoList.add(messageDto);

        when(messageService.saveMessage(messageDto)).thenThrow(NullPointerException.class);

        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        Assert.assertEquals("common/chat-content", chatApplicationController.chatUploadFile(messageDto, request, pageable));
    }

    @Test
    public void deleteMessage() {
        long idMessage = 1l;
        doNothing().when(messageService).deleteMessageByID(idMessage);
        Assert.assertEquals("chat-light-mode", chatApplicationController.deleteMessage(idMessage));
    }

    @Test(expected = Exception.class)
    public void deleteMessageError() {
        long idMessage = 1l;
        doThrow(Exception.class).when(messageService).deleteMessageByID(idMessage);
        Assert.assertNull(chatApplicationController.deleteMessage(idMessage));
    }

    @Test
    public void searchMessage() {
        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);
        Optional<String> optionalUsername = Optional.of("emp001");

        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);
        String username = SecurityUtils.getAccountCurrentUserLogin().get();

        Account account = new Account();
        account.setUsername(username);

        MessageDto messageDto = new MessageDto();
        messageDto.setAccountSender(account);

        List<MessageDto> messageDtoList = new ArrayList<>();
        messageDtoList.add(messageDto);

        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(anyString());
        when(messageService.findByContent(anyString(), pageable)).thenReturn(messageDtoList);

        Assert.assertEquals("common/chat-content", chatApplicationController.searchMessage(request, pageable));
    }

    @Test
    public void getUserOnline() {
        String keySearch = "aaaa";

        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(keySearch);
        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        AccountDto accountDto = new AccountDto();
        accountDto.setUsername("emp001");
        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(accountDto);
        when(accountService.getAccountOnline(keySearch, pageable)).thenReturn(accountDtos);

        Assert.assertEquals("common/tab-content-dialogs", chatApplicationController.getUserOnline(request, pageable));
    }

    @Test
    public void getUserOnline2() {
        when(request.getParameter(Constant.KEY_SEARCH)).thenReturn(anyString());
        Sort sortDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortDate);

        AccountDto accountDto = new AccountDto();
        accountDto.setUsername("emp001");
        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(accountDto);
        when(accountService.getAccountOnline(pageable)).thenReturn(accountDtos);

        Assert.assertEquals("common/tab-content-dialogs", chatApplicationController.getUserOnline(request, pageable));
    }


    @Test
    public void sendMessage() {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1l);
        messageDto.setContent("anh nmho em");
        when(messageService.saveMessage(messageDto)).thenReturn(messageDto);
        Assert.assertEquals(messageDto.getContent(), chatApplicationController.sendMessage(messageDto).getContent());

    }

    @Test
    public void joinChatRoom() {
        String username = "emp001";
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1l);
        messageDto.setContent("anh nmho em");
        messageDto.setCreatedBy(username);
        Account account = new Account();
        account.setUsername(username);
        when(accountService.getAccountByUsername(username)).thenReturn(account);

        Assert.assertEquals(messageDto.getContent(), chatApplicationController.joinChatRoom(messageDto, headerAccessor).getContent());
    }
}