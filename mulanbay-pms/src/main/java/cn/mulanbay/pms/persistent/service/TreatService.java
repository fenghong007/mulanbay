package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.*;
import cn.mulanbay.pms.persistent.domain.TreatDrug;
import cn.mulanbay.pms.persistent.domain.TreatDrugDetail;
import cn.mulanbay.pms.persistent.domain.TreatRecord;
import cn.mulanbay.pms.persistent.enums.BodyAbnormalRecordGroupType;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.util.MysqlUtil;
import cn.mulanbay.pms.web.bean.request.bodyabnormal.BodyAbnormalRecordDateStatSearch;
import cn.mulanbay.pms.web.bean.request.bodyabnormal.BodyAbnormalRecordStatSearch;
import cn.mulanbay.pms.web.bean.request.bodybasicInfo.BodyBasicInfoDateStatSearch;
import cn.mulanbay.pms.web.bean.request.health.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/31.
 * 看病service
 */
@Service
@Transactional
public class TreatService  extends BaseHibernateDao {

    /**
     * 获取看病用药的疾病分类列表，统计聚合
     * @return
     */
    public List<String> getTreatDrugCategoryList(TreatDrugDiseaseCategorySearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select distinct ");
            sb.append("disease");
            sb.append(" from treat_drug ");
            sb.append(pr.getParameterString());
            sb.append(" order by disease");
            return this.getEntityListSQL(sb.toString(),0,0,pr.getParameterValue());
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取看病用药的疾病分类列表异常", e);
        }
    }


    /**
     * 获取看病分类列表，统计聚合
     * @return
     */
    public List<String> getTreatCategoryList(TreatCategorySearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select distinct ");
            sb.append(sf.getGroupField());
            sb.append(" from TreatRecord ");
            sb.append(pr.getParameterString());
            sb.append(" order by ");
            sb.append(sf.getGroupField());
            return this.getEntityListHQL(sb.toString(),0,0,pr.getParameterValue());
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取看病分类列表异常", e);
        }
    }

    /**
     * 获取看病记录总的统计
     * @param sf
     * @return
     */
    public TreatRecordSummaryStat statTreatRecord(TreatRecordSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select count(0) as totalCount,");
            sb.append("sum(registeredFee) as totalRegisteredFee,");
            sb.append("sum(drugFee) as totalDrugFee,");
            sb.append("sum(operationFee) as totalOperationFee,");
            sb.append("sum(totalFee) as totalTotalFee,");
            sb.append("sum(medicalInsurancePaidFee) as totalMedicalInsurancePaidFee,");
            sb.append("sum(personalPaidFee) as totalPersonalPaidFee");
            sb.append(" from TreatRecord ");
            sb.append(pr.getParameterString());
            List<TreatRecordSummaryStat> list = this.getEntityListWithClassHQL(sb.toString(),pr.getPage(),pr.getPageSize(),TreatRecordSummaryStat.class,pr.getParameterValue());
            return list.get(0);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取看病记录统计异常", e);
        }
    }

    /**
     * 获取看病记录分析
     * @param sf
     * @return
     */
    public List<TreatRecordAnalyseStat> treatRecordAnalyseStat(TreatRecordAnalyseStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            String groupField = sf.getGroupField();
            String feeField = sf.getFeeField();
            StringBuffer sb = new StringBuffer();
            sb.append("select "+groupField+" as name,");
            sb.append("count(0) as totalCount,");
            sb.append("sum("+feeField+") as totalFee");
            sb.append(" from treat_record ");
            sb.append(pr.getParameterString());
            sb.append(" group by "+groupField);
            sb.append(" order by totalCount desc");
            List<TreatRecordAnalyseStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),TreatRecordAnalyseStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取看病记录分析异常", e);
        }
    }

    /**
     * 获取看病记录手术统计
     * @param pr
     * @return
     */
    public List<TreatOperationStat> treatOperationStat(PageRequest pr){
        try {
            pr.setNeedWhere(false);
            StringBuffer sb = new StringBuffer();
            sb.append("select name,count(0) as totalCount,sum(total_fee) as totalFee from (");
            sb.append("select treatOperation.name,treatRecord.total_fee from treat_operation treatOperation,treat_record treatRecord ");
            sb.append("where treatOperation.treat_record_id= treatRecord.id ");
            sb.append(pr.getParameterString());
            sb.append(") as aa");
            sb.append(" group by name order by totalCount desc");
            List<TreatOperationStat> list = this.getEntityListWithClassSQL(sb.toString(), pr.getPage(), pr.getPageSize(), TreatOperationStat.class, pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取看病记录手术统计异常", e);
        }
    }

    /**
     * 删除看病记录
     * @param treatRecord
     */
    public void deleteTreatRecord(TreatRecord treatRecord){
        try {
            // step 1 删除看病记录中的手术记录
            String hql="delete from TreatOperation where treatRecord.id=?";
            this.updateEntitys(hql,treatRecord.getId());

            // step 2 删除看病记录中的用药记录
            hql="delete from TreatDrug where treatRecord.id=?";
            this.updateEntitys(hql,treatRecord.getId());

            // step 3 删除看病记录
            this.removeEntity(treatRecord);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,
                    "删除看病记录异常", e);
        }
    }

    /**
     * 获取身体不适统计
     * @param sf
     * @return
     */
    public List<BodyAbnormalRecordStat> bodyAbnormalRecordStat(BodyAbnormalRecordStatSearch sf) {
        try {
            BodyAbnormalRecordGroupType groupField = sf.getGroupField();
            PageRequest pr = sf.buildQuery();
            List args = pr.getParameterValueList();
            StringBuffer sb = new StringBuffer();
            sb.append("select name,count(0) as totalCount,sum(last_days) as totalLastDays,max(occur_date) as maxOccurDate,min(occur_date) as minOccurDate ");
            sb.append("from (select ");
            if(groupField== BodyAbnormalRecordGroupType.DISEASE|| groupField==BodyAbnormalRecordGroupType.ORGAN){
                sb.append(groupField.getField()+" as name");
            }else{
                //数字转换为字符
                sb.append( "CAST("+groupField.getField()+" AS CHAR) as name");
            }
            sb.append(",last_days,occur_date from body_abnormal_record ");
            sb.append(pr.getParameterString());
            if(!StringUtil.isEmpty(sf.getName())){
                //添加检索
                sb.append(" and "+groupField.getField()+" like ?");
                args.add("%"+sf.getName()+"%");
            }
            sb.append(") tt group by name order by totalCount desc");
            List<BodyAbnormalRecordStat> list = this.getEntityListWithClassSQL(sb.toString(), pr.getPage(), pr.getPageSize(), BodyAbnormalRecordStat.class, args.toArray());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取身体不适统计异常", e);
        }
    }

    /**
     * 统计身体不适的基于时间的统计
     * @param sf
     * @return
     */
    public List<BodyAbnormalRecordDateStat> statDateBodyAbnormalRecord(BodyAbnormalRecordDateStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,");
            sb.append("count(0) as totalCount, ");
            sb.append("sum(last_days) as totalLastDays ");
            sb.append("from (");
            sb.append("select"+ MysqlUtil.dateTypeMethod("occur_date",dateGroupType)+"as indexValue,");
            sb.append("last_days from body_abnormal_record ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            sb.append(" order by indexValue");
            List<BodyAbnormalRecordDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),BodyAbnormalRecordDateStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "身体不适的基于时间的统计异常", e);
        }
    }

    /**
     * 统计身体不适的基于时间的统计
     * @param sf
     * @return
     */
    public BodyBasicInfoAvgStat statAvgBodyBasicInfo(BodyBasicInfoDateStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select avg(weight) as avgWeight,avg(height) as avgHeight ");
            sb.append("from body_basic_info ");
            sb.append(pr.getParameterString());
            BodyBasicInfoAvgStat stat = (BodyBasicInfoAvgStat) this.getEntityListWithClassSQLForOne(sb.toString(),BodyBasicInfoAvgStat.class,pr.getParameterValue());
            return stat;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "身体不适的基于时间的统计异常", e);
        }
    }

    /**
     * 统计身体基本情况的基于时间的统计
     * @param sf
     * @return
     */
    public List<BodyBasicInfoDateStat> statDateBodyBasicInfo(BodyBasicInfoDateStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,");
            sb.append("count(0) as totalCount, ");
            sb.append("sum(weight) as totalWeight,sum(height) as totalHeight,sum(bmi) as totalBmi ");
            sb.append("from (");
            sb.append("select"+ MysqlUtil.dateTypeMethod("record_date",dateGroupType)+"as indexValue,");
            sb.append("weight,height,bmi from body_basic_info ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            sb.append(" order by indexValue");
            List<BodyBasicInfoDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),BodyBasicInfoDateStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "统计身体基本情况的基于时间的统计异常", e);
        }
    }

    /**
     * 获取最新的看病记录
     * @param name
     * @param groupField
     * @return
     */
    public TreatRecord getLatestTreatRecord(String name,BodyAbnormalRecordGroupType groupField,Long userId){
        try {
            String fieldName = "organ";
            if(groupField== BodyAbnormalRecordGroupType.DISEASE){
                fieldName = "disease";
            }
            StringBuffer sb = new StringBuffer();
            sb.append("from TreatRecord ");
            sb.append("where "+fieldName +"=? and userId=? ");
            sb.append("and treatDate=(");
            sb.append("select max(treatDate) from TreatRecord  where "+fieldName +"=? and userId=? )");
            return (TreatRecord) this.getEntityForOne(sb.toString(),name,userId,name,userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取最新的看病记录异常", e);
        }
    }

    /**
     * 获取最新的看病记录
     * @param sf
     * @return
     */
    public TreatRecordAnalyseDetailStat statDetailTreatRecordAnalyse(TreatRecordAnalyseDetailStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            String paraString = pr.getParameterString();
            List args = pr.getParameterValueList();
            StringBuffer statSql = new StringBuffer();
            statSql.append("select count(0) as totalCount,sum(total_fee) as totalFee ,min(treat_date) as minTreatDate,max(treat_date) as maxTreatDate ");
            statSql.append("from treat_record ");
            statSql.append(paraString);
            TreatRecordAnalyseDetailStat stat = (TreatRecordAnalyseDetailStat) this.getEntityListWithClassSQLForOne(statSql.toString(),TreatRecordAnalyseDetailStat.class,args.toArray());
            //获取详情
            if(stat.getMinTreatDate()!=null){
                String hql= "from TreatRecord "+paraString+" and treatDate = ?";
                List newArgs = new ArrayList();
                newArgs.addAll(args);
                newArgs.add(stat.getMinTreatDate());
                TreatRecord minTreatRecord =  (TreatRecord) this.getEntityForOne(hql,newArgs.toArray());
                stat.setMinTreatRecord(minTreatRecord);
            }
            if(stat.getMaxTreatDate()!=null){
                String hql= "from TreatRecord "+paraString+" and treatDate = ?";
                List newArgs = new ArrayList();
                newArgs.addAll(args);
                newArgs.add(stat.getMaxTreatDate());
                TreatRecord maxTreatRecord =  (TreatRecord) this.getEntityForOne(hql,newArgs.toArray());
                stat.setMaxTreatRecord(maxTreatRecord);
            }
            return stat;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取最新的看病记录异常", e);
        }
    }

    /**
     * 保存或者更新用药详情
     * @param bean
     * @return
     */
    public void saveOrUpdateTreatDrugDetail(TreatDrugDetail bean){
        try {
            if(bean.getId()==null){
                this.saveEntity(bean);
            }else {
                this.updateEntity(bean);
            }
            Date compareDate = DateUtil.getDate(bean.getOccurTime(),DateUtil.FormatDay1);
            //更新药品记录的数据
            String hql="update TreatDrug set beginDate=? where id=? and (beginDate is null or beginDate>?) ";
            this.updateEntitys(hql,compareDate,bean.getTreatDrug().getId(),compareDate);
            String hql2="update TreatDrug set endDate=? where id=? and (endDate is null or endDate<?) ";
            this.updateEntitys(hql2,compareDate,bean.getTreatDrug().getId(),compareDate);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "保存或者更新用药详情异常", e);
        }
    }

    /**
     * 统计看病记录
     * @param sf
     * @return
     */
    public List<TreatRecordDateStat> statDateTreatRecord(TreatRecordDateStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            String feeField = sf.getFeeField();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,");
            sb.append("count(0) as totalCount, ");
            sb.append("sum(" + feeField + ") as totalFee ");
            sb.append("from (");
            sb.append("select"+ MysqlUtil.dateTypeMethod("treat_date",dateGroupType)+"as indexValue,");
            sb.append(feeField+" from treat_record ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            sb.append(" order by indexValue");
            List<TreatRecordDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),TreatRecordDateStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "统计看病记录异常", e);
        }
    }

    /**
     * 获取需要提醒的药品
     * 条件为：1设置了提醒 2还处在用药期间
     * @param date
     * @return
     */
    public List<TreatDrug> getNeedRemindDrug(Date date){
        try {

            String hql="from TreatDrug where remind=1 and beginDate<=? and endDate>=? ";
            return this.getEntityListNoPageHQL(hql,date,date);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取需要提醒的药品异常", e);
        }
    }

    /**
     * 获取用药次数
     * @param date
     * @return
     */
    public long getDrugDetailCount(Long treatDrugId,Date date){
        try {
            Date startTime = DateUtil.getFormMiddleNightDate(date);
            Date endTime = DateUtil.getTodayTillMiddleNightDate(date);
            String hql="select count(0) as n from TreatDrugDetail where treatDrug.id=? and occurTime>=? and occurTime<=? ";
            return this.getCount(hql,treatDrugId,startTime,endTime);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取用药次数异常", e);
        }
    }

}
