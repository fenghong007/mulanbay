package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.pms.persistent.domain.DatabaseClean;
import cn.mulanbay.pms.persistent.enums.CleanType;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DatabaseCleanService extends BaseHibernateDao{

    /**
     * 获取有效的数据库清理列表
     * @return
     */
    public List<DatabaseClean> getActiveDatabaseClean(){
        try {
            String hql="from DatabaseClean where status=? order by orderIndex";
            return this.getEntityListNoPageHQL(hql, CommonStatus.ENABLE);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
                    "获取有效的数据库清理列表异常", e);
        }
    }

    /**
     * 执行数据库清理
     * @param dc
     */
    public void updateAndExecDatabaseClean(DatabaseClean dc){
        try {
            String sql=null;
            int n=0;
            if(dc.getCleanType()== CleanType.TRUNCATE){
                sql="truncate table "+dc.getTableName();
                n =this.execSqlUpdate(sql);
            }else{
                Date date = DateUtil.getDate(0-dc.getDays());
                sql="delete from "+dc.getTableName()+" where "+dc.getDateField()+"<=? ";
                if(!StringUtil.isEmpty(dc.getExtraCondition())){
                    sql+="and "+dc.getExtraCondition();
                }
                n = this.execSqlUpdate(sql,date);
            }
            //更新
            dc.setLastCleanCounts(n);
            dc.setLastCleanTime(new Date());
            this.updateEntity(dc);
        } catch (BaseException e) {
            throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,
                    "执行数据库清理异常", e);
        }
    }

}
