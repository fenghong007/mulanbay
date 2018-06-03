package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.pms.persistent.domain.User;
import cn.mulanbay.pms.persistent.enums.BussType;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.common.GetYearTreeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/2/4.
 * 公共接口
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

    /**
     * 获取月统计年份列表
     * @return
     */
    @RequestMapping(value = "/getYearTree")
    @ResponseBody
    public ResultBean getYearTree(GetYearTreeSearch sf) {

        try {
            User user = baseService.getObject(User.class,sf.getUserId());
            //最小年份由注册时间决定
            int minYear = Integer.valueOf(DateUtil.getFormatDate(user.getCreatedTime(),"yyyy"));
            int maxYear = Integer.valueOf(DateUtil.getFormatDate(new Date(),"yyyy"));
            List<TreeBean> list = new ArrayList<TreeBean>();
            for(int i=maxYear;i>=minYear;i--){
                TreeBean tb = new TreeBean();
                tb.setId(i+"");
                tb.setText(i+"年");
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取年份列表树异常",
                    e);
        }
    }

    /**
     * 业务类别列表
     * @return
     */
    @RequestMapping(value = "/getBussTypeTree")
    @ResponseBody
    public ResultBean getBussTypeTree(Boolean needRoot) {

        try {
            List<TreeBean> list = new ArrayList<TreeBean>();
            for(BussType bt : BussType.values()){
                TreeBean tb = new TreeBean();
                tb.setId(bt.getBeanName());
                tb.setText(bt.getName());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,needRoot));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取业务类别列表异常",
                    e);
        }
    }
}
