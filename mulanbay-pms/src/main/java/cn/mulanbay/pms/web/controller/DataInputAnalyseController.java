package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.pms.persistent.bean.DataInputAnalyseStat;
import cn.mulanbay.pms.persistent.domain.DataInputAnalyse;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.service.DataService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.data.DataInputAnalyseSearch;
import cn.mulanbay.pms.web.bean.request.data.DataInputAnalyseStatSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.chart.ChartPieData;
import cn.mulanbay.pms.web.bean.response.chart.ChartPieSerieData;
import cn.mulanbay.pms.web.bean.response.chart.ChartPieSerieDetailData;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/1/21.
 */
@Controller
@RequestMapping("/dataInputAnalyse")
public class DataInputAnalyseController extends BaseController {

    private static Class<DataInputAnalyse> beanClass = DataInputAnalyse.class;

    @Autowired
    DataService dataService;

    /**
     *
     * @return
     */
    @RequestMapping(value = "/getDataInputAnalyseTree")
    @ResponseBody
    public ResultBean getDataInputAnalyseTree(Boolean needRoot) {
        try {
            DataInputAnalyseSearch sf = new DataInputAnalyseSearch();
            sf.setStatus(CommonStatus.ENABLE);
            PageRequest pr = sf.buildQuery();
            pr.setBeanClass(beanClass);
            PageResult<DataInputAnalyse> qr = baseService.getBeanResult(pr);
            List<TreeBean> list = new ArrayList<TreeBean>();
            List<DataInputAnalyse> gtList = qr.getBeanList();
            for (DataInputAnalyse gt : gtList) {
                TreeBean tb = new TreeBean();
                tb.setId(gt.getId().toString());
                tb.setText(gt.getName());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,needRoot));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取购买来源树异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list() {
        return "data/dataInputAnalyseList";
    }

    /**
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(DataInputAnalyseSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        PageResult<DataInputAnalyse> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(DataInputAnalyse bean) {
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
    public ResultBean get(Long id) {
        DataInputAnalyse br = baseService.getObject(DataInputAnalyse.class, id);
        return callback(br);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(DataInputAnalyse bean) {
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
    public ResultBean delete(String ids) {
        baseService.deleteObjects(DataInputAnalyse.class, NumberUtil.stringArrayToLongArray(ids.split(",")));
        return callback(null);
    }

    /**
     * 统计
     *
     * @return
     */
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean stat(DataInputAnalyseStatSearch sf) {
        List<DataInputAnalyseStat> list = dataService.statDataInputAnalyse(sf);
        ChartPieData chartPieData = new ChartPieData();
        chartPieData.setTitle("数据录入延迟分析");
        ChartPieSerieData serieData = new ChartPieSerieData();
        serieData.setName("分析");
        //总的值
        BigDecimal totalValue = new BigDecimal(0);
        for(DataInputAnalyseStat bean : list) {
            chartPieData.getXdata().add(bean.getGroupName());
            ChartPieSerieDetailData dataDetail = new ChartPieSerieDetailData();
            dataDetail.setName(bean.getGroupName());
            dataDetail.setValue(bean.getTotalCount());
            serieData.getData().add(dataDetail);
            totalValue=totalValue.add(new BigDecimal(bean.getTotalCount()));
        }
        String subTitle = this.getDateTitle(sf,String.valueOf(totalValue.intValue())+"次");
        chartPieData.setSubTitle(subTitle);
        chartPieData.getDetailData().add(serieData);
        return callback(chartPieData);
    }


}
