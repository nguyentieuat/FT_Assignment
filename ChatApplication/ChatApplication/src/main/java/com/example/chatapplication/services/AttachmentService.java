package com.example.chatapplication.services;

import com.example.chatapplication.domain.Attachment;

public interface AttachmentService {

    /**
     * Find attach by id
     *
     * @param idAvatar
     * @return
     */
    Attachment findAttachmentById(Long idAvatar);
}
