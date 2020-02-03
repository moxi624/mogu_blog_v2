package com.moxi.mougblog.base.global;

/**
 * SQL字段常量
 *
 * @author 陌溪
 */
public class BaseSQLConf {

    public final static String ID = "id";
    public final static String UID = "uid";
    public final static String CREATE_TIME = "create_time";
    public final static String UPDATE_TIME = "update_time";
    public final static String STATUS = "status";
    public final static String IS_PUBLISH = "is_publish";
    public final static String LEVEL = "level";
    public final static String SORT = "sort";

    //user表
    public static final String USERNAME = "user_name";
    public static final String USEREMAIL = "email";
    public static final String SOURCE = "source";
    public static final String UUID = "uuid";
    public static final String URL = "url";
    public final static String USER_UID = "user_uid";
    public final static String COMMENT_UID = "comment_uid";
    public final static String REPORT_COMMENT_UID = "report_comment_uid";

    // SystemConfig相关
    public final static String UPLOAD_QI_NIU = "upload_qi_niu";
    public final static String UPLOAD_LOCAL = "upload_local";
    public final static String LOCAL_PICTURE_BASE_URL = "local_picture_base_url";
    public final static String QI_NIU_PICTURE_BASE_URL = "qi_niu_picture_base_url";
    public final static String QI_NIU_ACCESS_KEY = "qi_niu_access_key";
    public final static String QI_NIU_SECRET_KEY = "qi_niu_secret_key";
    public final static String QI_NIU_BUCKET = "qi_niu_bucket";
    public final static String QI_NIU_AREA = "qi_niu_area";
    public final static String PICTURE_PRIORITY = "picture_priority";

    // web_visit表
    public final static String IP = "ip";
}
