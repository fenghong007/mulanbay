package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.DiaryStat;
import cn.mulanbay.pms.persistent.domain.Diary;
import cn.mulanbay.pms.persistent.service.DiaryService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.diary.DiaryDateStatSearch;
import cn.mulanbay.pms.web.bean.request.diary.DiaryFormRequest;
import cn.mulanbay.pms.web.bean.request.diary.DiarySearch;
import cn.mulanbay.pms.web.bean.response.chart.ChartData;
import cn.mulanbay.pms.web.bean.response.chart.ChartYData;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by fenghong on 2017/6/5.
 */
@Controller
@RequestMapping("/diary")
public class DiaryController extends BaseController {

    private static Class<Diary> beanClass = Diary.class;

    @Autowired
    DiaryService diaryService;

    @RequestMapping(value = "list")
    public String list() {
        return "diary/diaryList";
    }


    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(DiarySearch sf) {
        //sf.setUserId(this.getCurrentUserId());
        return callbackDataGrid(getDiaryResult(sf));
    }

    private PageResult<Diary> getDiaryResult(DiarySearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("year",Sort.DESC);
        pr.addSort(sort);
        PageResult<Diary> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid DiaryFormRequest formRequest) {
        Diary bean = new Diary();
        BeanCopy.copyProperties(formRequest,bean);
        bean.setCreatedTime(new Date());
        baseService.saveObject(bean);
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
        Diary bean=this.getUserEntity(Diary.class, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid DiaryFormRequest formRequest) {
        Diary bean=this.getUserEntity(Diary.class, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setLastModifyTime(new Date());
        baseService.updateObject(bean);
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
        this.deleteUserEntity(Diary.class,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }

    /**
     * 总的概要统计
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(DiarySearch sf) {
        DiaryStat data = diaryService.statDiary(sf);
        return callback(data);
    }

    /**
     * 日记统计页面
     * @return
     */
    @RequestMapping(value = "/dateStatList")
    public String statList() {
        return "diary/diaryDateStatList";
    }


    /**
     * 基于日期的统计
     * 界面上使用echarts展示图表，后端返回的是核心模块的数据，不再使用Echarts的第三方jar包封装（比较麻烦）
     *
     * @return
     */
    @RequestMapping(value = "/dateStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean dateStat(DiaryDateStatSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort sort =new Sort("year",Sort.ASC);
        pr.addSort(sort);
        PageResult<Diary> qr = baseService.getBeanResult(pr);

        ChartData chartData = new ChartData();
        chartData.setTitle("日记统计");
        chartData.setLegendData(new String[]{"日记数","篇数"});
        ChartYData yData1= new ChartYData();
        yData1.setName("日记数");
        ChartYData yData2= new ChartYData();
        yData2.setName("篇数");
        for (Diary bean : qr.getBeanList()) {
            chartData.getIntXData().add(bean.getYear());
            chartData.getXdata().add(bean.getYear()+"");
            yData1.getData().add(bean.getWords());
            yData2.getData().add(bean.getPieces());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        chartData = ChartUtil.completeDate(chartData,sf);
        return callback(chartData);
    }

}
