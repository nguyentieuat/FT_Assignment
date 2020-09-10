package com.example.managersharefile.services;

import com.example.managersharefile.exception.BusinessException;
import com.example.managersharefile.services.dto.RuleAccessFileDto;

import java.util.List;

public interface RuleAccessFileService {

    /**
     * Add rule access file for users.
     * @param ruleAccessFileDtos
     * @return
     * @throws BusinessException
     */
    List<RuleAccessFileDto> addRule(List<RuleAccessFileDto> ruleAccessFileDtos) throws BusinessException;

    /**
     *  Add rule access file for user.
     * @param ruleAccessFileDto
     * @return
     * @throws BusinessException
     */
    RuleAccessFileDto addRule(RuleAccessFileDto ruleAccessFileDto) throws BusinessException;

    /**
     * Remove rule access file for user.
     * @param ruleAccessFileDto
     * @return
     * @throws BusinessException
     */
    Boolean removeRule(RuleAccessFileDto ruleAccessFileDto) throws BusinessException;

    /**
     * Remove rule access file for users.
     * @param ruleAccessFileDtos
     * @return
     * @throws BusinessException
     */
    Boolean removeRule(List<RuleAccessFileDto> ruleAccessFileDtos) throws BusinessException;
}
