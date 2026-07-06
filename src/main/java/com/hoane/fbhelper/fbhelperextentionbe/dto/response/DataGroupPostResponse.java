package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataGroupPostResponse {
    private String title_match;
    private List<String> images;
    private List<String> contents;

    private Integer from_member;
    private Integer to_member;
    private Integer priority;
    private String name;

    private Integer user_id;

    public DataGroupPostResponse(DataGroupPost dataGroupPost) {
        this.title_match = dataGroupPost.getTitle_match();
        this.images = dataGroupPost.getImages();
        this.contents = dataGroupPost.getContents();
        this.from_member = dataGroupPost.getFrom_member();
        this.to_member = dataGroupPost.getTo_member();
        this.priority = dataGroupPost.getPriority();
        this.name = dataGroupPost.getName();
        this.user_id = dataGroupPost.getUser().getId();
    }
}
