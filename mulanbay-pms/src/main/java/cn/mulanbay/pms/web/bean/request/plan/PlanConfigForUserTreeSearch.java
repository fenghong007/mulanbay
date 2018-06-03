package cn.mulanbay.pms.web.bean.request.plan;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.common.aop.BindUserLevel;

/**
 * Created by fenghong on 2017/8/31.
 * 需要绑定用户级别
 * 通用的树查询
 */
public class PlanConfigForUserTreeSearch extends PlanConfigTreeSearch implements BindUser,BindUserLevel {

    private Integer level;

    public Integer getLevel() {
        return level;
    }

    @Override
    public void setLevel(Integer level) {
        this.level = level;
    }

}
