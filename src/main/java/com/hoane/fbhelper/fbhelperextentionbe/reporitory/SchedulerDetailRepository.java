package com.hoane.fbhelper.fbhelperextentionbe.reporitory;

import com.hoane.fbhelper.fbhelperextentionbe.entity.SchedulerDetail;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.SchedulerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchedulerDetailRepository extends JpaRepository<SchedulerDetail, Integer> {

    SchedulerDetail findByScheduler_IdAndSchedulerType(int scheduler_id, SchedulerType schedulerType);

    List<SchedulerDetail> findAllByScheduler_Id(int schedulerId);
}
