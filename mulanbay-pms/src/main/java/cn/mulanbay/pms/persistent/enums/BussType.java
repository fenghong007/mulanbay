package cn.mulanbay.pms.persistent.enums;

/**
 * Created by fenghong on 2016/12/30.
 * 业务类型
 */
public enum BussType {

    BUY_RECORD(0,"BuyRecord","消费记录")
    ,DREAM(1,"Dream","梦想")
    ,LIFE_EXPERIENCE(2,"LifeExperience","人生经历")
    ,MUSIC_PRACTICE(3,"MusicPractice","音乐练习")
    ,READING_RECORD(4,"ReadingRecord","阅读")
    ,SPORT_EXERCISE(5,"SportExercise","锻炼")
    ,TREAT_RECORD(6,"TreatRecord","看病记录")
    ,COMMON_RECORD(7,"CommonRecord","通用记录")
    ,DIARY(8,"Diary","日记")
    ,WORK_OVER_TIME(9,"WorkOvertime","加班记录")
    ,SLEEP(10,"Sleep","睡眠")
    ,DIET(11,"Diet","饮食");

    private int value;

    private String beanName;

    private String name;

    BussType(int value, String beanName, String name) {
        this.value = value;
        this.beanName = beanName;
        this.name = name;
    }

    BussType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static BussType getBussType(String beanName){
        for(BussType bt : BussType.values()){
            if(bt.beanName.equals(beanName)){
                return bt;
            }
        }
        return null;
    }
}