package cn.mulanbay.pms.web.bean.request;

import cn.mulanbay.pms.persistent.enums.DateGroupType;

import java.util.Date;

/**
 * Created by fenghong on 2017/2/11.
 * 时间统计类的基类
 */
public interface DateStatSearch {

    public Date getStartDate();

    public Date getEndDate();

    public DateGroupType getDateGroupType();

    public Boolean isCompliteDate();

}
