package cn.mulanbay.pms.web.controller;

/**
 * Created by fenghong on 2017/1/14.
 */

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.bean.LifeExperienceDateStat;
import cn.mulanbay.pms.persistent.bean.LifeExperienceMapStat;
import cn.mulanbay.pms.persistent.bean.TransferMapStat;
import cn.mulanbay.pms.persistent.domain.CityLocation;
import cn.mulanbay.pms.persistent.domain.LifeExperience;
import cn.mulanbay.pms.persistent.domain.LifeExperienceDetail;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.service.LifeExperienceService;
import cn.mulanbay.pms.util.ChartUtil;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.GroupType;
import cn.mulanbay.pms.web.bean.request.life.*;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.chart.ChartData;
import cn.mulanbay.pms.web.bean.response.chart.ChartYData;
import cn.mulanbay.pms.web.bean.response.life.LocationMapStatResponse;
import cn.mulanbay.pms.web.bean.response.life.TransferMapDoubleStatResponse;
import cn.mulanbay.pms.web.bean.response.life.TransferMapSingleStatResponse;
import cn.mulanbay.pms.web.bean.response.life.TransferMapStatResponse;
import cn.mulanbay.web.bean.response.ResultBean;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.VisualMap;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.MapData;
import com.github.abel533.echarts.series.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 人生经历表
 */
@Controller
@RequestMapping("/lifeExperience")
public class LifeExperienceController extends BaseController {

    @Autowired
    LifeExperienceService lifeExperienceService;

    private static Class<LifeExperience> beanClass = LifeExperience.class;

