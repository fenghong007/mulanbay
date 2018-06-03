package cn.mulanbay.pms.persistent.util;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.persistent.enums.DateGroupType;

import java.text.MessageFormat;

/**
 * Created by fenghong on 2017/6/28.
 */
public class MysqlUtil {

    /**
     * 时间函数
     * @param field
     * @param dateGroupType
     * @return
     */
    public static String dateTypeMethod(String field,DateGroupType dateGroupType){
        String method=null;
        if(dateGroupType==DateGroupType.HOUR){
            method = "hour";
        }else if(dateGroupType==DateGroupType.DAYOFMONTH){
            method = "DAYOFMONTH";
            //统计都是哪些小时点练习的
        }else if(dateGroupType==DateGroupType.DAYOFWEEK){
            //星期索引，周一=1，周二=2，周日=7
            return " WEEKDAY("+field+")+1 ";
        }else if(dateGroupType==DateGroupType.WEEK){
            method = "weekofyear";
        }else if(dateGroupType==DateGroupType.MONTH){
            method = "month";
        }else if(dateGroupType==DateGroupType.YEAR){
            return " CAST(DATE_FORMAT("+field+",'%Y') AS signed) ";
        }else if(dateGroupType==DateGroupType.DAY||dateGroupType==DateGroupType.DAYCALENDAR){
            return " CAST(DATE_FORMAT("+field+",'%Y%m%d') AS signed) ";
        }else if(dateGroupType==DateGroupType.YEARMONTH){
            return " CAST(DATE_FORMAT("+field+",'%Y%m') AS signed) ";
        } else if(dateGroupType==DateGroupType.HOURMINUTE){
            return "(CAST(DATE_FORMAT("+field+",'%H') AS signed)+CAST(DATE_FORMAT("+field+",'%i') AS signed)/100)";
        }else{
            throw new ApplicationException(PmsErrorCode.UN_SUPPORT_DATE_GROUP_TYPE);
        }
        return " "+method+"("+field+") ";
    }

    /**
     * 替换调绑定的值
     * 如果sqlContent有单引号，MessageFormat.format会把单引号删除导致sql异常
     * 解决方法：sqlContent的单引号用两个单引号代替
     * 比如：原来是select DATE_FORMAT(startDate,'%Y-%m-%d') as date...修改为select DATE_FORMAT(startDate,''%Y-%m-%d'') as date ...
     * @link http://blog.csdn.net/a258831020/article/details/46820855
     * @param sqlContent
     * @param bindValues
     * @return
     */
    public static String replaceBindValues(String sqlContent,String bindValues){
        if(!StringUtil.isEmpty(bindValues)){
            //替换用户选择的值
            String[] ss =bindValues.split(",");
            sqlContent = MessageFormat.format(sqlContent,ss);
        }
        return sqlContent;
    }
}
