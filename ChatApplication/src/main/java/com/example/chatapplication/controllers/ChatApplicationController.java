package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.AttachmentService;
import com.example.chatapplication.services.MessageService;
import com.example.chatapplication.services.dto.AccountDto;
import com.example.chatapplication.services.dto.MessageDto;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class ChatApplicationController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    private AccountMapper accountMapper;

    @Value("${file.path}")
    private String rootDir;

    /**
     * Get page chat
     *
     * @return
     */
    @GetMapping(value = {"/chat-light-mode", "/chat"})
    public String chatApplication(HttpServletRequest request, @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String username = SecurityUtils.getAccountCurrentUserLogin().orElse(null);

        if (!Objects.isNull(username)) {
            Account account = accountService.getAccountByUsername(username);
            AccountDto accountDto = accountMapper.toDto(account);
            request.setAttribute("currentUser", accountDto);
            List<MessageDto> messageDtoList = messageService.getAllMessage(pageable);
            messageDtoList.forEach(messageDto -> {
                messageDto.setOwner(messageDto.getAccountSender().getUsername().equalsIgnoreCase(username));
            });
            request.setAttribute("messageDtoList", messageDtoList);
        }

        return "chat-light-mode";
    }

    @GetMapping(value = {"/loadImage/{idAvatar}"})
    public void getAvatar(@PathVariable Long idAvatar, HttpServletResponse response) {

        Attachment attachment = attachmentService.findAttachmentById(idAvatar);
        if (!Objects.isNull(attachment)) {
            String path = new StringBuilder(rootDir)
                    .append(attachment.getPathAttachment())
                    .toString();

            File file = new File(path);
            if (file.exists()) {
                try {
                    InputStream inputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
                    response.setContentType("image/jpeg");
                    IOUtils.copy(inputStream, response.getOutputStream());
                } catch (IOException e) {
                    log.error("Cann't find avatar " + e);
                }
            }

        }
    }

    @PostMapping("/app/chat.uploadFile")
    public String chatUploadFile(@ModelAttribute MessageDto messageDto, HttpServletRequest request, @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String username = SecurityUtils.getAccountCurrentUserLogin().orElse(null);

        if (!Objects.isNull(username)) {
            MessageDto messageDtoResult = messageService.saveMessage(messageDto);

            List<MessageDto> messageDtoList = new ArrayList<>();
            messageDtoList.add(messageDtoResult);
            messageDtoList.forEach(mess -> {
                mess.setOwner(mess.getAccountSender().getUsername().equalsIgnoreCase(username));
            });
            request.setAttribute("messageDtoList", messageDtoList);
        }
        return "common/chat-content";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public MessageDto sendMessage(@Payload MessageDto messageDto) {

        MessageDto result = messageService.saveMessage(messageDto);
        return result;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/publicChatRoom")
    public MessageDto joinChatRoom(@Payload MessageDto messageDto, SimpMessageHeaderAccessor headerAccessor) {

        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", messageDto.getCreatedBy());

        return messageDto;
    }

}
