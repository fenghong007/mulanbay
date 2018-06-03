package cn.mulanbay.schedule;

import cn.mulanbay.common.exception.ErrorCode;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2017-10-19 9:50
 **/
public class ScheduleErrorCode extends ErrorCode {

    // 创建触发器异常
    public final static int TRIGGER_CREATE_ERROR=30001;

    // 触发器参数为空
    public final static int TRIGGER_PARA_NULL =30002;

    //调度器参数长度不正确
    public final static int TRIGGER_PARA_LENGTH_ERROR=30003;

    //调度器参数个数不正确
    public final static int TRIGGER_PARA_FORMAT_ERROR=30004;

    //获取有效调度器失败
    public final static int TRIGGER_GET_ACTIVE_LIST_ERROR=30005;

    //检查调度日志异常
    public final static int TRIGGER_LOG_CHECK_ERROR=30006;

    //获取自动重做的调度日志
    public final static int TRIGGER_GET_AUTO_REDO_LOG_ERROR=30007;

    public final static int TRIGGER_CANNOT_REDO=30008;

    public final static int SCHEDULE_NOT_ENABLED=30009;

    public final static int SCHEDULE_ALREADY_EXECED=30010;


}
