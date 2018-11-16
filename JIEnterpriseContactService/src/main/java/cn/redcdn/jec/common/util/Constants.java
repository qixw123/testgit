package cn.redcdn.jec.common.util;

public class Constants {
    public static final String ACCOUNT_TYPE_SYSADMIN = "sysadmin_";
    public static final String ORG_TYPE_SYSADMIN = "orgadmin_";

    /**
     * 搜索类型 所有
     */
    public static final int SEARCH_TYPE_ALL = 0;
    public static final int SEARCH_TYPE_NAME = 1;
    public static final int SEARCH_TYPE_NUBE = 2;
    public static final int SEARCH_TYPE_GROUP = 3;

    /**
     * 账号种类
     */
    public static final int ACCOUNT_TYPE_TP = 1;
    public static final int ACCOUNT_TYPE_MOBILE = 2;
    public static final int ACCOUNT_TYPE_MAIL = 3;
    public static final int ACCOUNT_TYPE_OTHER = 4;

    /**
     * 账号密码最多输入次数
     */
    public static final int MAX_ERROR_COUNT = 5;
    /**
     * token模式
     */
    public static final int APP_TOKEN = 1;// app用户
    public static final int ADMIN_TOKEN = 2;// 页面管理员

    /**
     * 逗号
     */
    public static final String STR_CONCAT_COMMA = ",";
    /**
     * 波浪号
     */
    public static final String STR_CONCAT_WAVE = "~";
    /**
     * 竖线号
     */
    public static final String STR_CONCAT_LINE = "|";
    /**
     * 横线
     */
    public static final String STR_CONCAT_DASH = "-";
    /**
     * 下划线
     */
    public static final String STR_CONCAT_UNDERLINE = "_";

    /**
     * 禁用状态
     */
    public static final int DISABLED = 1;// 禁用/删除
    public static final int ENABLED = 2;// 可用/未删除

    /**
     * 存在与否
     */
    public static final String NOT_EXIST = "1";
    public static final String EXIST = "2";

    /**
     * 组的级别
     */
    public static final int FIRST_LEVEL = 1;
    public static final int SECOND_LEVEL = 2;
    public static final int THIRD_LEVEL = 3;

    /**
     * 分组
     */
    public static final int GROUP_FIRST = 1;
    public static final int GROUP_SECOND = 2;
    public static final int GROUP_THIRD = 3;

    /**
     * 管理员类型
     */
    public static final String ACCOUNT_ADMIN = "1";
    public static final String ORG_ADMIN = "2";

    /**
     * 机构管理员账号查询类型
     */
    public static final String SEARCH_BY_ACCOUNT = "1";
    public static final String SEARCH_BY_ORG = "2";

    /**
     * 创建或重置密码
     */
    public static final int CREATE_ACCOUNT = 1;
    public static final int RESET_ACCOUNT = 2;

}
