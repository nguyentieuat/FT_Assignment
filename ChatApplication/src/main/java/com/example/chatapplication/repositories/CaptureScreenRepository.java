package com.example.chatapplication.repositories;

import com.example.chatapplication.domain.CaptureScreen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptureScreenRepository extends JpaRepository<CaptureScreen, Long> {

}
