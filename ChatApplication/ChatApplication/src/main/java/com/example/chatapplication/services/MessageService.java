package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.exception.BusinessException;
import com.example.chatapplication.services.dto.MessageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    /**
     * Get all message
     *
     * @param pageable
     * @return
     */
    List<MessageDto> getAllMessage(Pageable pageable);

    /**
     * Save message
     *
     * @param messageDto
     * @return
     */
    MessageDto saveMessage(MessageDto messageDto);

    /**
     * Find message by id
     *
     * @param l
     * @return
     */
    MessageDto findByMessageId(long l);

    /**
     * delete message by id
     *
     * @param idMessage
     * @throws BusinessException
     */
    void deleteMessageByID(long idMessage) throws BusinessException;

    /**
     * Find messages by context contain key search
     *
     * @param keySearch
     * @param pageable
     * @return
     */
    List<MessageDto> findByContent(String keySearch, Pageable pageable);

    /**
     * Find message by account owner
     *
     * @param account
     * @param pageable
     * @return
     */
    List<MessageDto> findAllByAccount(Account account, Pageable pageable);

    /**
     * Find message by account owner and context contain key search
     *
     * @param account
     * @param keySearch
     * @param pageable
     * @return
     */
    List<MessageDto> findAllByAccountAndContent(Account account, String keySearch, Pageable pageable);

    /**
     * Load more message in chat screen
     *
     * @param lastId
     * @param page
     * @param keySearch
     * @param pageable
     * @return
     */
    List<MessageDto> loadMoreMessage(long lastId, int page, String keySearch, Pageable pageable);

    /**
     * Load more message from screen manager
     *
     * @param username
     * @param lastId
     * @param page
     * @param keySearch
     * @param pageable
     * @return
     */
    List<MessageDto> loadMoreMessage(String username, long lastId, int page, String keySearch, Pageable pageable);
}
