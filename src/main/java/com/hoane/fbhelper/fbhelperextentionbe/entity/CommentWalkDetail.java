package com.hoane.fbhelper.fbhelperextentionbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Entity
@Table(name = "comment_walk_details")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentWalkDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "comment_walk_id", nullable = false, updatable = false)
    private CommentWalk commentWalk;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    private Device device;
}
