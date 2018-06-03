package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.DietAnalyseStat;
import cn.mulanbay.pms.persistent.domain.Diet;
import cn.mulanbay.pms.persistent.enums.DietSource;
import cn.mulanbay.pms.persistent.enums.DietType;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.diet.DietAnalyseSearch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DietService extends BaseHibernateDao {

    /**
     * 获取饮食习惯列表
     * @param startTime
     * @param endTime
     * @param userId
     * @param dietSource
     * @param dietType
     * @return
     */
    public List<Diet> getDietList(Date startTime,Date endTime,Long userId,DietSource dietSource,DietType dietType){
        try {
            String hql="from Diet where userId=? and occurTime>=? and occurTime<=? ";
            List args = new ArrayList();
            args.add(userId);
            args.add(startTime);
            args.add(endTime);
            if(dietSource!=null){
                hql+="and dietSource=? ";
                args.add(dietSource);
            }
            if(dietType!=null){
                hql+="and dietType=? ";
                args.add(dietType);
            }
            hql+="order by occurTime ";
            List<Diet> list = this.getEntityListNoPageHQL(hql,args.toArray());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取饮食习惯列表异常", e);
        }
    }

    /**
     * 饮食分析
     * @param sf 查询条件
     * @return
     */
    public List<DietAnalyseStat> statDietAnalyse(DietAnalyseSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select name,count(0) as totalCount from  ");
            sb.append("( ");
            sb.append("select substring_index(substring_index(a.foods,',',b.help_topic_id+1),',',-1)  as name ");
            sb.append("from diet a join mysql.help_topic b ");
            sb.append("on b.help_topic_id < (length(a.foods) - length(replace(a.foods,',',''))+1) ");
            sb.append(pr.getParameterString());
            sb.append(" ) as res");
            sb.append(" group by name ");
            if(sf.getChartType()== ChartType.BAR){
                sb.append(" order by totalCount desc ");
            }
            List<DietAnalyseStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),DietAnalyseStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "饮食分析异常", e);
        }
    }
}
