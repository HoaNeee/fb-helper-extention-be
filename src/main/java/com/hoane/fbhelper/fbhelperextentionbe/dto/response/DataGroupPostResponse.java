package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

public class DataGroupPostResponse {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DataGroupResponse {

        private String id;

        private String title;
        private List<String> files;
        private List<String> contents;

        private Integer fromMember;
        private Integer toMember;
        private Integer priority;
        private String name;


        private Boolean is_active;

        public DataGroupResponse(DataGroupPost dataGroupPost) {
            this.id = dataGroupPost.getId();
            this.title = dataGroupPost.getTitle();
            this.files = dataGroupPost.getFiles();
            this.contents = dataGroupPost.getContents();
            this.fromMember = dataGroupPost.getFromMember();
            this.toMember = dataGroupPost.getToMember();
            this.priority = dataGroupPost.getPriority();
            this.name = dataGroupPost.getName();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DataGroupPostDetailResponse {
        private int id;

        private Boolean isActive;

        private String deviceId;

        private String dataGroupPostId;

        public DataGroupPostDetailResponse(DataGroupPostDetail dataGroupPostDetail) {
            this.id = dataGroupPostDetail.getId();
            this.isActive = dataGroupPostDetail.getIsActive();
            this.deviceId = dataGroupPostDetail.getDevice().getId();
            this.dataGroupPostId = dataGroupPostDetail.getDataGroupPost().getId();
        }
    }
}
