package com.example.managersharefile.repositories;

import com.example.managersharefile.entities.Common;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonRepository extends JpaRepository<Common, Integer> {

    Common findByTypeIdAndTypeSubId(int typeId, int typeSubId);

    List findAllByTypeId(int typeId);
}
