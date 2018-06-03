package cn.mulanbay.pms.persistent.domain;

import cn.mulanbay.pms.persistent.enums.MonitorBussType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "error_code_define")
public class ErrorCodeDefine implements Serializable {

    private Integer code;

    private String name;

    private Integer level;

    private Boolean notifiable;

    private Boolean realtimeNotify;

    private String causes;

    private String solutions;

    private MonitorBussType bussType;

    private String remark;
    private Date createdTime;
    private Date lastModifyTime;

    // Property accessors
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "code", unique = true, nullable = false)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "level", nullable = false)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(name = "notifiable", nullable = false)
    public Boolean getNotifiable() {
        return notifiable;
    }

    public void setNotifiable(Boolean notifiable) {
        this.notifiable = notifiable;
    }

    @Column(name = "realtime_notify", nullable = false)
    public Boolean getRealtimeNotify() {
        return realtimeNotify;
    }

    public void setRealtimeNotify(Boolean realtimeNotify) {
        this.realtimeNotify = realtimeNotify;
    }

    @Column(name = "causes")
    public String getCauses() {
        return causes;
    }

    public void setCauses(String causes) {
        this.causes = causes;
    }

    @Column(name = "solutions")
    public String getSolutions() {
        return solutions;
    }

    public void setSolutions(String solutions) {
        this.solutions = solutions;
    }

    @Column(name = "buss_type", nullable = false)
    public MonitorBussType getBussType() {
        return bussType;
    }

    public void setBussType(MonitorBussType bussType) {
        this.bussType = bussType;
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
