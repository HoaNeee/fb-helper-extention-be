package com.hoane.fbhelper.fbhelperextentionbe.constant;


import java.util.List;

public class Constant {
    private static final String strictlyMatchStr = "Cho thuê trọ, Tìm phòng trọ, Cho thuê phòng trọ, CCMN, Phòng trọ, Tìm phòng trọ giá rẻ";

    public static final String USER_ID = "abc1234567";
    public static final String DEVICE_ID = "abcxyz12";
    public static final String DATA_GROUP_POST_ID = "abcxyz1234";
    public static final int TIME_DELAY_TO_POST = 8;
    public static final int TIME_DEFAULT_SCHEDULER_MINUTES = 5;
    public static final int TIME_DEFAULT_SCHEDULER_HOURS = 1;
    public static final int MAX_LENGTH_FILE_NAME = 15;
    public static final int MAX_GROUP_PER_BATCH = 1;
    public static final int MAX_COMMENT_PER_POST = 1;
    public static final int MAX_POST_INTERACT_PER_BATCH = 5;

    public static final String JWT_SECRET = "THIS_IS_MY_JWT_SECRET_VERY_LONG_LONG";
    public static final long JWT_EXPIRATION = 1000 * 60 * 60 * 24; //1d

    public static final List<String> STRICTLY_TITLE_MATCH_GROUPS = List.of(strictlyMatchStr.split(", "));

    public static final String PATH_UPLOAD_DIR = System.getenv("PATH_UPLOAD_DIR");
}
