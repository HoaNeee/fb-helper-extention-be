package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {

    @Query(value = "SELECT d.* FROM devices d WHERE d.id IN (SELECT device_id FROM device_settings WHERE user_id = :userId)", nativeQuery = true)
    List<Device> findAllByUser_Id(String userId);

}
