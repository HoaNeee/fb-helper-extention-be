package com.hoane.fbhelper.fbhelperextentionbe.reporitory;

import com.hoane.fbhelper.fbhelperextentionbe.entity.SpecialFrameHourSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecialFrameHourSettingRepository extends JpaRepository<SpecialFrameHourSetting, Integer> {
    List<SpecialFrameHourSetting> findAllByDevice_Id(String device_id);

    SpecialFrameHourSetting findByDevice_IdAndSpecialFrameHour_Id(String device_id, Integer special_frame_hour_id);

    void deleteAllBySpecialFrameHour_Id(int special_frame_hour_id);

    @Modifying
    @Query(value = "DELETE FROM special_frame_hour_settings WHERE special_frame_hour_id IN (SELECT s.id FROM special_frame_hours s WHERE s.user_id = :userId)", nativeQuery = true)
    void deleteAllByUser_Id(String userId);
}
