package cn.mulanbay.pms.web.bean.request.life;

/**
 * Created by fenghong on 2017/9/14.
 */
public class LifeExperienceRevise {

    private Long id;

    private Boolean reviseCost;

    private Boolean reviseDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getReviseCost() {
        return reviseCost;
    }

    public void setReviseCost(Boolean reviseCost) {
        this.reviseCost = reviseCost;
    }

    public Boolean getReviseDays() {
        return reviseDays;
    }

    public void setReviseDays(Boolean reviseDays) {
        this.reviseDays = reviseDays;
    }
}
