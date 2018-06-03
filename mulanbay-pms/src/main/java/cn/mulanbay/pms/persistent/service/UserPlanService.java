package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.pms.persistent.domain.PlanConfig;
import cn.mulanbay.pms.persistent.domain.UserPlan;
import cn.mulanbay.pms.persistent.domain.UserPlanConfigValue;
import cn.mulanbay.pms.persistent.domain.UserPlanRemind;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.enums.PlanType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserPlanService extends BaseHibernateDao {

    /**
     * 保存用户计划
     * @param bean
     */
    public void saveUsePlan(UserPlan bean){
        try {
            bean.setCreatedTime(new Date());
            this.saveEntity(bean);
            PlanConfig planConfig = (PlanConfig) this.getEntityById(PlanConfig.class,bean.getPlanConfig().getId());
            // 保存默认的值
            UserPlanConfigValue upcv = new UserPlanConfigValue();
            upcv.setCreatedTime(new Date());
            upcv.setPlanCountValue(planConfig.getDefaultPlanCountValue());
            upcv.setPlanValue(planConfig.getDefaultPlanValue());
            upcv.setUserPlan(bean);
            upcv.setUserId(bean.getUserId());
            upcv.setYear(DateUtil.getYear(new Date()));
            this.saveEntity(upcv);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_ADD_ERROR,
                    "保存用户计划异常", e);
        }
    }

    /**
     * 删除用户计划
     * @param userPlan
     */
    public void deleteUsePlan(UserPlan userPlan){
        try {
            String sql="delete from user_plan_remind where userPlanId=? ";
            this.execSqlUpdate(sql,userPlan.getId());
            //删除计划值
            sql="delete from user_plan_config_value where userPlanId=?";
            this.execSqlUpdate(sql,userPlan.getId());
            //删除计划配置
            sql ="delete from user_plan where id=?";
            this.execSqlUpdate(sql,userPlan.getId());
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,
                    "删除用户计划异常", e);
        }
    }

    /**
     * 查找用户计划提醒
     * @param userPlanId
     */
    public Long getRemindIdByUserPlan(Long userPlanId){
        try {
            String hql="select id from UserPlanRemind where userPlan.id=?";
            return (Long) this.getEntityForOne(hql,userPlanId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR,
                    "查找用户计划提醒异常", e);
        }
    }

    /**
     * 查找用户计划提醒信息，很少的数据
     * @param id
     */
    public UserPlanRemind getRemindNotifyInfo(Long id){
        try {
            String hql="select new UserPlanRemind(id,triggerType,triggerIntervel,remindTime) from UserPlanRemind where id=?";
            return (UserPlanRemind) this.getEntityForOne(hql,id);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR,
                    "查找用户计划提醒异常", e);
        }
    }

    /**
     * 查找用户计划提醒
     * @param userPlanId
     */
    public UserPlanRemind getRemindByUserPlan(Long userPlanId,Long userId){
        try {
            String hql="from UserPlanRemind where userPlan.id=? and userId=?";
            return (UserPlanRemind) this.getEntityForOne(hql,userPlanId,userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR,
                    "查找用户计划提醒异常", e);
        }
    }

    /**
     * 更新最后提醒时间
     * @param remindId
     */
    public void updateLastRemindTime(Long remindId,Date date){
        try {
            String hql="update UserPlanRemind set lastRemindTime=? where id=?";
            this.updateEntitys(hql,date,remindId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR,
                    "更新最后提醒时间异常", e);
        }
    }

    /**
     * 获取需要提醒的用户计划列表
     * @param planType
     * @return
     */
    public List<UserPlan> getNeedRemindUserPlan(PlanType planType){
        try {
            String hql="from UserPlan where planConfig.planType=? and status=? and remind=? ";
            return this.getEntityListNoPageHQL(hql,planType, CommonStatus.ENABLE,true);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取需要提醒的用户计划列表异常", e);
        }
    }

    /**
     * 获取有效的用户计划列表
     * @return
     */
    public List<UserPlan> getActiveUserPlan(){
        try {
            String hql="from UserPlan where status=? ";
            return this.getEntityListNoPageHQL(hql,CommonStatus.ENABLE);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取有效的用户计划列表异常", e);
        }
    }
}
