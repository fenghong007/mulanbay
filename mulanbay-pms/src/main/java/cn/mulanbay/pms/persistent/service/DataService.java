package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.AfterEightHourStat;
import cn.mulanbay.pms.persistent.bean.CommonRecordDateStat;
import cn.mulanbay.pms.persistent.bean.CommonSqlBean;
import cn.mulanbay.pms.persistent.bean.DataInputAnalyseStat;
import cn.mulanbay.pms.persistent.bean.common.SystemFunctionBean;
import cn.mulanbay.pms.persistent.domain.DataInputAnalyse;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.FunctionDataType;
import cn.mulanbay.pms.persistent.util.MysqlUtil;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.commonrecord.CommonRecordDateStatSearch;
import cn.mulanbay.pms.web.bean.request.data.AfterEightHourAnalyseStatSearch;
import cn.mulanbay.pms.web.bean.request.data.DataInputAnalyseStatSearch;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/22.
 */
@Service
@Transactional
public class DataService extends BaseHibernateDao {

    /**
     * 获取平均值
     *
     * @param sf
     * @return
     */
    public List<DataInputAnalyseStat> statDataInputAnalyse(DataInputAnalyseStatSearch sf) {
        try {
            PageRequest pr = sf.buildQuery();
            String hql = "from DataInputAnalyse ";
            hql += pr.getParameterString();
            List<DataInputAnalyse> list = this.getEntityListNoPageHQL(hql, pr.getParameterValue());
            CommonSqlBean sqlBean = this.calSqlBean(list, sf.getStartDate(), sf.getEndDate(), sf.getUserId());
            String sql = "";
            if (sf.getStatType() == DataInputAnalyseStatSearch.StatType.DAY) {
                //按照延期的天来统计
                StringBuffer sb = new StringBuffer();
                sb.append("select res as groupName,count(0) as totalCount from \n");
                sb.append("( \n");
                sb.append("select name, \n");
                sb.append("CASE WHEN n <0 THEN '超前' \n");
                sb.append("WHEN n=0  THEN '当天' \n");
                sb.append("WHEN 1<=n<=3  THEN '1-3天' \n");
                sb.append("WHEN 3<n<=7  THEN '3天到一个星期内' \n");
                sb.append("WHEN 7<n<=30  THEN '一个星期到一个月内' \n");
                sb.append("ELSE '超过一个月' \n");
                sb.append("END as res \n");
                sb.append("from ( \n");
                sb.append(sqlBean.getSqlContent());
                sb.append(" ) as rr ");
                if (!StringUtil.isEmpty(sf.getCompareValue())) {
                    sb.append(" where " + sf.getCompareValue());

                }
                sb.append(" ) as tt ");
                sb.append(" group by res \n");
                sql = sb.toString();
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("select name as groupName,count(0) as totalCount \n");
                sb.append("from ( \n");
                sb.append(sqlBean.getSqlContent());
                sb.append(" ) as rr ");
                if (!StringUtil.isEmpty(sf.getCompareValue())) {
                    sb.append(" where " + sf.getCompareValue());

                }
                sb.append(" group by name \n");
                sql = sb.toString();
            }
            List<DataInputAnalyseStat> result = this.getEntityListWithClassSQL(sql, 0, 0, DataInputAnalyseStat.class, sqlBean.getArgs().toArray());
            return result;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取平均值异常", e);
        }
    }

    /**
     * 计算封装SQL
     *
     * @param list
     * @param startDate
     * @param endDate
     * @param userId
     * @return
     */
    private CommonSqlBean calSqlBean(List<DataInputAnalyse> list, Date startDate, Date endDate, Long userId) {
        CommonSqlBean bean = new CommonSqlBean();
        StringBuffer sb = new StringBuffer();
        int n = list.size();
        for (int i = 0; i < n; i++) {
            DataInputAnalyse dia = list.get(i);
            if (n > 1 && i > 0) {
                sb.append(" \n union \n ");
            }
            sb.append("select '" + dia.getName() + "' as name,(CAST(DATE_FORMAT(" + dia.getInputField() + ",'%Y%m%d') AS signed)-CAST(DATE_FORMAT(" + dia.getBussField() + ",'%Y%m%d') AS signed)) as n from " + dia.getTableName() + " \n");
            sb.append("where " + dia.getBussField() + " is not null ");
            if (startDate != null) {
                sb.append("and " + dia.getBussField() + ">=? ");
                bean.addArg(startDate);
            }
            if (endDate != null) {
                sb.append("and " + dia.getBussField() + "<=? ");
                bean.addArg(endDate);
            }
            if (userId != null && !StringUtil.isEmpty(dia.getUserField())) {
                sb.append("and " + dia.getUserField() + "=? ");
                bean.addArg(userId);
            }
        }
        bean.setSqlContent(sb.toString());
        return bean;
    }

