package cn.mulanbay.pms.web.bean.request.life;

import cn.mulanbay.common.aop.BindUser;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-14 21:32
 */
public class LifeExperienceDetailFormRequest implements BindUser {

    private Long id;
    private Long userId;

    @NotNull(message = "{validate.businessTrip.lifeExperienceId.NotNull}")
    private Long lifeExperienceId;

    @NotEmpty(message = "{validate.lifeExperienceDetail.country.NotEmpty}")
    private String country;

    private Integer provinceId;
    private Integer cityId;
    private Integer districtId;

    //@NotEmpty(message = "{validate.lifeExperienceDetail.province.NotEmpty}")

    //@NotEmpty(message = "{validate.lifeExperienceDetail.city.NotEmpty}")

    //@NotEmpty(message = "{validate.lifeExperienceDetail.location.NotEmpty}")

    @NotNull(message = "{validate.businessTrip.occurDate.NotNull}")
    private Date occurDate;

    @NotNull(message = "{validate.businessTrip.startCity.NotNull}")
    private String startCity;

    @NotNull(message = "{validate.businessTrip.arriveCity.NotNull}")
    private String arriveCity;
    private Double cost;
    //是否加入到地图的绘制
    @NotNull(message = "{validate.businessTrip.mapStat.NotNull}")
    private Boolean mapStat;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLifeExperienceId() {
        return lifeExperienceId;
    }

    public void setLifeExperienceId(Long lifeExperienceId) {
        this.lifeExperienceId = lifeExperienceId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Date getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getArriveCity() {
        return arriveCity;
    }

    public void setArriveCity(String arriveCity) {
        this.arriveCity = arriveCity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Boolean getMapStat() {
        return mapStat;
    }

    public void setMapStat(Boolean mapStat) {
        this.mapStat = mapStat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
