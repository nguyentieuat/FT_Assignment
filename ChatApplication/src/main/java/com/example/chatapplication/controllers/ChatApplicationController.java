package com.example.chatapplication.controllers;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.domain.Attachment;
import com.example.chatapplication.services.AccountService;
import com.example.chatapplication.services.AttachmentService;
import com.example.chatapplication.services.ChatRomService;
import com.example.chatapplication.services.MessageService;
import com.example.chatapplication.services.dto.*;
import com.example.chatapplication.services.mapper.AccountMapper;
import com.example.chatapplication.ultities.Constants;
import com.example.chatapplication.ultities.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private AttachmentService attachmentService;

    @Autowired
    private ChatRomService chatRomService;

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
            request.setAttribute(Constants.NameAttribute.CURRENT_USER, accountDto);
            List<MessageDto> messageDtoList = messageService.getAllMessage(pageable);
            messageDtoList.forEach(messageDto -> {
                messageDto.setOwner(messageDto.getAccountSender().getUsername().equalsIgnoreCase(username));
            });
            request.setAttribute(Constants.NameAttribute.MESSAGE_DTO_LIST, messageDtoList);

            //Develop app, it will has many chat room, and chat room public for every one is a chat room specific where
            // has all everyone.
            ChatRoomDto chatRoomDto = chatRomService.getChatRoomById(Constants.ID_CHAT_ROOM_ALL_USER, pageable);
            request.setAttribute(Constants.NameAttribute.CHAT_ROOM_DTO, chatRoomDto);
        }

        return "chat-light-mode";
    }

    /**
     * Load image avatar
     *
     * @param idAvatar
     * @param response
     */
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
                    response.setContentType(Constants.TYPE_IMAGE);
                    IOUtils.copy(inputStream, response.getOutputStream());
                } catch (IOException e) {
                    log.error("Cann't find avatar " + e);
                }
            }

        }
    }

    /**
     * Process when message has exited file
     *
     * @param messageDto
     * @param request
     * @param pageable
     * @return
     */
    @PostMapping("/app/chat.uploadFile")
    public String chatUploadFile(@ModelAttribute MessageDto messageDto, HttpServletRequest request, @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String username = SecurityUtils.getAccountCurrentUserLogin().orElse(null);

        if (!Objects.isNull(username)) {
            MessageDto messageDtoResult = messageService.saveMessage(messageDto);
            if (Objects.isNull(messageDtoResult)){
                throw new NullPointerException("Error when save message has file attach");
            }

            List<MessageDto> messageDtoList = new ArrayList<>();
            messageDtoList.add(messageDtoResult);
            messageDtoList.forEach(mess -> {
                mess.setOwner(mess.getAccountSender().getUsername().equalsIgnoreCase(username));
            });
            request.setAttribute(Constants.NameAttribute.MESSAGE_DTO_LIST, messageDtoList);
        }
        return "common/chat-content";
    }


    /**
     * Delete message owner
     *
     * @param idMessage
     * @return
     */
    @PostMapping("/deleteMessage/{idMessage}")
    @ResponseBody
    public String deleteMessage(@PathVariable long idMessage) {
        try {
            messageService.deleteMessageByID(idMessage);
            ResponseEntityDto responseEntityDto = new ResponseEntityDto();
            responseEntityDto.setMessage("Delete success message id " + idMessage);
            return "chat-light-mode";
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return null;
        }

    }

    /**
     * Search message
     *
     * @param request
     * @param pageable
     * @return
     */
    @GetMapping("/searchMessage")
    public String searchMessage(HttpServletRequest request, @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        String username = SecurityUtils.getAccountCurrentUserLogin().orElse(null);

        if (!Objects.isNull(username)) {
            String keySearch = request.getParameter(Constants.KEY_SEARCH).trim();

            List<MessageDto> messageDtoList = messageService.findByContent(keySearch, pageable);
            messageDtoList.forEach(messageDto -> {
                messageDto.setOwner(messageDto.getAccountSender().getUsername().equalsIgnoreCase(username));
            });
            request.setAttribute(Constants.KEY_SEARCH, keySearch);
            request.setAttribute(Constants.NameAttribute.MESSAGE_DTO_LIST, messageDtoList);
        }

        return "common/chat-content";
    }

    /**
     * Get account is online
     *
     * @param request
     * @param pageable
     * @return
     */
    @GetMapping("/getUserOnline")
    public String getUserOnline(HttpServletRequest request, @PageableDefault(size = Constants.DEFAULT_SIZE_PAGE) Pageable pageable) {
        List<AccountDto> accountDtos;

        String keySearch = request.getParameter(Constants.KEY_SEARCH);
        if (Objects.isNull(keySearch) || keySearch.isEmpty()) {
            accountDtos = accountService.getAccountOnline(pageable);
        } else {
            accountDtos = accountService.getAccountOnline(keySearch.trim(), pageable);
        }
        request.setAttribute(Constants.KEY_SEARCH, keySearch);
        request.setAttribute(Constants.NameAttribute.LIST_ACCOUNT, accountDtos);
        return "common/tab-content-dialogs";
    }


    /**
     * Process when a message is sent
     *
     * @param messageDto
     * @return
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public MessageDto sendMessage(@Payload MessageDto messageDto) {

        MessageDto result = messageService.saveMessage(messageDto);
        return result;
    }

    /**
     * Process when a user connect
     *
     * @param messageDto
     * @return
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/publicChatRoom")
    public MessageDto joinChatRoom(@Payload MessageDto messageDto, SimpMessageHeaderAccessor headerAccessor) {

        String username = messageDto.getCreatedBy();
        Account account = accountService.getAccountByUsername(username);
        account.setOnline(true);

        accountService.updateInfoAccount(account);

        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", username);


        return messageDto;
    }

}
