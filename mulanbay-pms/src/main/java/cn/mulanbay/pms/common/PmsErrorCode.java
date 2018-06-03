package cn.mulanbay.pms.common;

/**
 * 错误代码
 * 规则：1位系统代码（pms为1）+2两位模块代码+2两位子模块代码+两位编码
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class PmsErrorCode {

    /** 通用类 start **/

    public static final int USER_ENTITY_NOT_FOUND= 1010001;

    public static final int USER_ENTITY_NOT_ALLOWED= 1010002;

    public static final int UN_SUPPORT_DATE_GROUP_TYPE= 1010003;


    /** 通用类 end **/

    /** 用户行为类 start **/

    public static final int USER_BEHAVIOR_CONFIG_WITH_LEVEL_NOT_FOUND= 1010201;
    public static final int USER_BEHAVIOR_CONFIG_CHECK_ERROR= 1010202;
    public static final int USER_BEHAVIOR_CONFIG_NULL= 1010203;

    /** 用户行为类 end **/

    /** 报表类 start **/
    public static final int REPORT_CONFIG_SQL_ERROR= 1020101;

    /** 报表类 end **/

    /** 消费类 start **/
    public static final int UNSUPPORT_BUY_RECORD_GROUP_TYPE= 1030101;

    public static final int BUY_RECORD_GOODS_TYPE_NULL= 1030102;

    /** 消费类 end **/

    /** 运动类 start **/
    public static final int SPROT_MILESTONE_KM_MN_NULL= 1040101;

    public static final int SPROT_MILESTONE_KM_NULL= 1040102;

    public static final int SPROT_MILESTONE_ORDER_INDEX_ERROR= 1040103;

    /** 运动类 end **/

    /** 计划类 start **/
    public static final int PLAN_CONFIG_SQL_RETURN_COLUMN_ERROR= 1050101;

    public static final int PLAN_CONFIG_PLAN_TYPE_NOT_SUPPORT= 1050102;

    public static final int PLAN_CONFIG_SQL_ERROR= 1050103;

    public static final int PLAN_REPORT_RE_STAT_YEAR_NULL= 1050104;

    public static final int USER_PLAN_REMIND_NULL= 1050105;


    /** 计划类 end **/

    /** 阅读类 start **/
    public static final int READING_RECORD_STATUS_ERROR= 1060101;


    /** 阅读类 end **/

    /** 日志类 start **/
    public static final int OPERATION_LOG_BEAN_ID_NULL= 1070101;

    public static final int OPERATION_LOG_COMPARE_ID_VALUE_NULL= 1070102;

    /** 日志类 end **/

    /** 系统类 start **/
    public static final int START_YEAR_NOT_EQUALS_END_YEAR= 1080101;

    public static final int SYSTEM_FUNCTION_NOT_DEFINE= 1080102;

    public static final int SYSTEM_FUNCTION_DISABLED= 1080103;

    public static final int OPERATION_LOG_PARA_IS_NULL= 1080104;

    public static final int OPERATION_LOG_CANNOT_GET= 1080105;

    public static final int START_OR_END_DATE_NULL= 1080106;


    /** 系统类 end **/

    /** 健康类 start **/
    public static final int TREAT_DRUG_DETAIL_OCCURTIME_INCORRECT= 1090101;


    /** 健康类 end **/

    /** 梦想类 start **/
    public static final int DREAM_PLAN_VALUE_NULL = 1100101;

    public static final int DREAM_FININSH_DATE_NULL = 1100102;

    /** 梦想类 end **/

    /** 饮食类 start **/
    public static final int DIET_TYPE_NULL= 1110101;


    /** 饮食类 end **/


    /** 提醒类 start **/
    public static final int NOTIFY_WARNING_VALUE_LESS_ALERT= 1120101;

    public static final int NOTIFY_WARNING_VALUE_MORE_ALERT= 1120102;

    /** 提醒类 end **/

}
