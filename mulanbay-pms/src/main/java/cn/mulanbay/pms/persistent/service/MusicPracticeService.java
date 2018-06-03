package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.*;
import cn.mulanbay.pms.persistent.domain.MusicInstrument;
import cn.mulanbay.pms.persistent.domain.MusicPractice;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.util.MysqlUtil;
import cn.mulanbay.pms.web.bean.request.ChartOrderType;
import cn.mulanbay.pms.web.bean.request.music.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by fenghong on 2017/1/24.
 */
@Service
@Transactional
public class MusicPracticeService extends BaseHibernateDao {

    /**
     * 练习的曲子统计
     * 如果是hql查询，那么返回结果绑定字段是映射的类型
     * @param sf
     * @return
     */
    public List<MusicPracticeTuneStat> statTune(MusicPracticeTuneSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select tune as name,sum(times) as totalTimes from MusicPracticeTune ");
            sb.append(pr.getParameterString());
            sb.append("group by tune order by totalTimes desc");
            List<MusicPracticeTuneStat> list = this.getEntityListWithClassHQL(sb.toString(),pr.getPage(),pr.getPageSize(),MusicPracticeTuneStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "练习的曲子统计", e);
        }
    }

    /**
     * 获取口琴练习总的统计
     * @param sf
     * @return
     */
    @SuppressWarnings("unchecked")
    public MusicPracticeSummaryStat musicPracticeSummaryStat(MusicPracticeStatSearch sf) {
        try {
            PageRequest pr = sf.buildQuery();
            String sql = "select count(*) as totalCount,sum(minutes) as totalMinutes from music_practice";
            sql += pr.getParameterString();
            List<MusicPracticeSummaryStat> list = this.getEntityListWithClassSQL(sql,pr.getPage(),pr.getPageSize(),MusicPracticeSummaryStat.class,pr.getParameterValue());
            return list.get(0);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取口琴练习总的统计异常", e);
        }
    }

    /**
     * 获取口琴练习乐器的统计
     * @param sf
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MusicPracticeInstrumentStat> musicPracticeInstrumentStat(MusicPracticeStatSearch sf) {
        try {
            PageRequest pr = sf.buildQuery();
            String sql = "select music_instrument_id as id,count(*) as totalCount,sum(minutes) as totalMinutes from music_practice";
            sql += pr.getParameterString();
            sql+=" group by music_instrument_id";
            List<MusicPracticeInstrumentStat> list = this.getEntityListWithClassSQL(sql,pr.getPage(),pr.getPageSize(),MusicPracticeInstrumentStat.class,pr.getParameterValue());
            //获取乐器名
            for(MusicPracticeInstrumentStat mp : list){
                String mi = this.getMusicInstrumentName(mp.getId().longValue());
                mp.setName(mi);
            }
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取口琴练习总的统计异常", e);
        }
    }

    /**
     * 口琴练习统计
     * @param sf
     * @return
     */
    public List<MusicPracticeDateStat> statDateMusicPractice(MusicPracticeDateStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,sum(minutes) as totalMinutes ,count(0) as totalCount ");
            sb.append("from (");
            sb.append("select"+MysqlUtil.dateTypeMethod("practice_date",dateGroupType)+"as indexValue,");
            sb.append("minutes from music_practice ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            sb.append(" order by indexValue");
            List<MusicPracticeDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),MusicPracticeDateStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "口琴练习统计异常", e);
        }
    }

    /**
     * 口琴练习统计
     * @param sf
     * @return
     */
    public List<MusicPracticeTimeStat> statTimeMusicPractice(MusicPracticeTimeStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,count(0) as totalCount ");
            sb.append("from (");
            if(dateGroupType==DateGroupType.MINUTE){
                //统计花费的练习时间
                sb.append("select minutes as indexValue");
            }else{
                sb.append("select "+ MysqlUtil.dateTypeMethod("practice_start_time",dateGroupType)+" as indexValue");
            }
            sb.append(" from music_practice ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            if(sf.getChartOrderType()== ChartOrderType.X){
                sb.append(" order by indexValue");
            }else{
                sb.append(" order by totalCount desc");
            }
            List<MusicPracticeTimeStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),MusicPracticeTimeStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "口琴练习统计异常", e);
        }
    }

    /**
     * 音乐练习比对统计
     * @param sf
     * @return
     */
    public List<MusicPracticeCompareStat> statCompareMusicPractice(MusicPracticeCompareStatSearch sf,Long musicInstrumentId){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select "+ MysqlUtil.dateTypeMethod("practice_start_time",sf.getXgroupType())+" as xValue,");
            sb.append(MysqlUtil.dateTypeMethod("practice_start_time",sf.getYgroupType())+" as yValue ");
            sb.append(" from music_practice ");
            sb.append(pr.getParameterString());
            sb.append(" and music_instrument_id = ? ");
            List args = pr.getParameterValueList();
            args.add(musicInstrumentId);
            List<MusicPracticeCompareStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),MusicPracticeCompareStat.class,args.toArray());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "音乐练习比对统计异常", e);
        }
    }

    /**
     * 获取乐器名称
     * @param musicInstrumentId
     * @return
     */
    public String getMusicInstrumentName(Long musicInstrumentId){
        try {
            if(musicInstrumentId==null||musicInstrumentId==0){
                return "音乐";
            }else{
                MusicInstrument musicInstrument = (MusicInstrument) this.getEntityById(MusicInstrument.class,musicInstrumentId);
                if(musicInstrument==null){
                    return "未知";
                }else{
                    return musicInstrument.getName();
                }
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取乐器名称异常", e);        }
    }

    /**
     * 获取看病分类列表，统计聚合
     * @return
     */
    public List<String> getMusicPracticeTuneList(MusicPracticeTuneTreeSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb = new StringBuffer();
            sb.append("select distinct tune ");
            sb.append(" from MusicPracticeTune ");
            sb.append(pr.getParameterString());
            //sb.append("order by musicPractice.practiceDate desc ");
            return this.getEntityListHQL(sb.toString(),0,0,pr.getParameterValue());
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取看病分类列表异常", e);
        }
    }

    /**
     * 删除音乐练习记录
     *
     * @param musicPractice
     */
    public void deleteMusicPractice(MusicPractice musicPractice){
        try {

            //删除曲子
            String hql="delete from MusicPracticeTune where musicPractice.id=?";
            this.updateEntitys(hql,musicPractice.getId());
            //删除记录
            this.removeEntity(musicPractice);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "删除音乐练习记录异常", e);
        }
    }
}
