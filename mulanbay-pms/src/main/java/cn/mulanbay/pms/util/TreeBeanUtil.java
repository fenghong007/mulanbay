package cn.mulanbay.pms.util;

import cn.mulanbay.pms.web.bean.response.TreeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghong on 2017/2/25.
 */
public class TreeBeanUtil {

    /**
     * 为tree添加跟节点
     * @param list
     * @param needRoot
     * @return
     */
    public static List<TreeBean> addRoot(List<TreeBean> list,Boolean needRoot){
        if(needRoot==null||needRoot==false){
            return list;
        }else{
            TreeBean root = new TreeBean();
            root.setId("");
            root.setText("请选择");
            root.setChildren(list);
            List<TreeBean> newList = new ArrayList<TreeBean>();
            newList.add(root);
            return newList;
        }
    }

    /**
     * 转换为tree
     * @param list 不含层级关系的单层list
     * @return
     */
    public static TreeBean changToTree(TreeBean root,List<TreeBean> list){
        for(TreeBean treeBean : list){
            if(treeBean.getPid()!=null&&treeBean.getPid().equals(root.getId())){
                root.addChild(treeBean);
            }
        }
        if(root.getChildren()!=null){
            for(TreeBean cc : root.getChildren()){
                changToTree(cc,list);
            }
        }
        return root;
    }

}
