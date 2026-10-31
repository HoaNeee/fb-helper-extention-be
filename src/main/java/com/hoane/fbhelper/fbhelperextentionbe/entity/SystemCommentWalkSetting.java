package com.hoane.fbhelper.fbhelperextentionbe.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.utils.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;


@Entity
@Table(name = "system_comment_walk_setting")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemCommentWalkSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> urlCommentWalkHomes = Constant.URL_COMMENT_WALK_HOMES;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> urlCommentWalkSearches = Constant.URL_COMMENT_WALK_SEARCHES;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> patternUrlPostCommentWalks = Constant.PATTERN_URL_POST_COMMENT_WALKS;

    private String apiKeyFreeGoogleAI;

    private String apiKeyPaidGoogleAI;
}
