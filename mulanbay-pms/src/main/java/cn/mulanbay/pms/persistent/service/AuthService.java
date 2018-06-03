package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.UserPointsDateStat;
import cn.mulanbay.pms.persistent.bean.UserPointsSourceStat;
import cn.mulanbay.pms.persistent.bean.UserPointsValueStat;
import cn.mulanbay.pms.persistent.bean.UserRewardPointsStat;
import cn.mulanbay.pms.persistent.domain.*;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.MonitorBussType;
import cn.mulanbay.pms.persistent.enums.RewardSource;
import cn.mulanbay.pms.persistent.util.MysqlUtil;
import cn.mulanbay.pms.web.bean.request.CommonOrderType;
import cn.mulanbay.pms.web.bean.request.user.UserPointsSourceStatSearch;
import cn.mulanbay.pms.web.bean.request.user.UserPointsTimelineStatSearch;
import cn.mulanbay.pms.web.bean.request.user.UserPointsValueStatSearch;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/8/6.
 */
@Service
@Transactional
public class AuthService  extends BaseHibernateDao {

    /**
     * 获取用户微信支付信息
     * @param userId
     * @return
     */
    public UserWxpayInfo getUserWxpayInfo(Long userId,String appId){
        try {
            String hql = "from UserWxpayInfo where userId = ? and appId=? ";
            return (UserWxpayInfo) this.getEntityForOne(hql,userId,appId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,
                    "获取用户微信支付信息异常", e);
        }
    }

    @Cacheable(value = "UserCache", key = "('pms:userWxpayInfo:').concat(#userId).concat(':').concat(#appId)")
    public UserWxpayInfo getUserWxpayInfoForCache(Long userId,String appId){
        return this.getUserWxpayInfo(userId,appId);
    }


    /**
     * 获取用户设置
     * @param userId
     * @return
     */
    public UserSetting getUserSetting(Long userId){
        try {
            String hql = "from UserSetting where userId = ?  ";
            return (UserSetting) this.getEntityForOne(hql,userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,
                    "获取用户设置异常", e);
        }
    }

    @Cacheable(value = "UserCache", key = "('pms:userSetting:').concat(#userId)")
    public UserSetting getUserSettingForCache(Long userId){
        return this.getUserSetting(userId);
    }

    @Cacheable(value = "UserCache", key = "('pms:user:').concat(#userId)")
    public User getUserForCache(Long userId){
        try {
            return (User) this.getEntityById(User.class,userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,
                    "获取用户信息异常", e);        }
    }

    /**
     * 通过手机号或者用户名查询用户
     * @param username
     * @return
     */
    public User getUserByUsernameOrPhone(String username){
        try {
            String hql = "from User where username = ? or phone=?";
            List<User> list = this.getEntityListNoPageHQL(hql,username,username);
            if(list.isEmpty()){
                return null;
            }else {
                return list.get(0);
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用户信息异常", e);
        }
    }

    /**
     * 通过最后一次登录token获取用户
     * @param token
     * @return
     */
    public User getUserByLastLoginTocken(String token){
        try {
            String hql = "from User where lastLoginToken =?";
            List<User> list = this.getEntityListNoPageHQL(hql,token);
            if(list.isEmpty()){
                return null;
            }else {
                return list.get(0);
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用户信息异常", e);
        }
    }

    /**
     * 更新用户积分
     * @param userId
     * @param points
     */
    public void updateUserPoint(Long userId, int points, int rewards, Long sourceId, RewardSource rewardSource,String remark,Long messageId){
        try {
            String hql = "update User set points =? where id=?";
            this.updateEntitys(hql,points,userId);
            UserRewardPointRecord record = new UserRewardPointRecord();
            record.setCreatedTime(new Date());
            record.setSourceId(sourceId);
            record.setRewardSource(rewardSource);
            record.setRewards(rewards);
            record.setUserId(userId);
            record.setAfterPoints(points);
            record.setRemark(remark);
            record.setMessageId(messageId);
            this.saveEntity(record);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "更新用户积分异常", e);
        }
    }

    /**
     * 获取用户当前积分
     * @param userId
     * @return
     */
    public Integer getUserPiont(Long userId){
        try {
            String hql = "select points from User where id=?";
            return (Integer) this.getEntityForOne(hql,userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用户当前积分异常", e);
        }
    }

    /**
     * 获取用户积分统计
     * @param sf
     * @return
     */
    public List<UserPointsDateStat> statDateUserPoints(UserPointsTimelineStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,sum(rewards) as totalRewardPoints ,count(0) as totalCount ");
            sb.append("from (");
            sb.append("select"+ MysqlUtil.dateTypeMethod("created_time",dateGroupType)+"as indexValue,");
            sb.append("rewards from user_reward_point_record ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            sb.append(" order by indexValue");
            List<UserPointsDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),UserPointsDateStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用户积分统计异常", e);
        }
    }

    /**
     * 获取用户积分统计（根据积分值）
     * @param sf
     * @return
     */
    public List<UserPointsValueStat> statUserPointsValue(UserPointsValueStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT source_id as sourceId,rewards,count(0) as totalCount,sum(rewards) as totalRewardPoints ");
            sb.append(" from user_reward_point_record ");
            sb.append(pr.getParameterString());
            sb.append("  group by source_id,rewards");
            List<UserPointsValueStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),UserPointsValueStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用户积分统计异常", e);
        }
    }

    /**
     * 获取用户积分统计(根据来源)
     * @param sf
     * @return
     */
    public List<UserPointsSourceStat> statUserPointsSource(UserPointsSourceStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT source_id as sourceId,reward_source as rewardSource,count(0) as totalCount,sum(rewards) as totalRewardPoints ");
            sb.append(" from user_reward_point_record ");
            sb.append(pr.getParameterString());
            sb.append("  group by source_id,reward_source ");
            if(sf.getOrderBy()== CommonOrderType.COUNTS){
                sb.append("order by totalCount desc");
            }else{
                sb.append("order by totalRewardPoints desc");
            }
            List<UserPointsSourceStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),UserPointsSourceStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用户积分统计异常", e);
        }
    }

    /**
     * 获取用户积分统计
     * @param startTime
     * @param endTime
     * @return
     */
    public List<UserRewardPointsStat> statUserRewardPoints(Date startTime,Date endTime){
        try {
            String sql = "select user_id as userId,count(0) as totalCount,sum(rewards) as totalPoints from user_reward_point_record " +
                    "where created_time>=? and created_time<=? group by user_id";
            return this.getEntityListWithClassSQL(sql,-1,0,UserRewardPointsStat.class,startTime,endTime);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用户积分统计异常", e);
        }
    }

    /**
     * 注册
     * @param user
     * @param us
     */
    public void userRegister(User user,UserSetting us){
        try {
            this.saveEntity(user);
            us.setUserId(user.getId());
            this.saveEntity(us);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_ADD_ERROR,
                    "注册异常", e);
        }
    }

    /**
     * 获取系统监控用户配置
     * @param bussType
     * @return
     */
    public List<SystemMonitorUser> selectSystemMonitorUserList(MonitorBussType bussType){
        try {
            String hql = "from SystemMonitorUser where bussType=0 or bussType=?";
            return this.getEntityListNoPageHQL(hql,bussType);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取系统监控用户配置异常", e);
        }
    }

}
