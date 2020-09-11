package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.domain.Message;
import com.example.chatapplication.exception.BusinessException;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.AttachmentRepository;
import com.example.chatapplication.repositories.MessageRepository;
import com.example.chatapplication.services.dto.MessageDto;
import com.example.chatapplication.services.impl.MessageServiceImpl;
import com.example.chatapplication.services.mapper.MessageMapper;
import com.example.chatapplication.ultities.FileUtilsUpload;
import com.example.chatapplication.ultities.SecurityUtils;
import org.junit.Assert;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private FileUtilsUpload fileUtilsUpload;

    @InjectMocks
    private MessageService messageService = new MessageServiceImpl();

    @Test
    public void getAllMessage() {
        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 10, sortCreatedDate);

        Message message = new Message();
        message.setId(1l);
        Message message2 = new Message();
        message2.setId(1l);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        messages.add(message2);

        when(messageRepository.findAllByOrderByCreatedDateDesc(pageable)).thenReturn(messages);

        List<Attachment> attachments = new ArrayList<>();

        when(attachmentRepository.findAllByMessage(message)).thenReturn(attachments);
        when(attachmentRepository.findAllByMessage(message2)).thenReturn(attachments);


        MessageDto messageDto = new MessageDto();
        messageDto.setId(1l);
        MessageDto messageDto2 = new MessageDto();
        messageDto2.setId(1l);

        when(messageMapper.toDto(message)).thenReturn(messageDto);
        when(messageMapper.toDto(message2)).thenReturn(messageDto2);

        Assert.assertEquals(messages.size(), messageService.getAllMessage(pageable).size());
    }

    @Test
    public void saveMessage() {
        MessageDto messageDto = new MessageDto();
        messageDto.setCreatedBy("emp001");
        messageDto.setContent("Anh nho em");

        MockMultipartFile uploadFile = new MockMultipartFile("test.txt", "test.txt", null, "content".getBytes());
        List<MultipartFile> files = new ArrayList<>();
        files.add(uploadFile);
        messageDto.setFiles(files);


        Account account = new Account();
        account.setUsername("emp001");

        String username = messageDto.getCreatedBy();
        when(accountRepository.findByUsername(username)).thenReturn(account);

        Message message = new Message();
        message.setAccountSender(account);
        message.setContent("Anh nho em");

        Message messageResult = new Message();
        messageResult.setId(1l);
        messageResult.setAccountSender(account);
        messageResult.setContent("Anh nho em");
        when(messageRepository.save(message)).thenReturn(message);
        Message result = message;

        List<Attachment> attachments = new ArrayList<>();
        Attachment attachment = new Attachment();
        attachments.add(attachment);

        List<Attachment> attachmentResults = new ArrayList<>();
        Attachment attachmentResult = new Attachment();
        attachmentResult.setId(1l);
        attachmentResults.add(attachmentResult);

        messageResult.setAttachments(attachmentResults);

        int numberRecordInDay = 0;
        when(attachmentRepository.countRecordCreatedInDateByUser(any(), eq(username))).thenReturn(numberRecordInDay);
        when(fileUtilsUpload.saveFileUpload(eq(username), eq(numberRecordInDay), any(), eq(files.get(0)))).thenReturn(anyString());

        when(attachmentRepository.saveAll(attachments)).thenReturn(attachmentResults);

        MessageDto resultDto = new MessageDto();
        resultDto.setId(1l);
        resultDto.setContent("Anh nho em");
        resultDto.setUsernameSender(username);

        when(messageMapper.toDto(messageResult)).thenReturn(resultDto);

        Assert.assertEquals(messageDto.getContent(), messageService.saveMessage(messageDto).getContent());
    }

    @Test
    public void findByMessageId() {
        long messageId = 1l;
        Message message = new Message();
        message.setId(messageId);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        List<Attachment> attachments = new ArrayList<>();
        when(attachmentRepository.findAllByMessage(message)).thenReturn(attachments);

        MessageDto messageDto = new MessageDto();
        messageDto.setId(messageId);
        when(messageMapper.toDto(message)).thenReturn(messageDto);

        Assert.assertEquals(messageId, messageService.findByMessageId(messageId).getId());
    }

    @Test
    public void deleteMessageByID() {
        Optional<String> optionalUsername = Optional.of("emp001");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        Account account = new Account();
        account.setUsername("emp001");

        long messageId = 1l;
        Message message = new Message();
        message.setId(messageId);
        message.setAccountSender(account);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        messageService.deleteMessageByID(messageId);
        doNothing().when(attachmentRepository).deleteAllByMessage(message);
        doNothing().when(messageRepository).deleteById(messageId);
    }

    @Test(expected = BusinessException.class)
    public void deleteMessageByIDNotPermiss() {
        Optional<String> optionalUsername = Optional.of("emp002");
        PowerMockito.mockStatic(SecurityUtils.class);
        when(SecurityUtils.getAccountCurrentUserLogin()).thenReturn(optionalUsername);

        Account account = new Account();
        account.setUsername("emp001");

        long messageId = 1l;
        Message message = new Message();
        message.setId(messageId);
        message.setAccountSender(account);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        messageService.deleteMessageByID(messageId);
        doNothing().when(attachmentRepository).deleteAllByMessage(message);
        doNothing().when(messageRepository).deleteById(messageId);
    }

    @Test
    public void findByContent() {
        String keySearch = "aaa0";

        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 10, sortCreatedDate);

        Message message = new Message();
        message.setId(1l);
        Message message2 = new Message();
        message2.setId(1l);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        messages.add(message2);

        when(messageRepository.findAllByContentContainingIgnoreCaseOrderByCreatedDateDesc(keySearch, pageable)).thenReturn(messages);

        List<Attachment> attachments = new ArrayList<>();

        when(attachmentRepository.findAllByMessage(message)).thenReturn(attachments);
        when(attachmentRepository.findAllByMessage(message2)).thenReturn(attachments);


        MessageDto messageDto = new MessageDto();
        messageDto.setId(1l);
        MessageDto messageDto2 = new MessageDto();
        messageDto2.setId(1l);

        when(messageMapper.toDto(message)).thenReturn(messageDto);
        when(messageMapper.toDto(message2)).thenReturn(messageDto2);

        Assert.assertEquals(messages.size(), messageService.findByContent(keySearch, pageable).size());
    }

    @Test
    public void findAllByAccount() {
        Account account = new Account();
        account.setUsername("emp001");

        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 10, sortCreatedDate);

        Message message = new Message();
        message.setId(1l);
        Message message2 = new Message();
        message2.setId(1l);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        messages.add(message2);

        when(messageRepository.findAllByAccountSenderOrderByCreatedDateDesc(account, pageable)).thenReturn(messages);

        List<Attachment> attachments = new ArrayList<>();

        when(attachmentRepository.findAllByMessage(message)).thenReturn(attachments);
        when(attachmentRepository.findAllByMessage(message2)).thenReturn(attachments);


        MessageDto messageDto = new MessageDto();
        messageDto.setId(1l);
        MessageDto messageDto2 = new MessageDto();
        messageDto2.setId(1l);

        when(messageMapper.toDto(message)).thenReturn(messageDto);
        when(messageMapper.toDto(message2)).thenReturn(messageDto2);

        Assert.assertEquals(messages.size(), messageService.findAllByAccount(account, pageable).size());

    }

    @Test
    public void findAllByAccountAndContent() {
        String keySearch = "aaa0";
        Account account = new Account();
        account.setUsername("emp001");

        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 10, sortCreatedDate);

        Message message = new Message();
        message.setId(1l);
        Message message2 = new Message();
        message2.setId(1l);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        messages.add(message2);

        when(messageRepository.findAllByAccountSenderAndContentContainingIgnoreCaseOrderByCreatedDateDesc(account, keySearch, pageable)).thenReturn(messages);

        List<Attachment> attachments = new ArrayList<>();

        when(attachmentRepository.findAllByMessage(message)).thenReturn(attachments);
        when(attachmentRepository.findAllByMessage(message2)).thenReturn(attachments);


        MessageDto messageDto = new MessageDto();
        messageDto.setId(1l);
        MessageDto messageDto2 = new MessageDto();
        messageDto2.setId(1l);

        when(messageMapper.toDto(message)).thenReturn(messageDto);
        when(messageMapper.toDto(message2)).thenReturn(messageDto2);

        Assert.assertEquals(messages.size(), messageService.findAllByAccountAndContent(account, keySearch, pageable).size());

    }

    @Test
    public void loadMoreMessage() {
        String keySearch = "aaa0";

        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 10, sortCreatedDate);
        long lastId = 1l;
        int page = 1;
        int pageSize = pageable.getPageSize();
        List<Message> messages = new ArrayList<>();
        when(messageRepository.findAllByContentContainingIgnoreCaseOrderByCreatedDateDesc(lastId, keySearch, page * pageSize, pageSize)).thenReturn(messages);
        Assert.assertEquals(messages.size(), messageService.loadMoreMessage(lastId, page, keySearch, pageable).size());
    }

    @Test
    public void loadMoreCapture() {

        Sort sortCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(0, 50, sortCreatedDate);

        List<Message> messages = new ArrayList<>();
        String username= "emp001";
        long lastId = 1l;
        int page = 1;
        int pageSize = pageable.getPageSize();
        String keySearch = "anh nhoe m";
        when(messageRepository.findAllByUsernameAndCreatedDateAfterOrderByCreatedDateDesc
                (username, lastId, page * pageSize, keySearch, pageSize)).thenReturn(messages);

        Message message = new Message();
        MessageDto messageDto = new MessageDto();
        when(messageMapper.toDto(message)).thenReturn(messageDto);

        Assert.assertEquals(messages.size(), messageService.loadMoreMessage( username,  lastId,  page,  keySearch, pageable).size());
    }

}