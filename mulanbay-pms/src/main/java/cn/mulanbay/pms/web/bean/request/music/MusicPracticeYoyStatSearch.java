package cn.mulanbay.pms.web.bean.request.music;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.persistent.query.Parameter;
import cn.mulanbay.persistent.query.Query;
import cn.mulanbay.pms.web.bean.request.BaseYoyStatSearch;

/**
 * Created by fenghong on 2017/2/5.
 * 口琴练习记录同期比对
 */
public class MusicPracticeYoyStatSearch extends BaseYoyStatSearch implements BindUser {

    private Long userId;

    @Query(fieldName = "musicInstrumentId", op = Parameter.Operator.EQ)
    private Long musicInstrumentId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMusicInstrumentId() {
        return musicInstrumentId;
    }

    public void setMusicInstrumentId(Long musicInstrumentId) {
        this.musicInstrumentId = musicInstrumentId;
    }
}
