package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.pms.persistent.domain.OperationLog;
import cn.mulanbay.pms.persistent.domain.SystemFunction;
import cn.mulanbay.pms.persistent.enums.FunctionType;
import cn.mulanbay.pms.persistent.enums.LogCompareType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghong on 2017/2/8.
 */
@Service
@Transactional
public class LogService extends BaseHibernateDao {

    private static Logger logger = Logger.getLogger(LogService.class);

    /**
     * 获取最近的操作日志比较(根据比较主体)
     *
     * @param target
     * @param compareType
     * @return
     */
    public OperationLog getNearestCompareLog(OperationLog target,LogCompareType compareType) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("from OperationLog where systemFunction.id=? ");
            sb.append("and id!=? ");
            sb.append("and idValue=? ");
            if(compareType==LogCompareType.EARLY){
                sb.append("and occurEndTime<=? order by occurEndTime desc");
            }else {
                sb.append("and occurEndTime>=? order by occurEndTime asc");
            }
            OperationLog log = (OperationLog) this.getEntityForOne(sb.toString(),target.getSystemFunction().getId(),target.getId(),target.getIdValue(),target.getOccurEndTime());
            return log;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取最近的操作日志比较异常", e);
        }
    }

    /**
     * 重新设置操作日志的功能点
     *
     * @param needReSet 如果true，说明以前已经有的也要重新设置
     * @return
     */
    public void setOperationLogFunctionId(boolean needReSet) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("update operation_log t set system_function_id= ");
            sb.append("(select id from system_function s where s.url_address=t.url_address and s.support_methods = t.method limit 1) ");
            if(!needReSet){
                sb.append("where t.system_function_id is null ");
            }
            this.execSqlUpdate(sb.toString());
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "重新设置操作日志的功能点异常", e);
        }
    }

    /**
     * 获取未配置的url
     *
     * @return
     */
    public List<Object[]> getUnConfigedUrlsForOperationLog() {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("select distinct ol.url_address,ol.method ");
            sb.append("from operation_log ol  ");
            sb.append("where ol.url_address not in (select url_address from system_function) ");
            sb.append("order by ol.url_address ");
            List<Object[]> list = this.getEntityListNoPageSQL(sb.toString());
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取未配置的url异常", e);
        }
    }

    /**
     * 获取idValue为空的数据
     *
     * @return
     */
    public List<OperationLog> getOperationLogByIdValueWithNull() {
        try {
            String hql ="from OperationLog where idValue is null ";
            List list = this.getEntityListNoPageHQL(hql);
            return list;
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取idValue为空的数据异常", e);
        }
    }

    /**
     * 重新设置操作日志的功能点
     *
     * @param log
     * @return
     */
    public void setIdValue(OperationLog log) {
        try {
            if (log.getSystemFunction()==null){
                logger.warn("操作日志id="+log.getId()+"还没有关联功能点");
                return;
            }
            Map map = (Map) JsonUtil.jsonToBean(log.getParas(),Map.class);
            if(map==null||map.isEmpty()){
                logger.warn("操作日志id="+log.getId()+"无任何请求参数");
                return;
            }
            String idValue = (String) map.get(log.getSystemFunction().getIdField());
            if(!StringUtil.isEmpty(idValue)){
                String hql="update OperationLog set idValue=? where id=? ";
                this.updateEntitys(hql,idValue,log.getId());
            }
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "重新设置操作日志的功能点异常", e);
        }
    }

    /**
     * 获取修改类的系统功能点，每个业务类只有一个
     *
     * @param beanName
     * @return
     */
    public SystemFunction getEditSystemFunction(String beanName) {
        try {
            String hql="from SystemFunction where beanName=? and functionType=? ";
            return (SystemFunction) this.getEntityForOne(hql,beanName,FunctionType.EDIT);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,
                    "获取修改类的系统功能点异常", e);
        }
    }

    /**
     * 获取最近一次的修改记录
     *
     * @param idValue
     * @param systemfunctionId
     * @return
     */
    public OperationLog getLatestOperationLog(String idValue,Long systemfunctionId) {
        try {
            String hql="from OperationLog where systemFunction.id=? and idValue=? and occurEndTime=(";
            hql+="select max(occurEndTime) from OperationLog where systemFunction.id=? and idValue=? )";
            return (OperationLog) this.getEntityForOne(hql,systemfunctionId,idValue,systemfunctionId,idValue);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,
                    "获取最近一次的修改记录异常", e);
        }
    }


}
