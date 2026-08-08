package com.hoane.fbhelper.fbhelperextentionbe.reporitory;

import com.hoane.fbhelper.fbhelperextentionbe.entity.SpecialFrameHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialFrameHourRepository extends JpaRepository<SpecialFrameHour, Integer> {
    List<SpecialFrameHour> findAllByUser_Id(String user_id);

    void deleteAllByUser_Id(String user_id);
}
