package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.LifeExperienceDateStat;
import cn.mulanbay.pms.persistent.bean.LifeExperienceMapStat;
import cn.mulanbay.pms.persistent.bean.TransferMapStat;
import cn.mulanbay.pms.persistent.domain.*;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.util.MysqlUtil;
import cn.mulanbay.pms.web.bean.request.life.LifeExperienceDateStatSearch;
import cn.mulanbay.pms.web.bean.request.life.LifeExperienceMapStatSearch;
import cn.mulanbay.pms.web.bean.request.life.LifeExperienceRevise;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/14.
 */
@Service
@Transactional
public class LifeExperienceService extends BaseHibernateDao {

    /**
     * 人生经历地图统计
     * @param sf
     * @return
     */
    public List<LifeExperienceMapStat> getLifeExperienceMapStat(LifeExperienceMapStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            LifeExperienceMapStatSearch.MapType mapType = sf.getMapType();
            StringBuffer sb =new StringBuffer();
            if(mapType== LifeExperienceMapStatSearch.MapType.CHINA){
                sb.append("select  CAST(province_id AS CHAR) as name");
            }else if(mapType== LifeExperienceMapStatSearch.MapType.WORLD){
                sb.append("select country as name");
            }else if(mapType== LifeExperienceMapStatSearch.MapType.LOCATION){
                sb.append("select arrive_city as name");
            }
            sb.append(",count(0) as totalCount,count(0) as totalDays from life_experience_detail ");
            sb.append(pr.getParameterString());
            sb.append(" and map_stat=1 ");
            if(mapType== LifeExperienceMapStatSearch.MapType.CHINA){
                sb.append(" group by province_id");
            }else if(mapType== LifeExperienceMapStatSearch.MapType.WORLD){
                sb.append(" group by country");
            }else if(mapType== LifeExperienceMapStatSearch.MapType.LOCATION){
                sb.append(" group by arrive_city");
            }
            List<LifeExperienceMapStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),LifeExperienceMapStat.class,pr.getParameterValue());
            if(mapType== LifeExperienceMapStatSearch.MapType.CHINA){
                for(LifeExperienceMapStat bb : list){
                    if(!StringUtil.isEmpty(bb.getName())){
                        Province province = (Province) this.getEntityById(Province.class,Integer.valueOf(bb.getName()));
                        bb.setName(province.getMapName());
                    }else{
                        bb.setName("未知");
                    }
                }
            }
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "人生经历地图统计", e);
        }
    }

    /**
     * 获取出发城市列表
     * @return
     */
    public List<String> getStartCityList(Long userId){
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select distinct start_city as startCity from life_experience_detail led,");
            sb.append("(select life_experience_id as lid,min(occur_date) as occur_date from life_experience_detail group by life_experience_id )  as tt ");
            sb.append("where led.occur_date = tt.occur_date and led.life_experience_id = tt.lid and led.user_id=? ");
            return this.getEntityListSQL(sb.toString(),0,0,userId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取出发城市列表异常", e);
        }
    }

    /**
     * 获取迁徙地图的数据
     * @param sf
     * @return
     */
    public List<TransferMapStat> statTransMap(LifeExperienceMapStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            StringBuffer sb =new StringBuffer();
            sb.append("select start_city as startCity,arrive_city as arriveCity,count(0) as totalCount");
            sb.append(" from life_experience_detail ");
            sb.append(pr.getParameterString());
            sb.append(" and map_stat=1 ");
            sb.append(" group by startCity,arriveCity ");
            List<TransferMapStat> list = this.getEntityListWithClassSQL(sb.toString(),
                    pr.getPage(),pr.getPageSize(),TransferMapStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "人生经历地图统计", e);
        }
    }

    /**
     * 获取地理位置信息
     * @param locationName
     * @return
     */
    public CityLocation getCityLocationByLocation(String locationName){
        try {
            String hql="from CityLocation where location=? ";
            List<CityLocation> list =  this.getEntityListHQL(hql,0,0,locationName);
            if(list.isEmpty()){
                return null;
            }else{
                return list.get(0);
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取地理位置信息异常", e);
        }
    }

    /**
     * 人生经历基于日期的统计
     * @param sf
     * @return
     */
    public List<LifeExperienceDateStat> statDateLifeExperience(LifeExperienceDateStatSearch sf){
        try {
            PageRequest pr = sf.buildQuery();
            DateGroupType dateGroupType = sf.getDateGroupType();
            StringBuffer sb = new StringBuffer();
            sb.append("select indexValue,");
            sb.append("count(0) as totalCount, ");
            sb.append("sum(days) as totalDays ");
            sb.append("from (");
            sb.append("select"+ MysqlUtil.dateTypeMethod("start_date",dateGroupType)+"as indexValue,");
            sb.append("days from life_experience ");
            sb.append(pr.getParameterString());
            sb.append(") tt group by indexValue ");
            sb.append(" order by indexValue");
            List<LifeExperienceDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),LifeExperienceDateStat.class,pr.getParameterValue());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "人生经历基于日期的统计异常", e);
        }
    }


    /**
     * 更新人生经历详情，附带更新人生经历的花费
     * @return
     */
    public void saveOrUpdateLifeExperienceDetail(LifeExperienceDetail bean,boolean updateStat){
        try {
            if(bean.getId()==null){
                saveOrUpdateLifeExperienceBean(bean,false);
            }else{
                saveOrUpdateLifeExperienceBean(bean,true);
            }
            if(updateStat){
                updateLifeExperienceCost(bean.getLifeExperience().getId());
            }
        } catch (PersistentException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "更新人生经历详情异常", e);
        }
    }

    /**
     * 更新人生经历花费
     * @return
     */
    public void updateLifeExperienceCost(Long lifeExperienceId){
        try {
            String sql="update LifeExperience set cost =(select sum(cost) from LifeExperienceDetail where lifeExperience.id =? ) where id=? ";
            this.updateEntitys(sql,lifeExperienceId,lifeExperienceId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "更新人生经历花费异常", e);
        }
    }

    /**
     * 更新人生经历明细花费
     * @return
     */
    public void updateLifeExperienceDetailStat(Long lifeExperienceDetailId){
        try {
            String sql="update LifeExperienceDetail set cost =(select sum(cost) from LifeExperienceConsume where lifeExperienceDetail.id =? ) where id=? ";
            this.updateEntitys(sql,lifeExperienceDetailId,lifeExperienceDetailId);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "更新人生经历明细花费异常", e);
        }
    }

    /**
     * 通过购买消费记录更新人生经历明细花费
     * @return
     */
    public void updateLifeExperienceConsumeByBuyRecord(BuyRecord buyRecord){
        try {
            String hql ="from LifeExperienceConsume where buyRecordId=?";
            List<LifeExperienceConsume> list = this.getEntityListNoPageHQL(hql,buyRecord.getId());
            for(LifeExperienceConsume bean : list){
                bean.setName(buyRecord.getGoodsName());
                bean.setCost(buyRecord.getTotalPrice());
                bean.setLastModifyTime(new Date());
                this.saveOrUpdateLifeExperienceConsume(bean,true);
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "更新人生经历明细花费异常", e);
        }
    }

    /**
     * 更新人生经历消费，附带更新人生经历详情的花费
     * @return
     */
    public void saveOrUpdateLifeExperienceConsume(LifeExperienceConsume bean,boolean updateStat){
        try {
            if(bean.getId()==null){
                saveOrUpdateLifeExperienceBean(bean,false);
            }else{
                saveOrUpdateLifeExperienceBean(bean,true);
            }
            //还需要去更新BuyRecord的关键字
            if(bean.getBuyRecordId()!=null){
                BuyRecord buyRecord = (BuyRecord) this.getEntityById(BuyRecord.class,bean.getBuyRecordId());
                LifeExperienceDetail detail = (LifeExperienceDetail) this.getEntityById(LifeExperienceDetail.class,bean.getLifeExperienceDetail().getId());
                //更新关键字
                String name = detail.getLifeExperience().getName();
                String keywords = buyRecord.getKeywords();
                boolean needUpdate=false;
                if(StringUtil.isEmpty(keywords)){
                    keywords = name;
                    needUpdate =true;
                }else{
                    if(keywords.contains(name)){
                        //已经包含就不更新
                    }else {
                        keywords+=","+name;
                        needUpdate =true;
                    }
                }
                if (needUpdate){
                    buyRecord.setKeywords(keywords);
                    buyRecord.setLastModifyTime(new Date());
                    this.updateEntity(buyRecord);
                }
            }
            if(updateStat){
                LifeExperienceDetail detail = (LifeExperienceDetail) this.getEntityById(LifeExperienceDetail.class,bean.getLifeExperienceDetail().getId());
                //更新明细
                updateLifeExperienceDetailStat(bean.getLifeExperienceDetail().getId());
                //此时detail没有完全加载，需要手动加载
                //更新总的人生经历
                updateLifeExperienceCost(detail.getLifeExperience().getId());
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "更新人生经历消费，附带更新人生经历详情的花费异常", e);
        }
    }


    /**
     * 删除人生经历消费
     * @return
     */
    public void deleteLifeExperienceConsume(Long id,boolean updateStat){
        try {
            LifeExperienceConsume bean = (LifeExperienceConsume) this.getEntityById(LifeExperienceConsume.class,id);
            Long detailId = bean.getLifeExperienceDetail().getId();
            Long lifeExperienceId = bean.getLifeExperienceDetail().getLifeExperience().getId();
            //删除
            deleteLifeExperienceConsume(bean);
            if(updateStat){
                //更新明细
                updateLifeExperienceDetailStat(detailId);
                //更新总的人生经历
                updateLifeExperienceCost(lifeExperienceId);
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "删除人生经历消费异常", e);
        }
    }

    /**
     * 独立事务删除人生经历消费信息
     * @param bean
     */
    public void deleteLifeExperienceConsume(LifeExperienceConsume bean){
        try {
            //删除
            this.removeEntity(bean);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "独立事务删除人生经历消费信息异常", e);
        }
    }

    /**
     * 独立事务删除人生经历消费信息
     * @param bean
     */
    public void deleteLifeExperienceDetail(LifeExperienceDetail bean){
        try {
            //删除消费
            String sql="delete from LifeExperienceConsume where lifeExperienceDetail.id=?";
            this.updateEntitys(sql,bean.getId());
            //删除
            this.removeEntity(bean);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "独立事务删除人生经历消费信息异常", e);
        }
    }

    /**
     * 删除人生经历详情
     * @return
     */
    public void deleteLifeExperienceDetail(Long id,Long userId,boolean updateStat){
        try {
            String hql = "from LifeExperienceDetail where id=? and userId=? ";
            LifeExperienceDetail bean = (LifeExperienceDetail) this.getEntityForOne(hql, new Object[]{id, userId});
            Long lifeExperienceId = bean.getLifeExperience().getId();
            deleteLifeExperienceDetail(bean);

            if(updateStat){
                //更新总的人生经历
                updateLifeExperienceCost(lifeExperienceId);
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "删除人生经历消费异常", e);
        }
    }

    /**
     * 独立事务，因为需要更新数据
     * @param bean
     */
    public void saveOrUpdateLifeExperienceBean(Object bean, boolean update){
        try {
            if(!update){
                this.saveEntity(bean);
            }else{
                this.updateEntity(bean);
            }
            //this.flushSession();
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "独立事务更新人生经历相关数据异常", e);
        }
    }

    /**
     * 删除人生经历
     * @return
     */
    public void deleteLifeExperience(Long id){
        try {
            //删除消费
            String sql="delete from LifeExperienceConsume where lifeExperienceDetail.id in (select id from LifeExperienceDetail where lifeExperience.id=? )";
            this.updateEntitys(sql,id);

            //删除人生经历详情
            String sql2="delete from LifeExperienceDetail where lifeExperience.id=?";
            this.updateEntitys(sql2,id);

            //删除人生经历
            String sql3="delete from LifeExperience where id=?";
            this.updateEntitys(sql3,id);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "删除人生经历异常", e);
        }
    }

    /**
     * 修正人生经历
     * @return
     */
    public void reviseLifeExperience(LifeExperienceRevise revise){
        try {
            if(revise.getReviseCost()!=null&&revise.getReviseCost()){
                this.updateLifeExperienceCost(revise.getId());
            }
            if(revise.getReviseDays()!=null&&revise.getReviseDays()){
                String sql="update life_experience set days =(select count(0) from (select distinct occurDate from life_experience_detail where lifeExperienceId =? ) as aa ) where id=? ";
                this.execSqlUpdate(sql,revise.getId(),revise.getId());
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "修正人生经历异常", e);
        }
    }

}
