package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.ReadingRecordAnalyseStat;
import cn.mulanbay.pms.persistent.bean.ReadingRecordDateStat;
import cn.mulanbay.pms.persistent.bean.ReadingRecordReadedStat;
import cn.mulanbay.pms.persistent.domain.BookCategory;
import cn.mulanbay.pms.persistent.domain.ReadingRecord;
import cn.mulanbay.pms.persistent.domain.ReadingRecordDetail;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.util.MysqlUtil;
import cn.mulanbay.pms.web.bean.request.read.ReadingRecordAnalyseStatSearch;
import cn.mulanbay.pms.web.bean.request.read.ReadingRecordDateStatSearch;
import cn.mulanbay.pms.web.bean.request.read.ReadingRecordStatSearch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/2/25.
 */
@Service
public class ReadingRecordService extends BaseHibernateDao {


    /**
     * 阅读统计
     * @param sf
     * @return
     */
    public ReadingRecordReadedStat statReadedReadingRecord(ReadingRecordStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select count(0) as totalCount,");
            sb.append("sum(cost_days) as totalCostDays");
            sb.append(" from reading_record ");
            sb.append(pr.getParameterString());
            sb.append(" and cost_days is not null ");
            List<ReadingRecordReadedStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),ReadingRecordReadedStat.class,pr.getParameterValue());
            return list.get(0);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "阅读统计异常", e);
        }
    }

    /**
     * 阅读统计
     * @param sf
     * @return
     */
    public List<ReadingRecordDateStat> statDateReadingRecord(ReadingRecordDateStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,");
            sb.append("count(0) as totalCount ");
            sb.append("from (");
            sb.append("select"+ MysqlUtil.dateTypeMethod("finished_date",dateGroupType)+"as indexValue");
            sb.append(" from reading_record ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            sb.append(" order by indexValue");
            List<ReadingRecordDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),ReadingRecordDateStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "阅读统计异常", e);
        }
    }

    /**
     * 获取阅读分析
     * @param sf
     * @return
     */
    public List<ReadingRecordAnalyseStat> statReadingRecordAnalyse(ReadingRecordAnalyseStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sql=new StringBuffer();
            ReadingRecordAnalyseStatSearch.GroupType groupType = sf.getGroupType();
            sql.append("select "+groupType.getFieldName());
            sql.append(",count(*) cc from reading_record ");
            sql.append(pr.getParameterString());
            sql.append(" group by "+groupType.getFieldName());
            List<Object[]> list =this.getEntityListNoPageSQL(sql.toString(), pr.getParameterValue());
            List<ReadingRecordAnalyseStat> result = new ArrayList<ReadingRecordAnalyseStat>();
            for (Object[] oo : list) {
                ReadingRecordAnalyseStat bb = new ReadingRecordAnalyseStat();
                Object nameFiled=oo[0];
                if(nameFiled==null){
                    bb.setName("未知");
                }else{
                    Object serierIdObj = oo[0];
                    if(serierIdObj==null){
                        //防止为NULL
                        serierIdObj="0";
                    }
                    String name = getSerierName(serierIdObj.toString(),groupType);
                    bb.setName(name);
                }
                double value =Double.valueOf(oo[1].toString());
                bb.setValue(value);
                result.add(bb);
            }
            return result;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取阅读分析异常", e);
        }
    }

    private String getSerierName(String idStr,ReadingRecordAnalyseStatSearch.GroupType groupType){
        try {
            if(groupType== ReadingRecordAnalyseStatSearch.GroupType.BOOKCATEGORY){
                BookCategory bookCategory = (BookCategory) this.getEntityById(BookCategory.class,Long.valueOf(idStr));
                return bookCategory.getName();
            }else if(groupType== ReadingRecordAnalyseStatSearch.GroupType.IMPORTANTLEVEL){
                return idStr;
            }else if(groupType== ReadingRecordAnalyseStatSearch.GroupType.BOOKTYPE){
                ReadingRecord.BookType bookType= ReadingRecord.BookType.getBookType(Integer.valueOf(idStr));
                return bookType==null ? idStr : bookType.getName();
            }else if(groupType== ReadingRecordAnalyseStatSearch.GroupType.LANGUAGE){
                ReadingRecord.Language language = ReadingRecord.Language.getLanguage(Integer.valueOf(idStr));
                return language==null ? idStr : language.getName();
            }else if(groupType== ReadingRecordAnalyseStatSearch.GroupType.STATUS){
                ReadingRecord.ReadingStatus readingStatus = ReadingRecord.ReadingStatus.getReadingStatus(Integer.valueOf(idStr));
                return readingStatus==null ? idStr : readingStatus.getName();
            }else {
                return idStr;
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取阅读分析分类名称异常", e);
        }
    }

    /**
     * 设置阅读状态为阅读中
     * @param readingRecordId
     */
    public void updateReadingStatusAsReading(Long readingRecordId,Date date){
        try {
            //无论是否已经读完，都更新为在读
            String hql="update ReadingRecord set status=?,lastModifyTime=? where id=? ";
            this.updateEntitys(hql, ReadingRecord.ReadingStatus.READING, date,readingRecordId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "设置阅读状态为阅读中异常", e);
        }
    }

    /**
     * 更新或者新增阅读明细
     * @param bean
     * @param update
     */
    public void saveOrUpdateReadingRecordDetail(ReadingRecordDetail bean,boolean update){
        try {
            if(update){
                this.updateEntity(bean);
            }else{
                this.saveEntity(bean);
            }
            this.updateReadingStatusAsReading(bean.getReadingRecord().getId(),bean.getReadTime());
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "设置阅读状态为阅读中异常", e);
        }
    }

    /**
     * 删除阅读记录
     *
     * @param bean
     */
    public void deleteReadingRecord(ReadingRecord bean){
        try {
            String hql="delete from ReadingRecordDetail where readingRecord.id=?";
            this.updateEntitys(hql,bean.getId());

            this.removeEntity(bean);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "删除阅读记录异常", e);
        }
    }
}
