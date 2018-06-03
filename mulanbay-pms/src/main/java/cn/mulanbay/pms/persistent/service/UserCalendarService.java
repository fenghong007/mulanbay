package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.pms.persistent.domain.UserCalendar;
import cn.mulanbay.pms.persistent.enums.UserCalendarFinishType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-03-12 21:55
 */
@Service
public class UserCalendarService extends BaseHibernateDao {

    /**
     * 获取用户日历
     * @param userId
     * @param bussIdentityKey
     * @param expireTime
     * @return
     */
    public UserCalendar getUserCalendar(Long userId,String bussIdentityKey,Date expireTime) {
        try {
            String hql = "from UserCalendar where userId =? and bussIdentityKey=? and expireTime>=?";
            return (UserCalendar) this.getEntityForOne(hql, userId, bussIdentityKey,expireTime);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_ERROR, "获取用户日历异常",e);

        }
    }

    /**
     * 获取用户目前需要的日历
     * @return
     */
    public List<UserCalendar> getCurrentUserCalendarList(Long userId) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("from UserCalendar where expireTime>=? ");
            if(userId!=null){
                sb.append("and userId="+userId);
            }else{
                sb.append("order by userId");
            }
            return this.getEntityListNoPageHQL(sb.toString(), new Date());
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_ERROR, "获取用户目前需要的日历异常",e);

        }
    }

    /**
     * 获取用户目前需要的日历数
     * @return
     */
    public Long getTodayUserCalendarCount(Long userId) {
        try {
            String hql="select count(0) from UserCalendar where expireTime>=? and userId=? ";

            return this.getCount(hql,new Date(),userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_ERROR, "获取用户目前需要的日历数异常",e);

        }
    }


    /**
     * 更新用户日历
     * @param uc
     * @param newDate
     */
    public void updateUserCalendarToDate(UserCalendar uc,Date newDate,Long messageId){
        try {
            uc.setBussDay(newDate);
            uc.setDelayCounts(uc.getDelayCounts()+1);
            uc.setLastModifyTime(new Date());
            uc.setMessageId(messageId);
            this.updateEntity(uc);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR, "更新用户日历异常",e);
        }
    }

    /**
     * 添加用户日历
     * @param uc
     */
    public void addUserCalendarToDate(UserCalendar uc){
        try {
            uc.setDelayCounts(0);
            uc.setCreatedTime(new Date());
            this.saveEntity(uc);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_ADD_ERROR, "添加用户日历异常",e);
        }
    }

    /**
     * 更新用户日历为已经完成
     *
     * @param userId
     * @param bussIdentityKey
     * @param finishedTime
     * @param finishType
     */
    public void updateUserCalendarForFinish(Long userId, String bussIdentityKey, Date finishedTime, UserCalendarFinishType finishType){
        try {
            String hql = "update UserCalendar set expireTime=?,finishType=?,lastModifyTime=? where bussIdentityKey=? and userId=? ";
            this.updateEntitys(hql,finishedTime,finishType,new Date(),bussIdentityKey,userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR, "更新用户日历为已经完成异常",e);

        }
    }
}
