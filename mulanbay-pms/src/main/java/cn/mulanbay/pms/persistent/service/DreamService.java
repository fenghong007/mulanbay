package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.business.handler.lock.RedisLock;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.DreamStat;
import cn.mulanbay.pms.persistent.domain.Dream;
import cn.mulanbay.pms.persistent.enums.DreamStatus;
import cn.mulanbay.pms.web.bean.request.dream.DreamStatListSearch;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by fenghong on 2017/1/22.
 */
@Service
@Transactional
public class DreamService extends BaseHibernateDao {

    public List<DreamStat> getDreamStat(PageRequest pr, DreamStatListSearch.GroupType groupType){
        try {
            StringBuffer sb =new StringBuffer();
            sb.append("select ");
            sb.append(groupType.getColumn());
            sb.append(",count(0) as totalCount from dream ");
            sb.append(pr.getParameterString());
            sb.append("group by ");
            sb.append(groupType.getColumn());
            sb.append(" order by totalCount desc");
            List<DreamStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),DreamStat.class,pr.getParameterValue());

            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "梦想统计异常", e);
        }
    }

    /**
     * 获取待刷新进度的梦想
     */
    public List<Dream> getNeedRefreshRateDream(Long userId){
        try {
            String hql="from Dream where userPlan.id is not null and status in(?,?) and userId=? ";
            List<Dream> list = this.getEntityListNoPageHQL(hql, DreamStatus.CREATED,DreamStatus.STARTED,userId);
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "梦想统计异常", e);
        }
    }

    /**
     * 获取待刷新进度的梦想
     */
    public List<Dream> getNeedRefreshRateDream(){
        try {
            String hql="from Dream where userPlan.id is not null and status in(?,?) ";
            List<Dream> list = this.getEntityListNoPageHQL(hql, DreamStatus.CREATED,DreamStatus.STARTED);
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "梦想统计异常", e);
        }
    }

    @RedisLock(key = "('pms:distributeLock:province:').concat(#id)",keyType = RedisLock.KeyType.SPEL,sleepMills = 1000)
    public Dream getDream(Long id){
        try {
            return (Dream) this.getEntityById(Dream.class,id);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取梦想异常", e);
        }
    }
}
