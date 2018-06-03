package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.persistent.domain.OperationLog;
import cn.mulanbay.pms.persistent.domain.SystemFunction;
import cn.mulanbay.pms.persistent.enums.IdFieldType;
import cn.mulanbay.pms.persistent.enums.LogCompareType;
import cn.mulanbay.pms.persistent.service.LogService;
import cn.mulanbay.pms.web.bean.request.log.OperationLogSearch;
import cn.mulanbay.pms.web.bean.response.log.OperationBeanDetailResponse;
import cn.mulanbay.pms.web.bean.response.log.OperationLogCompareResponse;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghong on 2017/1/10.
 */
@Controller
@RequestMapping("/operationLog")
public class OperationLogController extends BaseController  {

    private static Class<OperationLog> beanClass = OperationLog.class;

    @Autowired
    LogService logService;

    @RequestMapping(value = "list")
    public String list() {
        return "log/operationLogList";
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(OperationLogSearch sf) {
        return callbackDataGrid(getOperationLogResult(sf));
    }

    private PageResult<OperationLog> getOperationLogResult(OperationLogSearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("occurEndTime",Sort.DESC);
        pr.addSort(sort);
        PageResult<OperationLog> qr = baseService.getBeanResult(pr);
        return qr;
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/getParas", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getParas(Long id) {
        OperationLog br=baseService.getObject(OperationLog.class, id);
        return callback(br.getParas());
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/getReturnData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getReturnData(Long id) {
        OperationLog br=baseService.getObject(OperationLog.class, id);
        return callback(br.getReturnData());
    }

    /**
     * 查询被操作的业务对象的数据
     * @param id 为操作日志的记录号
     * @return
     */
    @RequestMapping(value = "/showBeanDetail", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean showBeanDetail(Long id) {
        OperationLog br=baseService.getObject(OperationLog.class, id);
        String idValue = br.getIdValue();
        if(StringUtil.isEmpty(idValue)){
            throw new ApplicationException(PmsErrorCode.OPERATION_LOG_BEAN_ID_NULL);
        }else{
            OperationBeanDetailResponse response = new OperationBeanDetailResponse();
            response.setIdValue(idValue);
            response.setBeanName(br.getSystemFunction().getBeanName());
            Serializable bussId=formatIdValue(br.getSystemFunction().getIdFieldType(),idValue);
            Object o = baseService.getObject(br.getSystemFunction().getBeanName(),bussId,br.getSystemFunction().getIdField());
            response.setBeanData(o);
            return callback(response);
        }
    }

    /**
     * 获取操作日志的比对数据：最新的数据、当前的数据、往前（或往后）的数据
     * @param id 为OperationLog的主键
     * @return
     */
    @RequestMapping(value = "/getCompareData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCompareData(Long id,LogCompareType compareType) {
        OperationLogCompareResponse response = new OperationLogCompareResponse();
        //获取当前日志记录的数据
        OperationLog br=baseService.getObject(OperationLog.class, id);
        response.setCurrentData(br);
        response.setBussId(br.getIdValue());
        SystemFunction sf = br.getSystemFunction();
        String idValue=getAndUpdateIdValue(br);
        if(sf!=null){
            //获取业务表最新的数据
            Serializable bussId=formatIdValue(sf.getIdFieldType(),idValue);
            Object o = baseService.getObject(sf.getBeanName(),bussId,sf.getIdField());
            if(o!=null){
                response.setLatestData(o);
            }
        }
        OperationLog nearest = logService.getNearestCompareLog(br,compareType);
        response.setCompareData(nearest);
        return callback(response);
    }

    /**
     * 获取某个具体业务bean的比对数据：最新的数据、当前的数据、往前（或往后）的数据
     * @param id 为beanName的主键

     * @return
     */
    @RequestMapping(value = "/getEditLogData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getEditLogData(String id,LogCompareType compareType,String beanName) {
        OperationLogCompareResponse response = new OperationLogCompareResponse();
        SystemFunction sf = logService.getEditSystemFunction(beanName);
        if(sf==null){
            throw new ApplicationException(PmsErrorCode.SYSTEM_FUNCTION_NOT_DEFINE,beanName+"修改类功能点没有定义");
        }
        //获取业务表最新的数据
        Serializable bussId=formatIdValue(sf.getIdFieldType(),id);
        response.setBussId(id);
        Object o = baseService.getObject(sf.getBeanName(),bussId,sf.getIdField());
        if(o!=null){
            response.setLatestData(o);
        }
        //获取最近一次修改
        OperationLog lastestOperationLog = logService.getLatestOperationLog(id,sf.getId());
        if(lastestOperationLog!=null){
            response.setCurrentData(lastestOperationLog);
            //获取最近的修改记录比较
            OperationLog nearest = logService.getNearestCompareLog(lastestOperationLog,compareType);
            response.setCompareData(nearest);
        }

        return callback(response);
    }

    private Serializable formatIdValue(IdFieldType idFieldType, String idValue){
        Serializable bussId=null;
        if(idFieldType== IdFieldType.LONG){
            bussId = Long.valueOf(idValue);
        }else if(idFieldType== IdFieldType.INTEGER){
            bussId = Integer.valueOf(idValue);
        }else if(idFieldType== IdFieldType.SHORT){
            bussId = Short.valueOf(idValue);
        }else {
            bussId = idValue;
        }
        return bussId;
    }

    private String getAndUpdateIdValue(OperationLog br){
        String idValue = br.getIdValue();
        if(StringUtil.isEmpty(idValue)){
            //从paras重新获取
            Map map  = (Map) JsonUtil.jsonToBean(br.getParas(),Map.class);
            Object o = map.get(br.getSystemFunction().getIdField());
            if(o!=null){
                idValue = o.toString();
                br.setIdValue(idValue);
                //更新
                baseService.updateObject(br);
            }
        }
        return idValue;
    }

    /**
     * 获取比对数据：最新的数据、当前的数据、往前（或往后）的数据
     * @param currentCompareId 目前正在比较的OperationLog的主键(比较页面的中间那个区域)
     * @return
     */
    @RequestMapping(value = "/getNearstCompareData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getNearstCompareData(Long currentCompareId,LogCompareType compareType) {
        OperationLogCompareResponse response = new OperationLogCompareResponse();
        if(currentCompareId==null){
            return callback(response);
        }
        OperationLog currentCompareLog=baseService.getObject(OperationLog.class, currentCompareId);
        if(StringUtil.isEmpty(currentCompareLog.getIdValue())){
            //idValue无法比较
            throw new ApplicationException(PmsErrorCode.OPERATION_LOG_COMPARE_ID_VALUE_NULL);
        }
        OperationLog nextCompareLog = logService.getNearestCompareLog(currentCompareLog,compareType);
        response.setCompareData(nextCompareLog);
        OperationLog currentLog=baseService.getObject(OperationLog.class, currentCompareId);
        response.setCurrentData(currentLog);
        return callback(response);
    }

    /**
     * 管理功能点ID
     *
     * @return
     */
    @RequestMapping(value = "/setOperationLogFunctionId", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean setOperationLogFunctionId(boolean needReSet) {
        logService.setOperationLogFunctionId(needReSet);
        return callback(null);
    }

    /**
     * 设置操作日志中的主键值
     *
     * @return
     */
    @RequestMapping(value = "/setIdValue", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean setIdValue() {
        List<OperationLog> list = logService.getOperationLogByIdValueWithNull();
        for(OperationLog log : list){
            logService.setIdValue(log);
        }
        return callback(null);
    }

}
