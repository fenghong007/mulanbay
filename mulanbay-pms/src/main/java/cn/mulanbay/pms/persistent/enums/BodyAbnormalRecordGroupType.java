package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 最佳类型
 */
public enum BodyAbnormalRecordGroupType {

    ORGAN("organ"),DISEASE("disease"),PAINLEVEL("pain_level"),IMPORTANTLEVEL("important_level"),LASTDAYS("last_days");

    private String field;

    BodyAbnormalRecordGroupType(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}