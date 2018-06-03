package cn.mulanbay.pms.web.bean.request.userbehavior;

import cn.mulanbay.common.aop.BindUserLevel;
import cn.mulanbay.pms.web.bean.request.CommonTreeSearch;

/**
 * Created by fenghong on 2017/8/31.
 * 通用的树查询
 */
public class UserBehaviorConfigToUserTreeSearch extends CommonTreeSearch implements BindUserLevel{

    private Integer level;

    public Integer getLevel() {
        return level;
    }

    @Override
    public void setLevel(Integer level) {
        this.level = level;
    }
}
