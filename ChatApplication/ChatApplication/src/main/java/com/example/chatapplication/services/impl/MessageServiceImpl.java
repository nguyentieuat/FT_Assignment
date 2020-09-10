package com.example.chatapplication.services.impl;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.domain.Message;
import com.example.chatapplication.exception.BusinessException;
import com.example.chatapplication.repositories.AccountRepository;
import com.example.chatapplication.repositories.AttachmentRepository;
import com.example.chatapplication.repositories.MessageRepository;
import com.example.chatapplication.services.MessageService;
import com.example.chatapplication.services.dto.MessageDto;
import com.example.chatapplication.services.mapper.MessageMapper;
import com.example.chatapplication.ultities.Constant;
import com.example.chatapplication.ultities.FileUtilsUpload;
import com.example.chatapplication.ultities.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private FileUtilsUpload fileUtilsUpload;

    @Override
    public List<MessageDto> getAllMessage(Pageable pageable) {
        List<Message> messages = messageRepository.findAllByOrderByCreatedDateDesc(pageable);
        getAttachmentForMessage(messages);
        List<MessageDto> result = messages.stream().map(messageMapper::toDto).collect(Collectors.toList());
        Collections.reverse(result);
        return result;
    }

    @Override
    public MessageDto saveMessage(MessageDto messageDto) {
        LocalDateTime now = LocalDateTime.now();
        String currentUsername = messageDto.getCreatedBy();

        Account currentAccount = accountRepository.findByUsername(currentUsername);
        Message message = new Message();
        message.setAccountSender(currentAccount);
        message.setContent(messageDto.getContent());
        message.setCreatedBy(currentUsername);
        message.setUpdatedBy(currentUsername);
        message.setCreatedDate(now);
        message.setUpdatedDate(now);
        Message result = messageRepository.save(message);

        if (!Objects.isNull(result)) {
            List<MultipartFile> files = messageDto.getFiles();
            if (!Objects.isNull(files)) {
                List<Attachment> attachments = processSaveFileAttachment(currentUsername, files, now, result);
                result.setAttachments(attachments);
            }
            return messageMapper.toDto(result);
        }

        return null;
    }

    private List<Attachment> processSaveFileAttachment(String username, List<MultipartFile> files, LocalDateTime now, Message message) {
        List<Attachment> attachmentResults = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Integer numberRecordInDay = attachmentRepository.countRecordCreatedInDateByUser(now, username);
                    String path = fileUtilsUpload.saveFileUpload(username, numberRecordInDay, now, file);
                    Attachment attachment = new Attachment();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setPathAttachment(path);
                    attachment.setSize(file.getSize());
                    attachment.setCreatedDate(now);
                    attachment.setCreatedBy(username);
                    attachment.setUpdatedDate(now);
                    attachment.setUpdatedBy(username);
                    attachment.setMessage(message);
                    attachmentResults.add(attachment);
                } catch (BusinessException e) {
                    log.error(ExceptionUtils.getMessage(e));
                }
            }
        }

        return attachmentRepository.saveAll(attachmentResults);
    }

    @Override
    public MessageDto findByMessageId(long messageId) {
        Message message = messageRepository.findById(messageId).get();
        if (!Objects.isNull(message)) {
            List attachments = attachmentRepository.findAllByMessage(message);
            message.setAttachments(attachments);
            return messageMapper.toDto(message);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteMessageByID(long idMessage) throws BusinessException {
        String username = SecurityUtils.getAccountCurrentUserLogin().get();
        Message message = messageRepository.findById(idMessage).get();
        if (message.getAccountSender().getUsername().equalsIgnoreCase(username)) {
            attachmentRepository.deleteAllByMessage(message);
            messageRepository.delete(message);
        } else {
            throw new BusinessException("You are not permission delete this message");
        }

    }

    @Override
    public List<MessageDto> findByContent(String keySearch, Pageable pageable) {

        List<Message> messages = messageRepository.findAllByContentContainingIgnoreCaseOrderByCreatedDateDesc(keySearch, pageable);
        getAttachmentForMessage(messages);
        List<MessageDto> messageDtos = messages.stream().map(messageMapper::toDto).collect(Collectors.toList());
        Collections.reverse(messageDtos);
        return messageDtos;
    }

    @Override
    public List<MessageDto> findAllByAccount(Account account, Pageable pageable) {
        List<Message> messages = messageRepository.findAllByAccountSenderOrderByCreatedDateDesc(account, pageable);
        getAttachmentForMessage(messages);
        List<MessageDto> messageDtos = messages.stream().map(messageMapper::toDto).collect(Collectors.toList());
        Collections.reverse(messageDtos);
        return messageDtos;
    }

    @Override
    public List<MessageDto> findAllByAccountAndContent(Account account, String content, Pageable pageable) {
        List<Message> messages = messageRepository.findAllByAccountSenderAndContentContainingIgnoreCaseOrderByCreatedDateDesc(account, content, pageable);
        getAttachmentForMessage(messages);
        List<MessageDto> messageDtos = messages.stream().map(messageMapper::toDto).collect(Collectors.toList());
        Collections.reverse(messageDtos);
        return messageDtos;
    }

    @Override
    public List<MessageDto> loadMoreMessage(long lastId, int page, String keySearch, Pageable pageable) {

        keySearch = new StringBuilder().append(Constant.PERCENT).append(keySearch).append(Constant.PERCENT).toString();
        int pageSize = pageable.getPageSize();
        List<Message> messages = messageRepository.findAllByContentContainingIgnoreCaseOrderByCreatedDateDesc(lastId,keySearch, page * pageSize, pageSize);
        getAttachmentForMessage(messages);
        List<MessageDto> messageDtos = messages.stream().map(messageMapper::toDto).collect(Collectors.toList());
        Collections.reverse(messageDtos);
        return messageDtos;
    }

    private void getAttachmentForMessage(List<Message> messages) {
        messages.forEach(message -> {
            List<Attachment> attachments = attachmentRepository.findAllByMessage(message);
            message.setAttachments(attachments);
        });
    }

}
