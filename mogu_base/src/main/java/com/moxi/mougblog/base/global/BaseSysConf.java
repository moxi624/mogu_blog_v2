package com.moxi.mougblog.base.global;

/**
 * 常量的基类
 *
 * @author 陌溪
 * @date 2018年9月25日00:06:54
 */
public class BaseSysConf {

    public final static String DEFAULT_UID = "uid00000000000000000000000000000";
    public final static String LIMIT_ONE = "LIMIT 1";
    public final static String OID = "oid";

    /**
     * picture相关
     */
    public final static String USER_UID = "userUid";
    public final static String BLOG = "blog";
    public final static String USER_NAME = "userName";
    public final static String ADMIN_UID = "adminUid";
    public final static String ROLE = "role";
    public final static String PROJECT_NAME = "projectName";
    public final static String SORT_NAME = "sortName";
    public final static String PIC_NAME = "picName";
    public final static String FILE_NAME = "fileName";
    public final static String UPLOADED = "uploaded";
    public final static String QI_NIU_URL = "qiNiuUrl";
    public final static String PIC_URL = "picUrl";
    public final static String MINIO_URL = "minioUrl";
    public final static String URL = "url";
    public final static String MESSAGE = "message";
    public final static String EXPANDED_NAME = "expandedName";
    public final static String FILE_OLD_NAME = "fileOldName";
    public final static String PICTURE_LIST = "pictureList";
    public final static String NAME = "name";
    public final static String SOURCE = "source";


    /**
     * IP相关
     */
    public final static String OS = "OS";
    public final static String BROWSER = "BROWSER";
    public final static String IP = "ip";
    public final static String UTF_8 = "utf-8";


    public final static String SUCCESS = "success";
    public final static String ERROR = "error";
    public final static String STATUS = "status";
    public final static String CREATE_TIME = "createTime";
    public final static String UPDATE_TIME = "updateTime";
    public final static String TOKEN = "token";
    public final static String PLATFORM = "platform";
    public final static String ACCESS_TOKEN = "accessToken";

    public final static String CAN_NOT_COMMENT = "0";
    public final static String CODE = "code";
    public final static String DATA = "data";
    public final static String UID = "uid";
    public final static String PAGE_NAME = "pageName";
    public final static String LEFT = "left";
    public final static String RIGHT = "right";

    public final static String DEFAULT_VALUE = "defaultValue";

    /**
     * platform平台相关
     */
    public final static String WEB = "web";
    public final static String ADMIN = "admin";

    /**
     * 分页相关
     */
    public final static String TOTAL = "total";
    public final static String TOTAL_PAGE = "totalPage";
    public final static String CURRENT_PAGE = "currentPage";
    public final static String BLOG_LIST = "blogList";
    public final static String PAGE_SIZE = "pageSize";

    /**
     * blog
     */
    public final static String BLOG_UID = "blogUid";
    public final static String LEVEL = "level";

    public final static String START_EMAIL_NOTIFICATION = "startEmailNotification";


    /**
     * RabbitMQ的命令操作
     */
    public final static String COMMAND = "command";
    public final static String EDIT = "edit";
    public final static String ADD = "add";
    public final static String DELETE = "delete";
    public final static String DELETE_BATCH = "deleteBatch";
    public final static String EDIT_BATCH = "editBatch";
    public final static String DELETE_ALL = "deleteAll";
    public final static String EXCHANGE_DIRECT = "exchange.direct";
    public final static String MOGU_BLOG = "mogu.blog";

    /**
     * redis相关
     */
    public final static String BLOG_SORT_BY_MONTH = "BLOG_SORT_BY_MONTH";
    /**
     * redis分割符
      */
    public final static String REDIS_SEGMENTATION = ":";
    public final static String EQUAL_TO = "=";
    /**
     * 月份集合
     */
    public final static String MONTH_SET = "MONTH_SET";
    /**
     * 博客等级
     */
    public final static String BLOG_LEVEL = "BLOG_LEVEL";


    /**
     * 字典类型
     */
    public final static String REDIS_DICT_TYPE = "REDIS_DICT_TYPE";

    /**
     * 文件分割符
     */
    public final static String FILE_SEGMENTATION = ",";

    /**
     * 系统全局是否标识
     */
    public static final int YES = 1;
    public static final int NO = 0;

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;

    public static final int TWO_TWO_FIVE = 225;
    public static final int ONE_ZERO_TWO_FOUR = 1024;

    /**
     * SystemConfig相关
     */
    public final static String UPLOAD_QI_NIU = "uploadQiNiu";
    public final static String UPLOAD_LOCAL = "uploadLocal";
    public final static String UPLOAD_MINIO = "uploadMinio";
    public final static String LOCAL_PICTURE_BASE_URL = "localPictureBaseUrl";
    public final static String QI_NIU_PICTURE_BASE_URL = "qiNiuPictureBaseUrl";
    public final static String MINIO_PICTURE_BASE_URL = "minioPictureBaseUrl";
    public final static String QI_NIU_ACCESS_KEY = "qiNiuAccessKey";
    public final static String QI_NIU_SECRET_KEY = "qiNiuSecretKey";
    public final static String QI_NIU_BUCKET = "qiNiuBucket";
    public final static String QI_NIU_AREA = "qiNiuArea";

    public final static String MINIO_END_POINT = "minioEndPoint";
    public final static String MINIO_ACCESS_KEY = "minioAccessKey";
    public final static String MINIO_SECRET_KEY = "minioSecretKey";
    public final static String MINIO_BUCKET = "minioBucket";

    public final static String PICTURE_PRIORITY = "picturePriority";
    public final static String CONTENT_PICTURE_PRIORITY = "contentPicturePriority";
    public final static String PICTURE = "picture";
    public final static String PICTURE_TOKEN = "pictureToken";
    public final static String LIST = "list";


    /**
     * AOP相关
     */
    public static final String AUTHOR = "author";
    public static final String BLOG_SORT_UID = "blogSortUid";
    public static final String TAG_UID = "tagUid";
    public static final String KEYWORDS = "keywords";
    public static final String MONTH_DATE = "monthDate";
    public static final String MODULE_UID = "moduleUid";
    public static final String OTHER_DATA = "otherData";
    public static final String COMMENT_VO = "commentVO";
    public static final String CONTENT = "content";
    public static final String TARGET = "target";

    /**
     * 参数配置相关
     */
    public static final String SYS_DEFAULT_PASSWORD = "SYS_DEFAULT_PASSWORD";
    public static final String BLOG_NEW_COUNT = "BLOG_NEW_COUNT";
    public static final String BLOG_FIRST_COUNT = "BLOG_FIRST_COUNT";
    public static final String BLOG_SECOND_COUNT = "BLOG_SECOND_COUNT";
    public static final String BLOG_THIRD_COUNT = "BLOG_THIRD_COUNT";
    public static final String BLOG_FOURTH_COUNT = "BLOG_FOURTH_COUNT";
    public static final String BLOG_HOT_COUNT = "BLOG_HOT_COUNT";
    public static final String HOT_TAG_COUNT = "HOT_TAG_COUNT";
    public static final String FRIENDLY_LINK_COUNT = "FRIENDLY_LINK_COUNT";
    public final static String PROJECT_NAME_ = "PROJECT_NAME";
    public final static String MAX_STORAGE_SIZE = "MAX_STORAGE_SIZE";
}
