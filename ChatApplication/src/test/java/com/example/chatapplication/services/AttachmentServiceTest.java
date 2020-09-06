package com.example.chatapplication.services;

import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.repositories.AttachmentRepository;
import com.example.chatapplication.services.impl.AttachmentServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttachmentServiceTest {
    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private AttachmentService attachmentService = new AttachmentServiceImpl();

    @Test
    public void findAttachmentById() {
        long idAttachment = 1l;
        Attachment attachment = new Attachment();
        attachment.setId(idAttachment);
        when(attachmentRepository.findById(idAttachment)).thenReturn(Optional.of(attachment));

        Assert.assertEquals(attachment.getId(), attachmentService.findAttachmentById(idAttachment).getId());
    }
}