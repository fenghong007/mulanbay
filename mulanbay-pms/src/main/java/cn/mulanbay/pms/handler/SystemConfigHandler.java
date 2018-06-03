package cn.mulanbay.pms.handler;

import cn.mulanbay.business.handler.BaseHandler;
import cn.mulanbay.business.handler.HandlerInfo;
import cn.mulanbay.common.util.IPAddressUtil;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.persistent.domain.ErrorCodeDefine;
import cn.mulanbay.pms.persistent.domain.SystemFunction;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
@Component
public class SystemConfigHandler extends BaseHandler {

    private static Logger logger = Logger.getLogger(SystemConfigHandler.class);

    private String hostIpAddress;

    /**
     * 是否采用内存缓存
     */
    @Value("${system.configCache.byMemoryCache}")
    boolean byMemoryCache=false;

    @Autowired
    BaseService baseService;

    @Autowired
    CacheHandler cacheHandler;

    // key:urlAddress_supportMethod,例如：/buyRecord/edit_POST
    private static Map<String,SystemFunction> functionMap = new HashMap<>();

    /**
     * 角色功能点缓存
     */
    private static Map<String,String> roleFunctionMap = new HashMap<>();

    public SystemConfigHandler() {
        super("系统配置");
    }

    /**
     * todo 暂时不支持restfull
     */
    public void reloadFunctions(){
        //获取所有的功能点
        List<SystemFunction> list = baseService.getBeanList(SystemFunction.class,0,0,null);
        functionMap.clear();
        int urlMapSize=0;
        //封装
        for(SystemFunction sf :list){
            if(sf.getUrlAddress()==null||sf.getSupportMethods()==null){
                continue;
            }else{
                String methods = sf.getSupportMethods();
                String[] ss =methods.split(",");
                for(String s : ss){
                    // 数据库中功能点路径不需要设置项目名，因为项目名称在实际过程中会被修改过
                    functionMap.put(sf.getUrlAddress()+"_"+s,sf);
                    urlMapSize++;
                }
            }
        }
        if(!byMemoryCache) {
            cacheHandler.delete("system_function");
            cacheHandler.setHash("system_function",functionMap,0);
        }
        logger.debug("初始化了"+urlMapSize+"条功能点记录");
    }

    @Override
    public void init() {
        super.init();
        //初始化
        hostIpAddress = IPAddressUtil.getLocalIpAddress();
        reloadFunctions();
    }

    @Override
    public void reload() {
        super.reload();
        reloadFunctions();
    }

    /**
     * 通过URL查询
     * @param url
     * @param method
     * @return
     */
    public SystemFunction getFunction(String url, String method){
        String fkey=url+"_"+method.toUpperCase();
        if(byMemoryCache){
            return functionMap.get(fkey);
        }else{
            return cacheHandler.getHash("system_function",fkey,SystemFunction.class);
        }
    }

    /**
     * 角色是否授权
     * @param functionId
     * @param roleId
     * @return
     */
    public boolean isRoleAuth(Long roleId,Long functionId){
        String rfKey = generateRoleFunctionKey(roleId,functionId);
        String s = null;
        if(byMemoryCache){
            s = roleFunctionMap.get(rfKey);
        }else{
            s = cacheHandler.getHash("role_function",rfKey,String.class);
        }
        boolean b = (s==null ? false : true);
        logger.debug("角色是否授权,key:"+rfKey+",auth:"+b);
        return b;
    }

    private String generateRoleFunctionKey(Long roleId,Long functionId){
        return roleId.toString()+"_"+functionId.toString();
    }

    public String getHostIpAddress() {
        return hostIpAddress;
    }

    public BaseService getBaseService() {
        return baseService;
    }

    @Override
    public HandlerInfo getHanderInfo() {
        HandlerInfo hi = super.getHanderInfo();
        hi.addDetail("serverIp",hostIpAddress);
        hi.addDetail("byMemoryCache",String.valueOf(byMemoryCache));
        hi.addDetail("functionMap size",String.valueOf(functionMap.size()));
        hi.addDetail("roleFunctionMap size",String.valueOf(roleFunctionMap.size()));
        return hi;
    }

    /**
     * 获取错误代码定义
     *
     * @param code
     * @return
     */
    public ErrorCodeDefine getErrorCodeDefine(int code){
        return baseService.getObject(ErrorCodeDefine.class,code);
    }

}
