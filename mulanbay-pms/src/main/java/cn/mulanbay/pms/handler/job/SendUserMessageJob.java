package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.persistent.domain.UserMessage;
import cn.mulanbay.pms.persistent.service.UserRemindMessageService;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.job.AbstractBaseJob;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 发送提醒消息
 * 待发送的消息已经被保存在消息表里面，该job只是定时去取消息发送
 * 根据expectSendTime字段判断是否要发送
 * todo 后期可以选择发送微信消息等
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class SendUserMessageJob extends AbstractBaseJob {

    private static Logger logger = Logger.getLogger(SendUserMessageJob.class);

    UserRemindMessageService userRemindMessageService;

    BaseService baseService;

    PmsNotifyHandler pmsNotifyHandler;

    @Override
    public TaskResult doTask() {
        TaskResult result = new TaskResult();
        userRemindMessageService = BeanFactoryUtil.getBean(UserRemindMessageService.class);
        baseService = BeanFactoryUtil.getBean(BaseService.class);
        pmsNotifyHandler = BeanFactoryUtil.getBean(PmsNotifyHandler.class);
        List<UserMessage> list = userRemindMessageService.getNeedSendMessage(1, 1000, 3);
        if (list.isEmpty()) {
            result.setComment("没有待发送的消息");
        } else {
            for (UserMessage message : list) {
                pmsNotifyHandler.sendMessage(message);
            }
            result.setExecuteResult(JobExecuteResult.SUCESS);
        }
        return result;
    }

    @Override
    public ParaCheckResult checkTriggerPara(String triggerDetail) {
        return DEFAULT_SUCCESS_PARA_CHECK;
    }
}
