package cn.mulanbay.pms.thread;

import cn.mulanbay.business.handler.lock.RedisDistributedLock;
import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.pms.handler.CacheHandler;
import cn.mulanbay.pms.persistent.enums.RewardSource;
import cn.mulanbay.pms.persistent.service.AuthService;
import org.apache.log4j.Logger;

public class RewardPointsThread extends Thread {

    private static Logger logger = Logger.getLogger(RewardPointsThread.class);

    private Long userId;

    private int rewards;

    private Long sourceId;

    private RewardSource rewardSource;

    private String remark;

    private Long messageId;

    public RewardPointsThread(Long userId,int rewards,Long sourceId,RewardSource rewardSource,String remark,Long messageId){
        this.userId=userId;
        this.rewards=rewards;
        this.sourceId=sourceId;
        this.rewardSource=rewardSource;
        this.remark=remark;
        this.messageId=messageId;
    }

    @Override
    public void run() {
        String key="pms:distributeLock:updateUserPoint:"+userId;
        RedisDistributedLock redisDistributedLock = BeanFactoryUtil.getBean(RedisDistributedLock.class);
        try {
            redisDistributedLock.lock(key,5000L,3,20);
            // 获取当前的积分
            CacheHandler cacheHandler =BeanFactoryUtil.getBean(CacheHandler.class);
            AuthService authService = BeanFactoryUtil.getBean(AuthService.class);
            Integer points = cacheHandler.get("userPoint:"+userId,Integer.class);
            if(points==null){
                points = authService.getUserPiont(userId);
            }
            points=points+rewards;
            authService.updateUserPoint(userId,points,rewards,sourceId,rewardSource,remark,messageId);
            cacheHandler.set("userPoint:"+userId,points,24*3600);
        } catch (Exception e) {
            logger.error("更新用户ID="+userId+"积分异常",e);
        }finally {
            redisDistributedLock.releaseLock(key);
        }
    }
}
