package cn.mulanbay.pms.web.controller;

/**
 * Created by fenghong on 2017/1/13.
 */

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.BuyRecord;
import cn.mulanbay.pms.persistent.domain.ConsumeType;
import cn.mulanbay.pms.persistent.domain.LifeExperienceConsume;
import cn.mulanbay.pms.persistent.domain.LifeExperienceDetail;
import cn.mulanbay.pms.persistent.service.LifeExperienceService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.buy.BuyRecordSearch;
import cn.mulanbay.pms.web.bean.request.life.LifeExperienceConsumeBuyRecordSearch;
import cn.mulanbay.pms.web.bean.request.life.LifeExperienceConsumeFormRequest;
import cn.mulanbay.pms.web.bean.request.life.LifeExperienceConsumeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人生经历明细
 */
@Controller
@RequestMapping("/lifeExperienceConsume")
public class LifeExperienceConsumeController extends BaseController{
    
    private static Class<LifeExperienceConsume> beanClass = LifeExperienceConsume.class;

    @Autowired
    LifeExperienceService lifeExperienceService;

    @RequestMapping(value = "list")
    public String list() {
        return "life/LifeExperienceConsumeList";
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @RequestMapping(value = "/getData")
    @ResponseBody
    public ResultBean getData(LifeExperienceConsumeSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("createdTime", Sort.DESC);
        pr.addSort(s);
        PageResult<LifeExperienceConsume> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid LifeExperienceConsumeFormRequest formRequest) {
        LifeExperienceConsume bean = new LifeExperienceConsume();
        BeanCopy.copyProperties(formRequest,bean);
        LifeExperienceDetail lifeExperienceDetail = this.getUserEntity(LifeExperienceDetail.class,formRequest.getLifeExperienceDetailId(),formRequest.getUserId());
        bean.setLifeExperienceDetail(lifeExperienceDetail);
        ConsumeType consumeType = this.getUserEntity(ConsumeType.class,formRequest.getConsumeTypeId(),formRequest.getUserId());
        bean.setConsumeType(consumeType);
        bean.setCreatedTime(new Date());
        lifeExperienceService.saveOrUpdateLifeExperienceConsume(bean,true);
        return callback(null);
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        LifeExperienceConsume bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid LifeExperienceConsumeFormRequest formRequest) {
        LifeExperienceConsume bean = this.getUserEntity(LifeExperienceConsume.class,formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        LifeExperienceDetail lifeExperienceDetail = this.getUserEntity(LifeExperienceDetail.class,formRequest.getLifeExperienceDetailId(),formRequest.getUserId());
        bean.setLifeExperienceDetail(lifeExperienceDetail);
        ConsumeType consumeType = this.getUserEntity(ConsumeType.class,formRequest.getConsumeTypeId(),formRequest.getUserId());
        bean.setConsumeType(consumeType);
        bean.setLastModifyTime(new Date());
        lifeExperienceService.saveOrUpdateLifeExperienceConsume(bean,true);
        return callback(null);
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
        this.deleteUserEntity(beanClass,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/getBuyRecordTree", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getBuyRecordTree(LifeExperienceConsumeBuyRecordSearch sf) {
        //先获取LifeExperienceDetail得到开始与结束日期
        LifeExperienceDetail detail =baseService.getObject(LifeExperienceDetail.class,sf.getLifeExperienceDetailId());
        BuyRecordSearch brsf = new BuyRecordSearch();
        brsf.setUserId(sf.getUserId());
        if(StringUtil.isEmpty(sf.getName())){
        }else{
            brsf.setName(sf.getName());
        }
        //选择前后三十天
        brsf.setStartDate(DateUtil.getDate((0-sf.getRoundDays()),detail.getOccurDate()));
        brsf.setEndDate(DateUtil.getDate(sf.getRoundDays(),detail.getOccurDate()));
        PageRequest pr = brsf.buildQuery();
        Sort s = new Sort("buyDate", Sort.ASC);
        pr.addSort(s);
        pr.setBeanClass(BuyRecord.class);
        List<BuyRecord> list = baseService.getBeanList(pr);
        List<TreeBean> treeBeans = new ArrayList<TreeBean>();
        for (BuyRecord gt : list) {
            TreeBean tb = new TreeBean();
            tb.setId(gt.getId().toString());
            tb.setText(gt.getGoodsName()+"("+ DateUtil.getFormatDate(gt.getBuyDate(),DateUtil.FormatDay1+")"));
            treeBeans.add(tb);
        }
        return callback(TreeBeanUtil.addRoot(treeBeans,false));
    }

}
