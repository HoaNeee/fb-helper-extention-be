package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.SpecialFrameHourSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecialFrameHourSettingRepository extends JpaRepository<SpecialFrameHourSetting, Integer> {
    List<SpecialFrameHourSetting> findAllByDevice_Id(String device_id);

    @Modifying
    @Query(value = "SELECT s.* FROM special_frame_hour_settings s WHERE s.special_frame_hour_id IN :list", nativeQuery = true)
    List<SpecialFrameHourSetting> findAllByListSpecialFrameHourId(List<Integer> list);

    @Modifying
    @Query(value = "SELECT ss.* FROM special_frame_hour_settings ss WHERE ss.device_id = :deviceId AND ss.special_frame_hour_id IN (SELECT s.id FROM special_frame_hours s WHERE s.user_id = :userId)", nativeQuery = true)
    List<SpecialFrameHourSetting> findAllByUserIdAndDeviceId(String userId, String deviceId);

    SpecialFrameHourSetting findByDevice_IdAndSpecialFrameHour_Id(String device_id, Integer special_frame_hour_id);

    void deleteAllBySpecialFrameHour_Id(int special_frame_hour_id);

    List<SpecialFrameHourSetting> findAllBySpecialFrameHour_Id(int special_frame_hour_id);

    @Modifying
    @Query(value = "UPDATE special_frame_hour_settings SET is_active = :isActive WHERE special_frame_hour_id =  :specialFrameHourId", nativeQuery = true)
    void updateStatusAllDeviceForUser(int specialFrameHourId, Boolean isActive);

    @Modifying
    @Query(value = "DELETE FROM special_frame_hour_settings WHERE special_frame_hour_id IN (SELECT s.id FROM special_frame_hours s WHERE s.user_id = :userId)", nativeQuery = true)
    void deleteAllByUser_Id(String userId);
}
