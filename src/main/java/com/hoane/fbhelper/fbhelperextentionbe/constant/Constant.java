package com.hoane.fbhelper.fbhelperextentionbe.constant;


import com.hoane.fbhelper.fbhelperextentionbe.utils.Utils;

import java.util.List;

public class Constant {

    public static final String USER_ID = "abc1234567";
    public static final String DEVICE_ID = "abcxyz12";
    public static final String DATA_GROUP_POST_ID = "abcxyz1234";
    public static final String COMMENT_WALK_ID = "comment_walk_1";
    public static final int TIME_DELAY_TO_POST = 8;
    public static final int TIME_DEFAULT_SCHEDULER_MINUTES = 5;
    public static final int TIME_DEFAULT_SCHEDULER_HOURS = 1;
    public static final int MAX_LENGTH_FILE_NAME = 15;


    public static final String JWT_SECRET = "THIS_IS_MY_JWT_SECRET_VERY_LONG_LONG";
    public static final long JWT_EXPIRATION = 1000 * 60 * 60 * 24; //1d

    public static final List<String> STRICTLY_TITLE_MATCH_GROUPS = Utils.splitStringToList(DefaultValue.strictlyMatchStr);
    public static final List<String> CONTENT_QUERY_INCLUDES_COMMON_COMMENT_WALK = Utils.splitStringToList(DefaultValue.contentQueryIncludesCommonCommentWalkStr);
    public static final List<String> CONTENT_QUERY_EXCLUDES_COMMON_COMMENT_WALK = Utils.splitStringToList(DefaultValue.contentQueryExcludesCommonCommentWalkStr);
    public static final List<String> KEYWORDS_CERTAIN_CHOICE_COMMENT_WALK = Utils.splitStringToList(DefaultValue.keywordsCertainChoiceCommentWalkStr);

    public static final String PATH_UPLOAD_DIR = System.getenv("PATH_UPLOAD_DIR");

    //Data group post
    public static final int MAX_GROUP_PER_BATCH = 1;
    public static final int MAX_COMMENT_PER_POST = 1;
    public static final int MAX_POST_INTERACT_PER_BATCH = 5;

    //COMMENT WALK
    public static final int MAX_COMMENT_WALK_PER_BATCH = 1;
    public static final int TIME_DELAY_FILL_CONTENT_COMMENT_WALK_MIN = 100;
    public static final int TIME_DELAY_FILL_CONTENT_COMMENT_WALK_MAX = 200;
    public static final int TIME_DELAY_FILL_FILE_COMMENT_WALK = 5;
    public static final int TIME_DELAY_SUBMIT_COMMENT_WALK = 11;
    public static final int MAX_RATE_VALUE_CONTENT_QUERY_INCLUDES_COMMON_COMMENT_WALK = 2;
    public static final int MAX_RATE_VALUE_CONTENT_QUERY_INCLUDES_COMMENT_WALK_RECOMMEND = 1;
    public static final String COMMENT_WALK_AREA = "RANDOM";
}
