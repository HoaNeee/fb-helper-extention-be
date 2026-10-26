package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceSettingStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {

    @Query(value = "SELECT d FROM Device d WHERE d.id IN (SELECT ds.device.id FROM DeviceSetting ds WHERE ds.user.id = :userId) AND (:keyword IS NULL OR LOWER(d.deviceName) LIKE CONCAT('%', LOWER(:keyword), '%')) AND (:status IS NULL OR d.status = :status)")
    Page<Device> findAllByUser_Id(String userId, Pageable pageable, String keyword, DeviceStatus status);


    @Query(value = "SELECT d FROM Device d WHERE d.id IN (SELECT ds.device.id FROM DeviceSetting ds WHERE ds.user.id = :userId)")
    List<Device> findAllByUser_Id(String userId);

    @Query(value = "SELECT d FROM Device d WHERE (:keyword IS NULL OR LOWER(d.deviceName) LIKE CONCAT('%', LOWER(:keyword), '%')) AND (:status IS NULL OR d.status = :status)")
    Page<Device> findAll(Pageable pageable, String keyword, DeviceStatus status);

    Integer countByStatus(DeviceStatus status);
}
