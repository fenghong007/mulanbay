package cn.mulanbay.schedule;

/**
 * ${DESCRIPTION}
 * 通知
 * @author fenghong
 * @create 2017-11-15 16:33
 **/
public interface NotifiableProcessor {

    void notifyMessage(Long taskTriggerId, String title, String message);
}
