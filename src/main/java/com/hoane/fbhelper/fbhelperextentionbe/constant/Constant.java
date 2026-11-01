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

    public static final int TIME_BREAK_WHEN_SPAMMED = 2;

    public static final String JWT_SECRET = "THIS_IS_MY_JWT_SECRET_VERY_LONG_LONG";
    public static final long JWT_EXPIRATION = 1000 * 60 * 60 * 24; //1d

    public static final List<String> STRICTLY_TITLE_MATCH_GROUPS = Utils.splitStringToList(DefaultValue.strictlyMatchStr);
    public static final List<String> CONTENT_QUERY_INCLUDES_COMMON_COMMENT_WALK = Utils.splitStringToList(DefaultValue.contentQueryIncludesCommonCommentWalkStr);
    public static final List<String> CONTENT_QUERY_EXCLUDES_COMMON_COMMENT_WALK = Utils.splitStringToList(DefaultValue.contentQueryExcludesCommonCommentWalkStr);
    public static final List<String> KEYWORDS_CERTAIN_CHOICE_COMMENT_WALK = Utils.splitStringToList(DefaultValue.keywordsCertainChoiceCommentWalkStr);

    public static final String PATH_UPLOAD_DIR = System.getenv("PATH_UPLOAD_DIR") != null ? System.getenv("PATH_UPLOAD_DIR") : "C:\\media\\fb-extention\\uploads";

    public static final int PRIORITY_TASK_POST = 1;
    public static final int PRIORITY_TASK_COMMENT_WALK = 2;

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
    public static final String COMMENT_WALK_SPEED = "NORMAL";

    //setting
    public static final String APP_NAME = "FB Tools Helper";
    public static final String MAIL_SUPPORT = "nhhoa03@gmail.com";
    public static final Integer DAY_FREE_PREMIUM = 5;
    public static final Integer BREAK_PER_BATCH_PERCENT = 20;

    public static final Integer PERCENT_REPLACE_VALUE = 5;

    public static final String REPLACE_VALUE_PHONE = "0339005642";
    public static final String REPLACE_VALUE_IMAGE = "tel:0339005642";

    public static final Integer COMMENT_AFTER_POST_PERCENT = 25;
    public static final Integer INTERACT_BEFORE_POST_PRECENT = 20;

    public static final List<String> URL_GET_LIST_GROUPS = List.of("https://www.facebook.com/groups/feed/?ref=group_browse");
    public static final List<String> PATTERN_URL_POST_IN_GROUP = List.of("^https:\\/\\/www\\.facebook\\.com\\/groups\\/[a-zA-Z0-9.]+\\/?$");
    public static final List<String> URL_COMMENT_WALK_SEARCHES = List.of("facebook.com/search/top");
    public static final List<String> URL_COMMENT_WALK_HOMES = List.of("facebook.com");
    public static final List<String> PATTERN_URL_POST_COMMENT_WALKS = List.of("^https:\\/\\/www\\.facebook\\.com\\/groups\\/[a-zA-Z0-9._-]+\\/permalink\\/[A-Za-z0-9_.-\\\\/]+(\\/?)$");

}
