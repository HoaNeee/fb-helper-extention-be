package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Scheduler;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SchedulerDetail;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SchedulerTime;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.SchedulerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

public class SchedulerResponse {

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SchedulerDetailResponse {
        private SchedulerType schedulerType;
        private List<SchedulerTime> schedulerTimeList;
        private Integer schedulerTimeValue;

        public SchedulerDetailResponse(SchedulerDetail schedulerDetail) {
            this.schedulerType = schedulerDetail.getSchedulerType();
            this.schedulerTimeList = schedulerDetail.getSchedulerTimeList();
            this.schedulerTimeValue = schedulerDetail.getSchedulerTimeValue();
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SchedulerDeviceResponse {
        private String device_id;
        private SchedulerType schedulerType;

        public SchedulerDeviceResponse(Scheduler scheduler) {
            this.device_id = scheduler.getDevice().getId();
            this.schedulerType = scheduler.getSchedulerType();
        }
    }
}
