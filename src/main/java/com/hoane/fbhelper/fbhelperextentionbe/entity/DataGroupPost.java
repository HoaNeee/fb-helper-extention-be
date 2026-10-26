package com.hoane.fbhelper.fbhelperextentionbe.entity;

import com.hoane.fbhelper.fbhelperextentionbe.utils.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "data_group_posts")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DataGroupPost {
    @Id
    @Column(columnDefinition = "VARCHAR(10)", unique = true, updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "NVARCHAR(255)") // Use NVARCHAR for Unicode support
    private String name;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT") //TEXT will be used to store large text data
    private List<String> contents;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> files;

    private Integer priority;

    private Integer fromMember;

    private Integer toMember;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
