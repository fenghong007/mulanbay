package cn.mulanbay.pms.web.bean.request.work;

import cn.mulanbay.web.bean.request.PageSearch;

/**
 * Created by fenghong on 2017/1/30.
 */
public class SportExerciseMaxStatRefresh extends PageSearch {

    private Integer sportTypeId;

    private boolean reInit;

    public Integer getSportTypeId() {
        return sportTypeId;
    }

    public void setSportTypeId(Integer sportTypeId) {
        this.sportTypeId = sportTypeId;
    }

    public boolean isReInit() {
        return reInit;
    }

    public void setReInit(boolean reInit) {
        this.reInit = reInit;
    }
}
