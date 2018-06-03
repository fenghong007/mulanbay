package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.persistent.enums.LogLevel;
import cn.mulanbay.pms.persistent.enums.MonitorBussType;
import cn.mulanbay.schedule.job.CommandJob;

/**
 * 命令job
 */
public class PmsCommandJob extends CommandJob {

    @Override
    public void notifyLog(String cmd) {
        //通知
        PmsNotifyHandler pmsNotifyHandler=BeanFactoryUtil.getBean(PmsNotifyHandler.class);
        String title = "服务器执行了脚本命令";
        pmsNotifyHandler.addMessageToNotifier(title,
                "服务器执行了脚本命令："+cmd,null, LogLevel.WARNING,null,MonitorBussType.SYSTEM);
    }
}
