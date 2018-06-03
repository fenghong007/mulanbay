package cn.mulanbay.pms.persistent.domain;

import cn.mulanbay.pms.persistent.enums.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by fenghong on 2017/1/10.
 */
@Entity
@Table(name = "system_function")
public class SystemFunction implements java.io.Serializable{

    private static final long serialVersionUID = 529618585154626058L;
    private Long id;
    private String name;
    private String supportMethods;
    private String urlAddress;
    private UrlType urlType;
    private FunctionType functionType;
    private FunctionDataType functionDataType;
    private Long groupId;
    private String imageName;
    private SystemFunction parent;
    private CommonStatus status;
    //针对什么类,查询使用
    private String beanName;
    //主键的域,查询使用
    private String idField;
    private IdFieldType idFieldType;
    private Integer orderIndex;
    private Boolean doLog;
    //是否计入触发统计（因为有些是筛选条件，这些并不需要统计）
    private Boolean triggerStat;
    //区分用户,有些公共的功能点不需要区分用户
    private Boolean diffUser;
    //是否登录验证
    private Boolean loginAuth;
    //是否授权认证
    private Boolean permissionAuth;
    //是否IP认证
    private Boolean ipAuth;
    //是否自动登陆
    private Boolean autoLogin;
    //是否请求限制
    private Boolean requestLimit;
    //限制周期
    private Integer requestLimitPeriod;
    //是否每天限制操作数(大于0说明要限制)
    private Integer dayLimit;
    //记录返回数据
    private Boolean recordReturnData;
    //奖励积分(正为加，负为减)
    private Integer rewardPoint;
    //错误代码定义，方便日志监控
    private Integer errorCode;
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

    @Column(name = "name", nullable = false, length = 32)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "support_methods", nullable = false, length = 64)
    public String getSupportMethods() {
        return supportMethods;
    }

    public void setSupportMethods(String supportMethods) {
        this.supportMethods = supportMethods;
    }

    @Column(name = "url_address", nullable = false, length = 100)
    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    @Column(name = "url_type", nullable = false)
    public UrlType getUrlType() {
        return urlType;
    }

    public void setUrlType(UrlType urlType) {
        this.urlType = urlType;
    }

    @Column(name = "function_type", nullable = false)
    public FunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
    }

    @Column(name = "function_data_type")
    public FunctionDataType getFunctionDataType() {
        return functionDataType;
    }

    public void setFunctionDataType(FunctionDataType functionDataType) {
        this.functionDataType = functionDataType;
    }

    @Column(name = "group_id")
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Column(name = "image_name")
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", nullable = true)
    public SystemFunction getParent() {
        return parent;
    }

    public void setParent(SystemFunction parent) {
        this.parent = parent;
    }

    @Column(name = "status")
    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    @Column(name = "bean_name")
    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Column(name = "id_field")
    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    @Column(name = "id_field_type")
    public IdFieldType getIdFieldType() {
        return idFieldType;
    }

    public void setIdFieldType(IdFieldType idFieldType) {
        this.idFieldType = idFieldType;
    }

    @Column(name = "order_index")
    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Column(name = "do_log")
    public Boolean getDoLog() {
        return doLog;
    }

    public void setDoLog(Boolean doLog) {
        this.doLog = doLog;
    }

    @Column(name = "trigger_stat")
    public Boolean getTriggerStat() {
        return triggerStat;
    }

    public void setTriggerStat(Boolean triggerStat) {
        this.triggerStat = triggerStat;
    }

    @Column(name = "diff_user")
    public Boolean getDiffUser() {
        return diffUser;
    }

    public void setDiffUser(Boolean diffUser) {
        this.diffUser = diffUser;
    }

    @Column(name = "login_auth")
    public Boolean getLoginAuth() {
        return loginAuth;
    }

    public void setLoginAuth(Boolean loginAuth) {
        this.loginAuth = loginAuth;
    }

    @Column(name = "permission_auth")
    public Boolean getPermissionAuth() {
        return permissionAuth;
    }

    public void setPermissionAuth(Boolean permissionAuth) {
        this.permissionAuth = permissionAuth;
    }

    @Column(name = "ip_auth")
    public Boolean getIpAuth() {
        return ipAuth;
    }

    public void setIpAuth(Boolean ipAuth) {
        this.ipAuth = ipAuth;
    }

    @Column(name = "auto_login")
    public Boolean getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(Boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    @Column(name = "request_limit")
    public Boolean getRequestLimit() {
        return requestLimit;
    }

    public void setRequestLimit(Boolean requestLimit) {
        this.requestLimit = requestLimit;
    }

    @Column(name = "request_limit_period")
    public Integer getRequestLimitPeriod() {
        return requestLimitPeriod;
    }

    public void setRequestLimitPeriod(Integer requestLimitPeriod) {
        this.requestLimitPeriod = requestLimitPeriod;
    }

    @Column(name = "day_limit")
    public Integer getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }

    @Column(name = "record_return_data")
    public Boolean getRecordReturnData() {
        return recordReturnData;
    }

    public void setRecordReturnData(Boolean recordReturnData) {
        this.recordReturnData = recordReturnData;
    }

    @Column(name = "reward_point")
    public Integer getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(Integer rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    @Column(name = "error_code")
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "created_time", length = 19)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "last_modify_time", length = 19)
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Transient
    public String getUrlTypeName(){
        if(urlType!=null){
            return urlType.getName();
        }else{
            return null;
        }
    }

    @Transient
    public String getFunctionTypeName(){
        if(functionType!=null){
            return functionType.getName();
        }else{
            return null;
        }
    }

    @Transient
    public String getFunctionDataTypeName(){
        if(functionDataType!=null){
            return functionDataType.getName();
        }else{
            return null;
        }
    }

    @Transient
    public String getStatusName(){
        if(status!=null){
            return status.getName();
        }else{
            return null;
        }
    }

    @Transient
    public Long getParentId(){
        if(parent!=null){
            return parent.getId();
        }else{
            return null;
        }
    }


}
