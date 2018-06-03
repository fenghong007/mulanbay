package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.pms.persistent.bean.StatValueConfigBean;
import cn.mulanbay.pms.persistent.bean.StatValueConfigDetail;
import cn.mulanbay.pms.persistent.domain.StatValueConfig;
import cn.mulanbay.pms.persistent.enums.StatValueType;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatValueConfigService extends BaseHibernateDao {

    /**
     * 获取统计类的配置值列表
     * @param fid 报表类型id或者提醒配置id或者计划配置id
     * @param type 类型
     * @return
     */
    public List<StatValueConfigBean> getStatValueConfig(Long fid, StatValueType type,Long userId){
        try {
            String hql="from StatValueConfig where fid=? and type=? order by orderIndex";
            List<StatValueConfig> configs = this.getEntityListNoPageHQL(hql,fid,type);
            if(configs.isEmpty()){
                return new ArrayList<>();
            }else{
                List<StatValueConfigBean> list = new ArrayList<>();
                for(StatValueConfig svc : configs){
                    StatValueConfigBean scb=getStatValueConfigBean(svc,null,userId);
                    list.add(scb);
                }
                return list;
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取统计类的配置值列表异常", e);
        }

    }

    /**
     * 获取单个
     * @param svc
     * @return
     */
    public StatValueConfigBean getStatValueConfigBean(StatValueConfig svc,String pid,Long userId){
        try {
            StatValueConfigBean scb=new StatValueConfigBean();
            scb.setName(svc.getName());
            scb.setPromptMsg(svc.getPromptMsg());
            scb.setCasCadeType(svc.getCasCadeType());
            String sql = svc.getSqlContent();
            if(!StringUtil.isEmpty(pid)){
                sql = MessageFormat.format(sql,pid);
            }
            if(!StringUtil.isEmpty(svc.getUserField())){
                sql = sql.replaceAll("\\{"+svc.getUserField()+"\\}",userId.toString());
            }
            List<Object[]> vcs = this.getEntityListNoPageSQL(sql);
            for(Object[] o: vcs){
                StatValueConfigDetail detail = new StatValueConfigDetail();
                detail.setId(o[0].toString());
                detail.setText(o[1].toString());
                scb.addStatValueConfigDetail(detail);
            }
            return scb;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取统计类的配置值列表异常", e);
        }
    }


}
