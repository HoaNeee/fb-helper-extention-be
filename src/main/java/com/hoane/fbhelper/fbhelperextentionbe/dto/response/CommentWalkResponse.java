package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalk;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalkDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

public class CommentWalkResponse {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DataResponse {
        private String id;

        private List<String> titleQuerySearchs;

        private List<String> files;

        private List<String> contents;

        private List<String> keywordQueryIncludes;

        private List<String> keywordQueryExcludes;

        private List<String> keywordsCertainChoice;

        private Integer matchRateValueContentQueryIncludes;

        private String name;


        private Boolean isActive;

        public DataResponse(CommentWalk commentWalk) {
            this.id = commentWalk.getId();
            this.titleQuerySearchs = commentWalk.getTitleQuerySearchs();
            this.name = commentWalk.getName();
            this.files = commentWalk.getFiles();
            this.contents = commentWalk.getContents();
            this.keywordQueryExcludes = commentWalk.getKeywordQueryExcludes();
            this.keywordQueryIncludes = commentWalk.getKeywordQueryIncludes();
            this.keywordsCertainChoice = commentWalk.getKeywordsCertainChoice();
            this.matchRateValueContentQueryIncludes = commentWalk.getMatchRateValueContentQueryIncludes();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CommentWalkDetailResponse {
        private Integer id;

        private Boolean isActive;

        private String deviceId;

        private String commentWalkId;

        public CommentWalkDetailResponse(CommentWalkDetail commentWalkDetail) {
            this.id = commentWalkDetail.getId();
            this.isActive = commentWalkDetail.getIsActive();
            this.deviceId = commentWalkDetail.getDevice().getId();
            this.commentWalkId = commentWalkDetail.getCommentWalk().getId();
        }
    }
}