    @RequestMapping(value = "list")
    public String list() {
        return "life/lifeExperienceList";
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(LifeExperienceSearch sf) {
        PageRequest pr = sf.buildQuery();
        pr.setPageSize(sf.getPageSize());
        pr.setBeanClass(beanClass);
        Sort s = new Sort("startDate", Sort.DESC);
        pr.addSort(s);
        PageResult<LifeExperience> qr = baseService.getBeanResult(pr);
        return callbackDataGrid(qr);
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid LifeExperienceFormRequest formRequest) {
        LifeExperience bean = new LifeExperience();
        BeanCopy.copyProperties(formRequest,bean);
        bean.setCost(0.0);
        bean.setCreatedTime(new Date());
        baseService.saveObject(bean);
        return callback(bean);
    }


    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
        LifeExperience bean=this.getUserEntity(beanClass, getRequest.getId(),getRequest.getUserId());
        return callback(bean);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid LifeExperienceFormRequest formRequest) {
        LifeExperience bean=this.getUserEntity(beanClass, formRequest.getId(),formRequest.getUserId());
        BeanCopy.copyProperties(formRequest,bean);
        bean.setLastModifyTime(new Date());
        if(bean.getCost()==null){
            bean.setCost(0.0);
        }
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
        this.deleteUserEntity(beanClass,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }
    @RequestMapping(value = "/mapStatList")
    public String realtimeStatList() {
        return "life/mapStatList";
    }

    /**
     * 地图统计
     * 如果是中国地图、世界地图类型的统计，则直接返回全部封装好的Option
     * 如果是按照地点统计分析（即详细的统计），后端只返回核心数据（用com.github.abel533.echarts比较麻烦，觉得以后的统计还是只返回核心数据）
     * @return
     */
    @RequestMapping(value = "/mapStat")
    @ResponseBody
    public ResultBean mapStat(LifeExperienceMapStatSearch sf) {
        sf.setIntTypes(sf.getTypes());
        List<LifeExperienceMapStat> list = lifeExperienceService.getLifeExperienceMapStat(sf);
        if(sf.getMapType()== LifeExperienceMapStatSearch.MapType.LOCATION){
            return this.callback(createLocationMapStat(list,sf.getStatType()));
        }
        String mapType="china";
        // 是否需要显示地图上的地点名称
        boolean showName=true;
        if(sf.getMapType()== LifeExperienceMapStatSearch.MapType.WORLD){
            mapType="world";
            showName=false;
        }
        Option option = new Option();
        option.title("人生去过的地方统计",this.getDateTitle(sf));
        option.title().subtext(DateUtil.getFormatDate(sf.getStartDate(),DateUtil.FormatDay1)+"~"+DateUtil.getFormatDate(sf.getEndDate(),DateUtil.FormatDay1));
        option.title().x("center");
        option.tooltip().trigger(Trigger.item);
        option.legend().orient(Orient.vertical).left("left").data(sf.getStatType().getName());


        VisualMap visualMap = new VisualMap();
        visualMap.setMin(0);
        visualMap.left("left");
        visualMap.top("bottom");
        visualMap.text(new String[]{"高","低"});
        visualMap.calculable(true);
        option.visualMap().add(visualMap);
        option.toolbox().show(true).orient(Orient.vertical).left("right").top("center");

        List<MapData> mapDataList  = new ArrayList<>();
        int maxValue=0;
        for(LifeExperienceMapStat dd: list){
            if(sf.getStatType()== LifeExperienceMapStatSearch.StatType.COUNT){
                MapData c = new MapData(dd.getName(),dd.getTotalCount().intValue());
                mapDataList.add(c);
                if(dd.getTotalCount().intValue()>maxValue){
                    maxValue = dd.getTotalCount().intValue();
                }
            }else{
                MapData c = new MapData(dd.getName(),dd.getTotalDays().longValue());
                mapDataList.add(c);
                if(dd.getTotalDays().intValue()>maxValue){
                    maxValue = dd.getTotalDays().intValue();
                }
            }
        }
        //最大值由计算得出
        visualMap.setMax(maxValue);
        Map map = new Map();
        map.name(sf.getStatType().getName()).mapType(mapType).roam(false).label().normal().show(showName);
        map.label().emphasis().show(true);
        map.setData(mapDataList);
        option.series().add(map);
        return callback(option);
    }

    /**
     * 基于地点的统计
     * @param list
     * @return
     */
    private LocationMapStatResponse createLocationMapStat(List<LifeExperienceMapStat> list , LifeExperienceMapStatSearch.StatType statType){
        LocationMapStatResponse response = new LocationMapStatResponse();
        List<MapData> dataList = new ArrayList<>();
        List<LifeExperienceMapStat> newList = convertMapStatLocation(list);
        int maxValue=0;
        for(LifeExperienceMapStat dd : newList){
            if(statType== LifeExperienceMapStatSearch.StatType.COUNT){
                MapData c = new MapData(dd.getName(),dd.getTotalCount().intValue());
                dataList.add(c);
                if(dd.getTotalCount().intValue()>maxValue){
                    maxValue = dd.getTotalCount().intValue();
                }
            }else{
                MapData c = new MapData(dd.getName(),dd.getTotalDays().longValue());
                dataList.add(c);
                if(dd.getTotalDays().intValue()>maxValue){
                    maxValue = dd.getTotalDays().intValue();
                }
            }
        }
        response.setDataList(dataList);
        response.setMax(maxValue);
        response.setGeoCoordMapData(getGeoCoordMapData());
        return response;

    }

    /**
     * 获取地理位置数据定义
     * @return
     */
    private java.util.Map<String,double[]> getGeoCoordMapData(){
        PageResult<CityLocation> pr=baseService.getBeanResult(CityLocation.class,-1,0, (Sort) null);
        java.util.Map<String,double[]> geoCoordMapData = new HashMap<>();
        for(CityLocation cl : pr.getBeanList()){
            geoCoordMapData.put(cl.getLocation(),new double[]{cl.getLon(),cl.getLat()});

        }
        return geoCoordMapData;
    }

    /**
     * 把location字段根据分割符重新统计
     * @param list
     * @return
     */
    private List<LifeExperienceMapStat> convertMapStatLocation(List<LifeExperienceMapStat> list){
        java.util.Map<String,LifeExperienceMapStat> map = new HashMap<>();
        for(LifeExperienceMapStat dd :list){
            String name = dd.getName();
            LifeExperienceMapStat stat = map.get(name);
            if(stat==null){
                LifeExperienceMapStat newStat = new LifeExperienceMapStat();
                newStat.setName(name);
                newStat.setTotalDays(dd.getTotalDays());
                newStat.setTotalCount(dd.getTotalCount());
                map.put(name,newStat);
            }else{
                stat.setTotalCount(stat.getTotalCount().add(dd.getTotalCount()));
                stat.setTotalDays(stat.getTotalDays().add(dd.getTotalDays()));
            }
        }
        List<LifeExperienceMapStat> result = new ArrayList<>();
        for(LifeExperienceMapStat stat :map.values() ){
            result.add(stat);
        }
        return result;
    }

    /**
     * 获取出发城市树
     *
     * @return
     */
    @RequestMapping(value = "/getStartCityTree")
    @ResponseBody
    public ResultBean getStartCityTree(LifeExperienceGetStartCityTreeRequest sf) {
        try {
            List<String> citys = lifeExperienceService.getStartCityList(sf.getUserId());
            List<TreeBean> list = new ArrayList<TreeBean>();
            for (String s : citys) {
                TreeBean tb = new TreeBean();
                tb.setId(s);
                tb.setText(s);
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取出发城市树异常",
                    e);
        }
    }

    /**
     * 迁徙地图
     * @return
     */
    @RequestMapping(value = "/transferMapStatList")
    public String transferMapStatList() {
        return "life/transferMapStatList";
    }

    /**
     * 针对某个经历迁徙地图统计
     * @param id
     * @return
     */
    @RequestMapping(value = "/transferMapByLifeExpStat")
    @ResponseBody
    public ResultBean transferMapByLifeExpStat(Long id) {
        LifeExperience lifeExperience = baseService.getObject(LifeExperience.class,id);
        LifeExperienceDetailSearch sf = new LifeExperienceDetailSearch();
        sf.setLifeExperienceId(id);
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("occurDate", Sort.ASC);
        pr.addSort(s);
        pr.setBeanClass(LifeExperienceDetail.class);
        List<LifeExperienceDetail> detailList = baseService.getBeanList(pr);
        TransferMapDoubleStatResponse response = new TransferMapDoubleStatResponse();
        response.setTitle(lifeExperience.getName()+"线路");
        response.setSubTitle("");
        response.setGeoCoordMapData(getGeoCoordMapData());
        List<TransferMapStat> list = new ArrayList<>();
        BigInteger totalCount = BigInteger.valueOf(1L);
        for(LifeExperienceDetail detail : detailList){
            TransferMapStat tms = new TransferMapStat();
            tms.setStartCity(detail.getStartCity());
            tms.setArriveCity(detail.getArriveCity());
            tms.setTotalCount(totalCount);
            list.add(tms);
        }
        response.setStatData(list);
        return  callback(response);
    }

    /**
     * 迁徙地图统计
     * @param sf
     * @return
     */
    @RequestMapping(value = "/transferMapStat")
    @ResponseBody
    public ResultBean transferMapStat(LifeExperienceMapStatSearch sf) {
        //sf.setUserId(this.getCurrentUserId());
        if(sf.getMapType()== LifeExperienceMapStatSearch.MapType.TRANSFER_DOUBLE){
            return callback(this.createDoubleTransferMapStatResponse(sf));
        }else{
            return callback(this.createSingleTransferMapStatResponse(sf));
        }
    }

    /**
     * 单向迁移地图数据封装
     * @param sf
     * @return
     */
    private TransferMapStatResponse createDoubleTransferMapStatResponse(LifeExperienceMapStatSearch sf){
        TransferMapDoubleStatResponse response = new TransferMapDoubleStatResponse();
        response.setTitle("人生经历线路统计");
        response.setSubTitle(this.getDateTitle(sf));
        response.setGeoCoordMapData(getGeoCoordMapData());
        //sf.setUserId(this.getCurrentUserId());
        List<TransferMapStat> list = lifeExperienceService.statTransMap(sf);
        response.setStatData(list);
        return  response;
    }

    /**
     * 单向迁移地图数据封装
     * @param sf
     * @return
     */
    private TransferMapStatResponse createSingleTransferMapStatResponse(LifeExperienceMapStatSearch sf){
        TransferMapSingleStatResponse response = new TransferMapSingleStatResponse();
        response.setTitle("人生经历线路统计");
        response.setSubTitle(this.getDateTitle(sf));
        response.setGeoCoordMapData(getGeoCoordMapData());
        //sf.setUserId(this.getCurrentUserId());
        List<String> startCitys =null;
        if(StringUtil.isEmpty(sf.getStartCity())){
            //获取全部
            startCitys = lifeExperienceService.getStartCityList(sf.getUserId());
        }else {
            startCitys= new ArrayList<>();
            startCitys.add(sf.getStartCity());
        }
        response.setLegendData(startCitys);
        for(String s : startCitys){
            sf.setStartCity(s);
            List<TransferMapStat> list = lifeExperienceService.statTransMap(sf);
            response.getStatData().add(list);
        }
        return  response;
    }

    /**
     * 锻炼管理统计页面
     * @return
     */
    @RequestMapping(value = "/dateStatList")
    public String statList() {
        return "life/lifeExperienceDateStatList";
    }


    /**
     * 基于日期的统计
     * 界面上使用echarts展示图表，后端返回的是核心模块的数据，不再使用Echarts的第三方jar包封装（比较麻烦）
     *
     * @return
     */
    @RequestMapping(value = "/dateStat", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean dateStat(LifeExperienceDateStatSearch sf) {
        sf.setIntTypes(sf.getTypes());
        List<LifeExperienceDateStat> list = lifeExperienceService.statDateLifeExperience(sf);
        if(sf.getDateGroupType()== DateGroupType.DAYCALENDAR){
            return callback(ChartUtil.createChartCalandarData("人生经历统计","次数","次",sf,list));
        }
        ChartData chartData = new ChartData();
        chartData.setTitle("人生经历统计");
        chartData.setLegendData(new String[]{"次数","天数"});
        ChartYData yData1= new ChartYData();
        yData1.setName("次数");
        ChartYData yData2= new ChartYData();
        yData2.setName("天数");
        for (LifeExperienceDateStat bean : list) {
            chartData.addXData(bean,sf.getDateGroupType());
            yData1.getData().add(bean.getTotalCount());
            yData2.getData().add(bean.getTotalDays());
        }
        chartData.getYdata().add(yData1);
        chartData.getYdata().add(yData2);
        chartData = ChartUtil.completeDate(chartData,sf);
        return callback(chartData);
    }

    /**
     * 同期比对
     * @return
     */
    @RequestMapping(value = "/yoyStatList")
    public String yoyStatList() {
        return "life/lifeExperienceYoyStatList";
    }

    /**
     * 按照日期统计
     * @return
     */
    @RequestMapping(value = "/yoyStat")
    @ResponseBody
    public ResultBean yoyStat(@Valid LifeExperienceYoyStatSearch sf) {
        ChartData chartData = initYoyCharData(sf,"人生经历同期对比",null);
        String[] legendData = new String[sf.getYears().size()];
        for(int i=0;i<sf.getYears().size();i++){
            legendData[i]=sf.getYears().get(i).toString();
            //数据,为了代码复用及统一，统计还是按照日期的统计
            LifeExperienceDateStatSearch dateSearch= new LifeExperienceDateStatSearch();
            dateSearch.setDateGroupType(sf.getDateGroupType());
            dateSearch.setStartDate(DateUtil.getDate(sf.getYears().get(i)+"-01-01",DateUtil.FormatDay1));
            dateSearch.setEndDate(DateUtil.getDate(sf.getYears().get(i)+"-12-31",DateUtil.FormatDay1));
            dateSearch.setUserId(sf.getUserId());
            dateSearch.setIntTypes(sf.getTypes());
            ChartYData yData= new ChartYData();
            yData.setName(sf.getYears().get(i).toString());
            List<LifeExperienceDateStat> list = lifeExperienceService.statDateLifeExperience(dateSearch);
            //临时内容，作为补全用
            ChartData temp = new ChartData();
            for(LifeExperienceDateStat bean : list){
                temp.addXData(bean,sf.getDateGroupType());
                if(sf.getGroupType()== GroupType.COUNT){
                    yData.getData().add(bean.getTotalCount());
                }else if(sf.getGroupType()==GroupType.DAYS){
                    yData.getData().add(bean.getTotalDays());
                }
            }
            //临时内容，作为补全用
            temp.getYdata().add(yData);
            dateSearch.setCompliteDate(true);
            temp = ChartUtil.completeDate(temp,dateSearch);
            //设置到最终的结果集中
            chartData.getYdata().add(temp.getYdata().get(0));
        }
        chartData.setLegendData(legendData);

        return callback(chartData);
    }

    /**
     * 修正花费和天数
     * @param revise
     *
     * @return
     */
    @RequestMapping(value = "/revise")
    @ResponseBody
    public ResultBean revise(LifeExperienceRevise revise) {
        lifeExperienceService.reviseLifeExperience(revise);
        return callback(null);
    }

}
