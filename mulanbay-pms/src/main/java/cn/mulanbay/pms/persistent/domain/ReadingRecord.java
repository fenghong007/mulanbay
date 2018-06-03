package cn.mulanbay.pms.persistent.domain;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by fenghong on 2017/1/10.
 */
@Entity
@Table(name = "reading_record")
public class ReadingRecord implements java.io.Serializable {
    private static final long serialVersionUID = -6674046076514845193L;
    private Long id;
    private Long userId;
    private BookCategory bookCategory;
    private String bookName;
    private String author;
    private String isbn;
    //初步日期
    private Integer publishedYear;
    //出版社
    private String press;
    //国家
    private String nation;
    private BookType bookType;
    private Language language;
    // 重要等级(0-5)
    private Double importantLevel;
    private Date proposedDate;
    private Date beginDate;
    private Date finishedDate;
    //保存日期：如购入、借入
    private Date storeDate;
    private ReadingStatus status;
    //读完花费时间
    private Integer costDays;
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
    @JoinColumn(name = "book_category_id")
    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    @Basic
    @Column(name = "book_name")
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Basic
    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Basic
    @Column(name = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "published_year")
    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    @Basic
    @Column(name = "press")
    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    @Basic
    @Column(name = "nation")
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    @Basic
    @Column(name = "book_type")
    public BookType getBookType() {
        return bookType;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }

    @Basic
    @Column(name = "language")
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Basic
    @Column(name = "important_level")
    public Double getImportantLevel() {
        return importantLevel;
    }

    public void setImportantLevel(Double importantLevel) {
        this.importantLevel = importantLevel;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "proposed_date")
    public Date getProposedDate() {
        return proposedDate;
    }

    public void setProposedDate(Date proposedDate) {
        this.proposedDate = proposedDate;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "finished_date")
    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "store_date")
    public Date getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(Date storeDate) {
        this.storeDate = storeDate;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "begin_date")
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @Basic
    @Column(name = "status")
    public ReadingStatus getStatus() {
        return status;
    }

    public void setStatus(ReadingStatus status) {
        this.status = status;
    }

    @Basic
    @Column(name = "cost_days")
    public Integer getCostDays() {
        return costDays;
    }

    public void setCostDays(Integer costDays) {
        this.costDays = costDays;
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

    @Transient
    public String getStatusName(){
        if(this.status!=null){
            return this.status.getName();
        }else{
            return null;
        }
    }

    @Transient
    public String getLanguageName(){
        if(this.language!=null){
            return this.language.getName();
        }else{
            return null;
        }
    }

    @Transient
    public String getBookTypeName(){
        if(this.bookType!=null){
            return this.bookType.getName();
        }else{
            return null;
        }
    }

    public enum Language{
        CHINESE(0,"中文"),
        ENGLISH(1,"英文");
        private int value;
        private String name;

        Language(int value, String name) {
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

        public static Language getLanguage(int ordinal){
            for(Language language : Language.values()){
                if(language.ordinal()==ordinal){
                    return language;
                }
            }
            return null;
        }
    }
    /**
     * 目前enum的映射采用默认的orinal（序列号），因此类中所列的枚举值不能改变顺序
     */
    public enum BookType{
        PAPER(0,"纸质书"),
        EBOOK(1,"电子书");
        private int value;
        private String name;

        BookType(int value, String name) {
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

        public static BookType getBookType(int ordinal){
            for(BookType bookType : BookType.values()){
                if(bookType.ordinal()==ordinal){
                    return bookType;
                }
            }
            return null;
        }
    }

    /**
     * 目前enum的映射采用默认的orinal（序列号），因此类中所列的枚举值不能改变顺序
     */
    public enum ReadingStatus{
        READING(0,"正在读"),
        UNREAD(1,"未读"),
        READED(2,"已读"),
        GIVEUP(3,"放弃");
        private int value;
        private String name;

        ReadingStatus(int value, String name) {
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

        public static ReadingStatus getReadingStatus(int ordinal){
            for(ReadingStatus readingStatus : ReadingStatus.values()){
                if(readingStatus.ordinal()==ordinal){
                    return readingStatus;
                }
            }
            return null;
        }
    }
}
