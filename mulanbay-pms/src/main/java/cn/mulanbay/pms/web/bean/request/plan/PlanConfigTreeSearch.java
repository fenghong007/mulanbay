package cn.mulanbay.pms.web.bean.request.plan;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.pms.web.bean.request.CommonTreeSearch;

/**
 * Created by fenghong on 2017/8/31.
 * 通用的树查询
 */
public class PlanConfigTreeSearch extends CommonTreeSearch implements BindUser {

    private Integer level;

    private String relatedBeans;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRelatedBeans() {
        return relatedBeans;
    }

    public void setRelatedBeans(String relatedBeans) {
        this.relatedBeans = relatedBeans;
    }
}
