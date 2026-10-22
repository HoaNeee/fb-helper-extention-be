package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.DeviceSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceSettingRepository extends JpaRepository<DeviceSetting, Integer> {

    DeviceSetting findByDevice_IdAndUser_id(String deviceId, String userId);

    List<DeviceSetting> findAllByUser_Id(String userId);
}
