package com.hoane.fbhelper.fbhelperextentionbe.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.utils.converter.ListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Entity
@Table(name = "special_frame_hours")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SpecialFrameHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer startTime;
    private Integer endTime;

    private Integer maxGroup;

    @Convert(converter = ListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Integer> applyDates;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
