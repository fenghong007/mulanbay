package cn.mulanbay.pms.web.bean.request.life;

import cn.mulanbay.pms.persistent.enums.ExperienceType;
import cn.mulanbay.pms.web.bean.request.BaseYoyStatSearch;

import java.util.List;

/**
 * Created by fenghong on 2017/2/5.
 * 人生经历同期比对
 */
public class LifeExperienceYoyStatSearch extends BaseYoyStatSearch {

    private Long userId;

    private List<ExperienceType> types;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ExperienceType> getTypes() {
        return types;
    }

    public void setTypes(List<ExperienceType> types) {
        this.types = types;
    }
}
