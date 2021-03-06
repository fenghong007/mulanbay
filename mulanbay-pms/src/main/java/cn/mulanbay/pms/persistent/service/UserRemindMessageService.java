package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.pms.persistent.domain.UserMessage;
import cn.mulanbay.pms.persistent.enums.MessageSendStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserRemindMessageService extends BaseHibernateDao {


    /**
     * 获取需要发送的消息列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<UserMessage> getNeedSendMessage(int page, int pageSize, int maxFail) {
        try {
            String hql="from UserMessage where (sendStatus=? or (sendStatus=? and failCount<?)) and expectSendTime<=? ";
            return this.getEntityListHQL(hql,page,pageSize, MessageSendStatus.UN_SEND,
                    MessageSendStatus.SEND_FAIL,maxFail,new Date());
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取需要发送的消息列表异常", e);
        }
    }

}
