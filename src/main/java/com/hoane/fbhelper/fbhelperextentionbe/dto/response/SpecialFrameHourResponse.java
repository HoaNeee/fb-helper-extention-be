package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SpecialFrameHour;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SpecialFrameHourSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SpecialFrameHourResponse {
    private Integer id;

    private Integer startTime;

    private Integer endTime;

    private Integer maxGroup;

    private List<Integer> applyDates;

    private Boolean isActive;

    public SpecialFrameHourResponse(SpecialFrameHour specialFrameHour, SpecialFrameHourSetting specialFrameHourSetting) {
        this.id = specialFrameHour.getId();
        this.startTime = specialFrameHour.getStartTime();
        this.endTime = specialFrameHour.getEndTime();
        this.maxGroup = specialFrameHour.getMaxGroup();
        this.applyDates = specialFrameHour.getApplyDates();
        this.isActive = specialFrameHourSetting.getIsActive();
    }

    public SpecialFrameHourResponse(SpecialFrameHour specialFrameHour) {
        this.id = specialFrameHour.getId();
        this.startTime = specialFrameHour.getStartTime();
        this.endTime = specialFrameHour.getEndTime();
        this.maxGroup = specialFrameHour.getMaxGroup();
        this.applyDates = specialFrameHour.getApplyDates();
    }
}