    /**
     * 获取功能点的菜单列表
     * @return
     */
    public List<SystemFunctionBean> getSystemFunctionMenu() {
        try {
            String sql ="select id,name,pid from system_function where function_data_type=? or function_data_type =? order by pid ";
            List<SystemFunctionBean> result = this.getEntityListWithClassSQL(sql, -1, 0,
                    SystemFunctionBean.class, FunctionDataType.MENU.ordinal(), FunctionDataType.PAGE.ordinal());
            return result;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取功能点的菜单列表异常", e);
        }
    }

    public List<AfterEightHourStat> statAfterEightHour(AfterEightHourAnalyseStatSearch sf) {
        if(sf.getChartType()== ChartType.PIE){
            return this.statAfterEightHourForPie(sf);
        }else{
            return this.statAfterEightHourForScatter(sf);
        }
    }

    /**
     * 获取八小时外的统计
     * @return
     */
    public List<AfterEightHourStat> statAfterEightHourForPie(AfterEightHourAnalyseStatSearch sf) {
        try {
            List<AfterEightHourStat> result = new ArrayList<>();
            Date endTime = DateUtil.getDate(DateUtil.getFormatDate(sf.getEndDate(),DateUtil.FormatDay1)+" 23:59:59",DateUtil.Format24Datetime);
            // 音乐练习
            String sql ="select mi.name as name,count(0) as totalCount,sum(minutes) as totalTime,'音乐' as groupName from music_practice mp,music_instrument mi ";
            sql+="where mp.music_instrument_id = mi.id and mp.user_id=? and mp.practice_date>=? and mp.practice_date<=? group by mi.name";
            if(sf.getDataGroup()== AfterEightHourAnalyseStatSearch.DataGroup.GROUP){
                sql="select groupName,sum(totalCount) as totalCount,sum(totalTime) as totalTime from ("+sql+") as aaa ";
            }
            List<AfterEightHourStat> list = this.getEntityListWithClassSQL(sql, -1, 0,
                    AfterEightHourStat.class, sf.getUserId(),sf.getStartDate(),endTime);
            result.addAll(list);
            // 锻炼
            sql ="select st.name as name,count(0) as totalCount,sum(se.minutes) as totalTime,'锻炼' as groupName from sport_exercise se,sport_type st ";
            sql+="where se.sport_type_id = st.id and se.user_id=? and se.exercise_date>=? and se.exercise_date<=? group by st.name";
            if(sf.getDataGroup()== AfterEightHourAnalyseStatSearch.DataGroup.GROUP){
                sql="select groupName,sum(totalCount) as totalCount,sum(totalTime) as totalTime from ("+sql+") as aaa ";
            }
            list = this.getEntityListWithClassSQL(sql, -1, 0,
                    AfterEightHourStat.class, sf.getUserId(),sf.getStartDate(),endTime);
            result.addAll(list);
            //阅读
            sql ="select '看书' as name,count(0) as totalCount,sum(minutes) as totalTime,'看书' as groupName from reading_record_detail where user_id=? ";
            sql+="and read_time>=? and read_time<=?";
            if(sf.getDataGroup()== AfterEightHourAnalyseStatSearch.DataGroup.GROUP){
                sql="select groupName,sum(totalCount) as totalCount,sum(totalTime) as totalTime from ("+sql+") as aaa ";
            }
            list = this.getEntityListWithClassSQL(sql, -1, 0,
                    AfterEightHourStat.class, sf.getUserId(),sf.getStartDate(),endTime);
            result.addAll(list);
            // 通用
            sql ="select crt.name as name,count(0) as totalCount,sum(value) as totalTime,'其他' as groupName from common_record cr,common_record_type crt  ";
            sql+="where cr.common_record_type_id = crt.id and cr.user_id=? and crt.over_work_stat=1 and cr.occur_time>=? and cr.occur_time<=? group by crt.name";
            if(sf.getDataGroup()== AfterEightHourAnalyseStatSearch.DataGroup.GROUP){
                sql="select groupName,sum(totalCount) as totalCount,sum(totalTime) as totalTime from ("+sql+") as aaa ";
            }
            list = this.getEntityListWithClassSQL(sql, -1, 0,
                    AfterEightHourStat.class, sf.getUserId(),sf.getStartDate(),endTime);
            result.addAll(list);

            return result;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取八小时外的统计异常", e);
        }
    }

