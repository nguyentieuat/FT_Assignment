package com.example.chatapplication.services.impl;

import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.repositories.AttachmentRepository;
import com.example.chatapplication.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public Attachment findAttachmentById(Long idAttachment) {
        return attachmentRepository.findById(idAttachment).orElseGet(null);
    }
}
