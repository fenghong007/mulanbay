package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.pms.persistent.bean.CommonSqlBean;
import cn.mulanbay.pms.persistent.bean.NotifyResult;
import cn.mulanbay.pms.persistent.domain.NotifyConfig;
import cn.mulanbay.pms.persistent.domain.UserNotify;
import cn.mulanbay.pms.persistent.domain.UserNotifyRemind;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.enums.ResultType;
import cn.mulanbay.pms.persistent.enums.SqlType;
import cn.mulanbay.pms.persistent.util.MysqlUtil;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/2/2.
 */
@Service
@Transactional
public class NotifyService extends BaseHibernateDao {

    private static Logger logger = Logger.getLogger(NotifyService.class);


    /**
     * 提醒统计
     *
     * @param userId
     * @return
     */
    public List<NotifyResult> statNotify(Long userId) {
        try {
            List<NotifyResult> results = new ArrayList<>();
            String hql = "from UserNotify where status=? order by orderIndex ";
            List<UserNotify> configList = this.getEntityListNoPageHQL(hql, CommonStatus.ENABLE);
            if (configList.isEmpty()) {
                return results;
            } else {
                int i = 1;
                for (UserNotify nc : configList) {
                    NotifyResult notifyResult = this.getNotifyResult(nc,userId);
                    notifyResult.setId(i);
                    if(notifyResult!=null){
                        i++;
                        results.add(notifyResult);
                    }
                }
            }
            return results;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "统计提醒异常", e);
        }
    }

    /**
     * 采用缓存模式
     * @param un
     * @param userId
     * @return
     */
    @Cacheable(value = "notifyStatCache", key = "('pms:notifyStat:').concat(#un.id).concat(':').concat(#userId)")
    public NotifyResult getNotifyResultForCache(UserNotify un, Long userId){
        return this.getNotifyResult(un,userId);
    }


    /**
     * 获取提醒的统计结果
     * @param un
     * @param userId
     * @return
     */
    public NotifyResult getNotifyResult(UserNotify un, Long userId){
        try {
            NotifyConfig nc = un.getNotifyConfig();
            CommonSqlBean sqlBean = this.calSqlBean(un,userId);
            List<Object[]> rr = null;
            if (nc.getSqlType() == SqlType.HQL) {
                rr = this.getEntityListNoPageHQL(sqlBean.getSqlContent(),sqlBean.getArgs().toArray());
            } else {
                rr = this.getEntityListNoPageSQL(sqlBean.getSqlContent(),sqlBean.getArgs().toArray());
            }
            if (!rr.isEmpty()) {
                Object value = rr.get(0);
                NotifyResult notifyResult = new NotifyResult();
                if (value == null) {
                    logger.warn("统计出" + nc.getName() + "的结果为空");
                    if (nc.getResultType() == ResultType.DATE || nc.getResultType() == ResultType.NAME_DATE) {
                        //notifyResult.setDateValue(new Date(0));
                    } else if (nc.getResultType() == ResultType.NUMBER || nc.getResultType() == ResultType.NAME_NUMBER) {
                        notifyResult.setNumValue(0L);
                    }
                } else {
                    if (nc.getResultType() == ResultType.DATE) {
                        notifyResult.setDateValue((Date) value);
                    } else if (nc.getResultType() == ResultType.NUMBER) {
                        notifyResult.setNumValue(Long.valueOf(value.toString()));
                    } else if (nc.getResultType() == ResultType.NAME_DATE) {
                        Object[] dupliteValue = (Object[]) value;
                        Object nameValue = dupliteValue[0];
                        Object dateValue = dupliteValue[1];
                        notifyResult.setName(nameValue.toString());
                        notifyResult.setDateValue((Date) dateValue);
                    } else if (nc.getResultType() == ResultType.NAME_NUMBER) {
                        Object[] dupliteValue = (Object[]) value;
                        Object nameValue = dupliteValue[0];
                        Object numValue = dupliteValue[1];
                        notifyResult.setName(nameValue.toString());
                        notifyResult.setNumValue(Long.valueOf(numValue.toString()));
                    }

                }
                notifyResult.setUserNotify(un);
                //计算
                notifyResult.calculte();
                return notifyResult;
            }else{
                NotifyResult nr = new NotifyResult();
                nr.setName("--");
                nr.setUserNotify(un);
                return nr;
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "统计提醒["+un.getTitle()+"]异常", e);
        }
    }
    /**
     * 计算封装SQL
     *
     * @param un
     * @param userId
     * @return
     */
    private CommonSqlBean calSqlBean(UserNotify un, Long userId) {
        CommonSqlBean bean = new CommonSqlBean();
        StringBuffer sb = new StringBuffer();
        NotifyConfig notifyConfig = un.getNotifyConfig();
        sb.append(notifyConfig.getSqlContent());
        String userField = notifyConfig.getUserField();
        if (!StringUtil.isEmpty(userField)) {
            if(notifyConfig.getSqlContent().contains("user_id=?")){
                //说明一些SQL语句中已经包含（特殊统计）
            }else{
                //添加用户过滤
                if (!notifyConfig.getSqlContent().contains("where")) {
                    sb.append(" where " + userField + "=? ");
                }else{
                    sb.append(" and " + userField + "=? ");
                }
            }
            bean.addArg(userId);
        }
        String sqlContent = sb.toString();
        sqlContent = MysqlUtil.replaceBindValues(sqlContent,un.getBindValues());
        bean.setSqlContent(sqlContent);
        return bean;
    }

    /**
     *
     * @param userNotifyId
     * @param userId
     * @return
     */
    public UserNotifyRemind getUserNotifyRemind(Long userNotifyId,Long userId){
        try {
            String hql="from UserNotifyRemind where userNotify.id=? and userId=? ";
            return (UserNotifyRemind) this.getEntityForOne(hql,userNotifyId,userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR,
                    "查找用户提醒异常", e);
        }
    }

    /**
     * 更新最后提醒时间
     * @param remindId
     */
    public void updateLastRemindTime(Long remindId,Date date){
        try {
            String hql="update UserNotifyRemind set lastRemindTime=? where id=?";
            this.updateEntitys(hql,date,remindId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR,
                    "更新最后提醒时间异常", e);
        }
    }


    /**
     * 获取需要提醒的用户提醒列表
     * @return
     */
    public List<UserNotify> getNeedRemindUserNotify(){
        try {
            String hql="from UserNotify where status=? and remind=? ";
            return this.getEntityListNoPageHQL(hql, CommonStatus.ENABLE,true);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取需要提醒的用户提醒列表异常", e);
        }
    }

    /**
     * 获取提醒配置列表
     * @return
     */
    public List<NotifyConfig> getNotifyConfigList(Integer minLevel){
        try {
            String hql="from NotifyConfig where status=? and level<=? ";
            return this.getEntityListNoPageHQL(hql, CommonStatus.ENABLE,minLevel);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取提醒配置列表异常", e);
        }
    }

    /**
     * 获取提醒配置
     * 需要根据用户级别判断
     *
     * @return
     */
    public NotifyConfig getNotifyConfig(Long id,Integer userLevel){
        try {
            String hql="from NotifyConfig where id=? and level<=? ";
            return (NotifyConfig) this.getEntityForOne(hql, id,userLevel);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取提醒配置列表异常", e);
        }
    }

    /**
     * 获取用户有效的提醒配置
     * @return
     */
    public List<UserNotify> getActiveUserNotifyList(){
        try {
            String hql="from UserNotify where status=? ";
            return (List<UserNotify>) this.getEntityForOne(hql, CommonStatus.ENABLE);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用户有效的提醒配置异常", e);
        }
    }


}
