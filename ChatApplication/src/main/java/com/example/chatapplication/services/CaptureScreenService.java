package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CaptureScreenService {

    /**
     * Save capture image
     *
     * @param dataImg
     */
    void saveCapture(String dataImg);

    /**
     * Get capture by account
     *
     * @param account
     * @param pageable
     * @return
     */
    List<CaptureScreenDto> findAllByAccount(Account account, Pageable pageable);

    /**
     * Find capture by id
     *
     * @param idCapture
     * @return
     */
    CaptureScreenDto findCaptureById(Long idCapture);

    /**
     * Find capture by account and condition created date
     *
     * @param account
     * @param createDate
     * @param pageable
     * @return
     */
    List<CaptureScreenDto> findAllByAccountAndCreateDate(Account account, String createDate, Pageable pageable);

    /**
     * Load more capture
     *
     * @param username
     * @param lastId
     * @param page
     * @param createDateStr
     * @param pageable
     * @return
     */
    List<CaptureScreenDto> loadMoreCapture(String username, long lastId, int page, String createDateStr, Pageable pageable);
}
