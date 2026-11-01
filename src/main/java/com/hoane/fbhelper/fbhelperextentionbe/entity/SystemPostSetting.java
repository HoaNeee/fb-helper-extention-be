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
@Table(name = "system_post_setting")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemPostSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean isReplaceImage = true;

    private Boolean isReplacePhone = true;

    private Integer replacePhonePercent = Constant.PERCENT_REPLACE_VALUE;

    private Integer replaceImagePercent = Constant.PERCENT_REPLACE_VALUE;

    private String replacePhoneValue = Constant.REPLACE_VALUE_PHONE;

    private String replaceImageValue = Constant.REPLACE_VALUE_IMAGE;

    private Integer commentAfterPostPercent = Constant.COMMENT_AFTER_POST_PERCENT;
    
    private Integer interactBeforePostPercent = Constant.INTERACT_BEFORE_POST_PRECENT;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> urlGetListGroups = Constant.URL_GET_LIST_GROUPS;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> patternUrlPostInGroups = Constant.PATTERN_URL_POST_IN_GROUP;
}