    /**
     * 根据散点图统计
     * @param sf
     * @return
     */
    public List<AfterEightHourStat> statAfterEightHourForScatter(AfterEightHourAnalyseStatSearch sf) {
        try {
            List<AfterEightHourStat> result = new ArrayList<>();
            Date endTime = DateUtil.getDate(DateUtil.getFormatDate(sf.getEndDate(),DateUtil.FormatDay1)+" 23:59:59",DateUtil.Format24Datetime);
            // 音乐练习
            String sql ="select mi.name as name,WEEKDAY(mp.practice_date)+1 as indexValue,count(0) as totalCount,sum(minutes) as totalTime from music_practice mp,music_instrument mi ";
            sql+="where mp.music_instrument_id = mi.id and mp.user_id=? and mp.practice_date>=? and mp.practice_date<=? group by mi.name,indexValue";
            List<AfterEightHourStat> list = this.getEntityListWithClassSQL(sql, -1, 0,
                    AfterEightHourStat.class, sf.getUserId(),sf.getStartDate(),endTime);
            result.addAll(list);
            // 锻炼
            sql ="select st.name as name,WEEKDAY(se.exercise_date)+1 as indexValue,count(0) as totalCount,sum(se.minutes) as totalTime from sport_exercise se,sport_type st ";
            sql+="where se.sport_type_id = st.id and se.user_id=? and se.exercise_date>=? and se.exercise_date<=? group by st.name,indexValue";
            list = this.getEntityListWithClassSQL(sql, -1, 0,
                    AfterEightHourStat.class, sf.getUserId(),sf.getStartDate(),endTime);
            result.addAll(list);
            //阅读
            sql ="select '阅读' as name,WEEKDAY(read_time)+1 as indexValue,count(0) as totalCount,sum(minutes) as totalTime from reading_record_detail where user_id=? ";
            sql+="and read_time>=? and read_time<=? group by indexValue";
            list = this.getEntityListWithClassSQL(sql, -1, 0,
                    AfterEightHourStat.class, sf.getUserId(),sf.getStartDate(),endTime);
            result.addAll(list);
            // 通用
            sql ="select crt.name as name,WEEKDAY(cr.occur_time)+1 as indexValue,count(0) as totalCount,sum(value) as totalTime from common_record cr,common_record_type crt  ";
            sql+="where cr.common_record_type_id = crt.id and cr.user_id=? and crt.over_work_stat=1 and cr.occur_time>=? and cr.occur_time<=? group by crt.name,indexValue";
            list = this.getEntityListWithClassSQL(sql, -1, 0,
                    AfterEightHourStat.class, sf.getUserId(),sf.getStartDate(),endTime);
            result.addAll(list);

            return result;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取功能点的菜单列表异常", e);
        }
    }



    /**
     * 统计通用记录的基于时间的统计
     * @param sf
     * @return
     */
    public List<CommonRecordDateStat> statDateCommonRecord(CommonRecordDateStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,");
            sb.append("count(0) as totalCount, ");
            sb.append("sum(value) as totalValue ");
            sb.append("from (");
            sb.append("select"+ MysqlUtil.dateTypeMethod("occur_time",dateGroupType)+"as indexValue,");
            sb.append("value from common_record ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            sb.append(" order by indexValue");
            List<CommonRecordDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),CommonRecordDateStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "通用记录的基于时间的统计异常", e);
        }
    }


}
