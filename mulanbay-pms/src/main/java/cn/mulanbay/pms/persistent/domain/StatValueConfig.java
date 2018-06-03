package cn.mulanbay.pms.persistent.domain;


import cn.mulanbay.pms.persistent.enums.CasCadeType;
import cn.mulanbay.pms.persistent.enums.StatValueClass;
import cn.mulanbay.pms.persistent.enums.StatValueType;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 所有报表、计划、提醒类型的sql中值的选择在这里配置
 */
@Entity
@Table(name = "stat_value_config")
public class StatValueConfig  implements java.io.Serializable{

    private static final long serialVersionUID = 882330393814155329L;
    private Long id;

    private String name;

    private StatValueType type;

    private StatValueClass valueClass;

    private Long fid;

    private String sqlContent;

    //是否记录由上层来决定
    private CasCadeType casCadeType;

    //是否和用户绑定，空表示不绑定
    private String userField;

    private Integer orderIndex;

    //提示信息
    private String promptMsg;

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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "type")
    public StatValueType getType() {
        return type;
    }

    public void setType(StatValueType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "value_class")
    public StatValueClass getValueClass() {
        return valueClass;
    }

    public void setValueClass(StatValueClass valueClass) {
        this.valueClass = valueClass;
    }

    @Basic
    @Column(name = "fid")
    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    @Basic
    @Column(name = "sql_content")
    public String getSqlContent() {
        return sqlContent;
    }

    public void setSqlContent(String sqlContent) {
        this.sqlContent = sqlContent;
    }

    @Basic
    @Column(name = "cas_cade_type")
    public CasCadeType getCasCadeType() {
        return casCadeType;
    }

    public void setCasCadeType(CasCadeType casCadeType) {
        this.casCadeType = casCadeType;
    }

    @Basic
    @Column(name = "user_field")
    public String getUserField() {
        return userField;
    }

    public void setUserField(String userField) {
        this.userField = userField;
    }

    @Basic
    @Column(name = "order_index")
    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Basic
    @Column(name = "prompt_msg")
    public String getPromptMsg() {
        return promptMsg;
    }

    public void setPromptMsg(String promptMsg) {
        this.promptMsg = promptMsg;
    }

    @Transient
    public String getTypeName(){
        if(type!=null){
            return type.getName();
        }else {
            return null;
        }
    }

    @Transient
    public String getCasCadeTypeName(){
        if(casCadeType!=null){
            return casCadeType.getName();
        }else {
            return null;
        }
    }
}
