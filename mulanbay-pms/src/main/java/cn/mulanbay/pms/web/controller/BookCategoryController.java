package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.BookCategory;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.read.BookCategoryFormRequest;
import cn.mulanbay.pms.web.bean.request.read.BookCategorySearch;
import cn.mulanbay.pms.web.bean.request.read.BookCategoryTreeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghong on 2017/6/29.
 * 乐器管理
 */
@Controller
@RequestMapping("/bookCategory")
public class BookCategoryController extends BaseController {

    private static Class<BookCategory> beanClass = BookCategory.class;

    /**
     * 为锻炼管理界面的下拉菜单使用
     * @return
     */
    @RequestMapping(value = "/getBookCategoryTree")
    @ResponseBody
    public ResultBean getBookCategoryTree(BookCategoryTreeSearch scts) {

        try {
            BookCategorySearch sf = new BookCategorySearch();
            sf.setUserId(scts.getUserId());
            sf.setPage(-1);
            PageResult<BookCategory> pr = getBookCategory(sf);
            List<TreeBean> list = new ArrayList<TreeBean>();
            List<BookCategory> gtList = pr.getBeanList();
            for (BookCategory gt : gtList) {
                TreeBean tb = new TreeBean();
                tb.setId(gt.getId().toString());
                tb.setText(gt.getName());
                list.add(tb);
            }
            return callback(TreeBeanUtil.addRoot(list,scts.getNeedRoot()));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取书籍分类树异常",
                    e);
        }
    }

    @RequestMapping(value = "list")
    public String list() {
        return "read/bookCategoryList";
    }

    /**
     * 获取运动锻炼列表
     *
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getData(BookCategorySearch sf) {
        PageResult<BookCategory> qr = getBookCategory(sf);
        return callbackDataGrid(qr);
    }

    private PageResult<BookCategory> getBookCategory(BookCategorySearch sf){
        PageRequest pr = sf.buildQuery();
        pr.setBeanClass(beanClass);
        Sort s = new Sort("orderIndex", Sort.ASC);
        pr.addSort(s);
        PageResult<BookCategory> qr = baseService.getBeanResult(pr);
        return qr;
    }

    /**
     * 创建
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean create(@Valid BookCategoryFormRequest formRequest) {
        BookCategory bean= new BookCategory();
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
        BookCategory br = this.getUserEntity(BookCategory.class, getRequest.getId(),getRequest.getUserId());
        return callback(br);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean edit(@Valid BookCategoryFormRequest formRequest) {
        BookCategory bean = this.getUserEntity(BookCategory.class, formRequest.getId(),formRequest.getUserId());
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
        this.deleteUserEntity(BookCategory.class,
                NumberUtil.stringArrayToLongArray(deleteRequest.getIds().split(",")),
                deleteRequest.getUserId());
        return callback(null);
    }
}
