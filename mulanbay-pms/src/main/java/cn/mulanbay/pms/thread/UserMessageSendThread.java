package cn.mulanbay.pms.thread;

import cn.mulanbay.pms.handler.PmsNotifyHandler;
import cn.mulanbay.pms.persistent.domain.UserMessage;

/**
 * ${DESCRIPTION}
 * 消息发送线程
 *
 * @author fenghong
 * @create 2018-02-20 18:23
 */
public class UserMessageSendThread extends Thread {

    private UserMessage message;

    private PmsNotifyHandler pmsNotifyHandler;

    public UserMessageSendThread(UserMessage message, PmsNotifyHandler pmsNotifyHandler) {
        super("消息发送");
        this.message = message;
        this.pmsNotifyHandler = pmsNotifyHandler;
    }

    @Override
    public void run() {
        pmsNotifyHandler.sendMessage(message);
    }
}
