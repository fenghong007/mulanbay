package cn.mulanbay.pms.web.controller;

/**
 * Created by fenghong on 2017/1/13.
 */

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.MusicPracticeTuneStat;
import cn.mulanbay.pms.persistent.domain.MusicPractice;
import cn.mulanbay.pms.persistent.domain.MusicPracticeTune;
import cn.mulanbay.pms.persistent.service.MusicPracticeService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.music.MusicPracticeTuneFormRequest;
import cn.mulanbay.pms.web.bean.request.music.MusicPracticeTuneSearch;
import cn.mulanbay.pms.web.bean.request.music.MusicPracticeTuneTreeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.chart.ChartData;
import cn.mulanbay.pms.web.bean.response.chart.ChartYData;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 口琴练习曲子记录
 */
@Controller
@RequestMapping("/musicPracticeTune")
public class MusicPracticeTuneController extends BaseController{
    private static Class<MusicPracticeTune> beanClass = MusicPracticeTune.class;

    @Autowired
    MusicPracticeService musicPracticeService;

    /**
     * 获取看病或者器官的各种分类归类
     *
     * @return
     */
    @RequestMapping(value = "/getMusicPracticeTuneTree")
    @ResponseBody
    public ResultBean getMusicPracticeTuneTree(MusicPracticeTuneTreeSearch sf) {

        try {
            List<String> tuneList = musicPracticeService.getMusicPracticeTuneList(sf);
            List<TreeBean> list = new ArrayList<TreeBean>();
            int i = 0;
            for (String ss : tuneList) {
                TreeBean tb = new TreeBean();
                tb.setId(ss);
                tb.setText(ss);
                list.add(tb);
                i++;
            }
            return callback(TreeBeanUtil.addRoot(list, sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取看病的各种分类归类异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list() {
        return "music/musicPracticeTuneList";
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @RequestMapping(value = "/getData")
    @ResponseBody
    public ResultBean getData(MusicPracticeTuneSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("musicPractice.practiceDate", Sort.DESC);
        pr.addSort(s);
        PageResult<MusicPracticeTune> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid MusicPracticeTuneFormRequest formRequest ) {
        MusicPracticeTune bean = new MusicPracticeTune();
        BeanCopy.copyProperties(formRequest,bean);
        MusicPractice musicPractice=this.getUserEntity(MusicPractice.class, formRequest.getMusicPracticeId(),formRequest.getUserId());
        bean.setMusicPractice(musicPractice);
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
        MusicPracticeTune bean=this.getUserEntity(MusicPracticeTune.class, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid MusicPracticeTuneFormRequest formRequest) {
        MusicPracticeTune bean =this.getUserEntity(MusicPracticeTune.class, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        MusicPractice musicPractice=this.getUserEntity(MusicPractice.class, formRequest.getMusicPracticeId(),formRequest.getUserId());
        bean.setMusicPractice(musicPractice);
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
        baseService.deleteObjects(MusicPracticeTune.class, NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")));
        return callback(null);
    }


    @RequestMapping(value = "/tuneStatList")
    public String tuneStatList() {
        return "music/musicPracticeTuneStatList";
    }


    /**
     * 统计
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(MusicPracticeTuneSearch sf) {
        //sf.setUserId(this.getCurrentUserId());
        List<MusicPracticeTuneStat> list = musicPracticeService.statTune(sf);
        ChartData chartData = new ChartData();
        chartData.setTitle(musicPracticeService.getMusicInstrumentName(sf.getMusicInstrumentId())+"练习曲子统计");

        chartData.setLegendData(new String[]{"次数"});
        ChartYData yData1= new ChartYData();
        yData1.setName("次数");
        for(MusicPracticeTuneStat bean : list){
            chartData.getXdata().add(bean.getName());
            yData1.getData().add(bean.getTotalTimes());
        }
        chartData.getYdata().add(yData1);
        return callback(chartData);
    }

}
