package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.entity.SystemCommentWalkSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SystemPostSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SystemSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SystemToolSetting;
import com.hoane.fbhelper.fbhelperextentionbe.repository.SystemCommentWalkSettingRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.SystemPostSettingRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.SystemSettingRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.SystemToolSettingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    @Autowired
    private SystemToolSettingRepository systemToolSettingRepository;

    @Autowired
    private SystemPostSettingRepository systemPostSettingRepository;

    @Autowired
    private SystemCommentWalkSettingRepository systemCommentWalkSettingRepository;

    @Autowired
    private ModelMapper modelMapper;


    private SystemSetting findSystemSettingOrThrow() {
        return systemSettingRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("Server error: can not find system setting"));
    }


    private SystemToolSetting findSystemToolSettingOrThrow() {
        return systemToolSettingRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("Server error: can not find system tool setting"));
    }

    private SystemPostSetting findSystemPostSettingOrThrow() {
        return systemPostSettingRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("Server error: can not find system post setting"));
    }

    private SystemCommentWalkSetting findSystemCommentWalkSettingOrThrow() {
        return systemCommentWalkSettingRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("Server error: can not find system comment walk setting"));
    }


    public SystemSetting getSystemSetting() {
        return findSystemSettingOrThrow();
    }

    public SystemToolSetting getSystemToolSetting() {
        return findSystemToolSettingOrThrow();
    }

    public SystemPostSetting getSystemPostSetting() {
        return findSystemPostSettingOrThrow();
    }

    public SystemCommentWalkSetting getSystemCommentWalkSetting() {
        return findSystemCommentWalkSettingOrThrow();
    }


    public SystemSetting updateSystemSetting(SystemSetting systemSetting) {
        SystemSetting existSystemSetting = findSystemSettingOrThrow();

        modelMapper.map(systemSetting, existSystemSetting);

        return systemSettingRepository.save(existSystemSetting);
    }

    public SystemToolSetting updateSystemToolSetting(SystemToolSetting systemToolSetting) {
        SystemToolSetting existSystemToolSetting = findSystemToolSettingOrThrow();

        modelMapper.map(systemToolSetting, existSystemToolSetting);

        return systemToolSettingRepository.save(existSystemToolSetting);
    }

    public SystemPostSetting updateSystemPostSetting(SystemPostSetting systemPostSetting) {
        SystemPostSetting existSystemPostSetting = findSystemPostSettingOrThrow();

        modelMapper.map(systemPostSetting, existSystemPostSetting);

        return systemPostSettingRepository.save(existSystemPostSetting);
    }

    public SystemCommentWalkSetting updateSystemCommentWalkSetting(SystemCommentWalkSetting systemCommentWalkSetting) {
        SystemCommentWalkSetting existSystemCommentWalkSetting = findSystemCommentWalkSettingOrThrow();

        modelMapper.map(systemCommentWalkSetting, existSystemCommentWalkSetting);

        return systemCommentWalkSettingRepository.save(existSystemCommentWalkSetting);
    }
}
