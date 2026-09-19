package com.hoane.fbhelper.fbhelperextentionbe.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriorityTask {
    @NotNull(message = "Priority task post can not null")
    @Min(value = 0, message = "Priority task post must be greater than or equal to 0")
    private Integer priorityTaskPost;

    @NotNull(message = "Priority task comment walk can not null")
    @Min(value = 0, message = "Priority task comment walk must be greater than or equal to 0")
    private Integer priorityTaskCommentWalk;
}
