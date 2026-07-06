package com.hoane.fbhelper.fbhelperextentionbe.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DataGroupPostRequest {
    @NotBlank(message = "Title match can not null or empty", groups = OnCreate.class)
    @Size(min = 1, groups = OnUpdate.class, message = "Title match must be greater than or equal to 1 character")
    private String title_match;

    private String name;

    private Integer priority;

    private List<String> images;

    private List<String> contents;

    @Min(value = 0, message = "From member must be greater than or equal to 0")
    private Integer from_member;

    @Min(value = 0, message = "To member must be greater than or equal to 0")
    private Integer to_member;
}

