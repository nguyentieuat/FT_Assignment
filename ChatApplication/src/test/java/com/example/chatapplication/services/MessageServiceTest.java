package com.example.chatapplication.services;

import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.AttachmentRepository;
import com.example.chatapplication.repositories.MessageRepository;
import com.example.chatapplication.services.impl.MessageServiceImpl;
import com.example.chatapplication.services.mapper.MessageMapper;
import com.example.chatapplication.ultities.FileUtilsUpload;
import com.example.chatapplication.ultities.SecurityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
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
    }

    @Test
    public void saveMessage() {
    }

    @Test
    public void findByMessageId() {
    }

    @Test
    public void deleteMessageByID() {
    }

    @Test
    public void findByContent() {
    }

    @Test
    public void findAllByAccount() {
    }

    @Test
    public void findAllByAccountAndContent() {
    }
}