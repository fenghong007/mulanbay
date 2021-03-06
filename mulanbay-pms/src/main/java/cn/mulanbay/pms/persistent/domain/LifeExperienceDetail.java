package cn.mulanbay.pms.persistent.domain;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by fenghong on 2017/1/10.
 * 人生经历每天的明细
 */
@Entity
@Table(name = "life_experience_detail")
public class LifeExperienceDetail implements java.io.Serializable {
    private static final long serialVersionUID = 1618796530128502244L;
    private Long id;
    private Long userId;
    private LifeExperience lifeExperience;
    private String country;
    private Integer provinceId;
    private Integer cityId;
    private Integer districtId;
    private Date occurDate;
    private String startCity;
    private String arriveCity;
    private Double cost;
    //是否加入到地图的绘制
    private Boolean mapStat;
    private String remark;
    private Date createdTime;
    private Date lastModifyTime;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "life_experience_id", nullable = true)
    public LifeExperience getLifeExperience() {
        return lifeExperience;
    }

    public void setLifeExperience(LifeExperience lifeExperience) {
        this.lifeExperience = lifeExperience;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "province_id")
    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    @Basic
    @Column(name = "city_id")
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @Basic
    @Column(name = "district_id")
    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "occur_date", length = 10)
    public Date getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }

    @Basic
    @Column(name = "start_city")
    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    @Basic
    @Column(name = "arrive_city")
    public String getArriveCity() {
        return arriveCity;
    }

    public void setArriveCity(String arriveCity) {
        this.arriveCity = arriveCity;
    }

    @Basic
    @Column(name = "cost")
    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "map_stat")
    public Boolean getMapStat() {
        return mapStat;
    }

    public void setMapStat(Boolean mapStat) {
        this.mapStat = mapStat;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "created_time")
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "last_modify_time")
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

}
