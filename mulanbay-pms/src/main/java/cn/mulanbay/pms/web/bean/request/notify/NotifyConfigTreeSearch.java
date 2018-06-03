package cn.mulanbay.pms.web.bean.request.notify;

import cn.mulanbay.pms.web.bean.request.CommonTreeSearch;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-20 15:29
 */
public class NotifyConfigTreeSearch extends CommonTreeSearch {

    private String relatedBeans;

    public String getRelatedBeans() {
        return relatedBeans;
    }

    public void setRelatedBeans(String relatedBeans) {
        this.relatedBeans = relatedBeans;
    }

}
