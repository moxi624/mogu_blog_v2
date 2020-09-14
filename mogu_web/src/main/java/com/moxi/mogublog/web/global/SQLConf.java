package com.moxi.mogublog.web.global;

import com.moxi.mougblog.base.global.BaseSQLConf;

/**
 * SQL字段常量
 *
 * @author xuzhixiang
 */
public final class SQLConf extends BaseSQLConf {


    /**
     * FileSort表
     */
    public final static String CONTENT = "content";
    public final static String TITLE = "title";
    public final static String NAME = "name";
    public final static String PIC_NAME = "pic_name";
    public final static String PICTURE_SORT_UID = "picture_sort_uid";

    /**
     * Blog表
     */
    public final static String CLICK_COUNT = "click_count";
    public static final String LEVEL = "level";
    public final static String SUMMARY = "summary";
    public static final String TAG_UID = "tag_uid";
    public static final String BLOG_UID = "blog_uid";
    public static final String BLOG_SORT_UID = "blog_sort_uid";
    public static final String SORT = "sort";
    public static final String AUTHOR = "author";

    /**
     * StudyVideo表
     */
    public static final String RESOURCE_SORT_UID = "resource_sort_uid";

    public static final String USER_NAME = "user_name";
    public static final String EMAIL = "email";

    /**
     * webVisit表
     */
    public static final String BEHAVIOR = "behavior";
    public static final String IP = "ip";
    public static final String MODULE_UID = "module_uid";

    /**
     * sortRestAPi
     */
    public static final String MONTH = "month";

    public static final String FIRST_COMMENT_UID = "first_comment_uid";

}
