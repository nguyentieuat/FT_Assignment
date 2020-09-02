package com.example.chatapplication.services;

import com.example.chatapplication.domain.Account;
import com.example.chatapplication.services.dto.CaptureScreenDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CaptureScreenService {

    void saveCapture(String dataImg);

    List<CaptureScreenDto> findAllByAccount(Account account, Pageable pageable);

    CaptureScreenDto findCaptureById(Long idCapture);

    List<CaptureScreenDto> findAllByAccountAndCreateDate(Account account, String trim, Pageable pageable);
}
