package cn.mulanbay.pms.persistent.domain;

import cn.mulanbay.pms.persistent.enums.MusicPracticeTuneType;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by fenghong on 2017/1/10.
 */
@Entity
@Table(name = "music_practice_tune")
public class MusicPracticeTune implements java.io.Serializable{
    private static final long serialVersionUID = 7184640780429015652L;
    private Long id;
    private Long userId;
    private MusicPractice musicPractice;
    private String tune;
    private Integer times;
    private Level level;
    private MusicPracticeTuneType tuneType;
    private String remark;

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
    @JoinColumn(name = "music_practice_id")
    public MusicPractice getMusicPractice() {
        return musicPractice;
    }

    public void setMusicPractice(MusicPractice musicPractice) {
        this.musicPractice = musicPractice;
    }

    @Basic
    @Column(name = "tune")
    public String getTune() {
        return tune;
    }

    public void setTune(String tune) {
        this.tune = tune;
    }

    @Basic
    @Column(name = "times")
    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @Basic
    @Column(name = "level")
    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Basic
    @Column(name = "tune_type")
    public MusicPracticeTuneType getTuneType() {
        return tuneType;
    }

    public void setTuneType(MusicPracticeTuneType tuneType) {
        this.tuneType = tuneType;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Transient
    public String getLevelName(){
        if(level!=null){
            return level.getName();
        }else{
            return null;
        }
    }

    @Transient
    public String getTuneTypeName(){
        if(tuneType!=null){
            return tuneType.getName();
        }else{
            return null;
        }
    }
    /**
     * 目前enum的映射采用默认的orinal（序列号），因此类中所列的枚举值不能改变顺序
     */
    public enum Level{
        PRACTICE(0,"练习"),
        SKILLED(1,"熟练"),
        RECORD(2,"录音"),
        PERFORMANCE(3,"演奏");
        private int value;
        private String name;

        Level(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static Level getLevel(int ordinal){
            for(Level level : Level.values()){
                if(level.ordinal()==ordinal){
                    return level;
                }
            }
            return null;
        }
    }
}
