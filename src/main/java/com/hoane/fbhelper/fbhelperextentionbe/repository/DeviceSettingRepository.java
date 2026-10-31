package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DeviceSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceSettingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceSettingRepository extends JpaRepository<DeviceSetting, Integer> {

    DeviceSetting findByDevice_IdAndUser_id(String deviceId, String userId);

    List<DeviceSetting> findAllByUser_Id(String userId);

    @Query(value = "SELECT ds FROM DeviceSetting ds WHERE ds.device.id = :deviceId " +
            "AND (:status IS NULL OR ds.status = :status) " +
            "AND (:keyword IS NULL OR (LOWER(ds.user.name) LIKE CONCAT('%', :keyword, '%')) OR LOWER(ds.user.username) LIKE CONCAT('%', :keyword, '%'))")
    Page<DeviceSetting> findAllByDevice_Id(String deviceId, Pageable pageable, String keyword, DeviceSettingStatus status);


    @Query(value = "SELECT * FROM device_settings WHERE device_id IN :ids", nativeQuery = true)
    List<DeviceSetting> findAllByListDeviceId(List<String> ids);

    Integer countByStatusAndDevice_Id(DeviceSettingStatus status, String deviceId);


    @Modifying
    @Query(value = "UPDATE DeviceSetting SET isChanged = :changed WHERE user.id = :userId")
    void updateAllChangedByUserId(String userId, Boolean changed);

    @Modifying
    @Query(value = "UPDATE device_settings SET is_changed = :changed WHERE device_id IN :deviceIds AND user_id = :userId", nativeQuery = true)
    void updateAllChangedByListDeviceIdAndUserId(List<String> deviceIds, String userId, Boolean changed);
}
