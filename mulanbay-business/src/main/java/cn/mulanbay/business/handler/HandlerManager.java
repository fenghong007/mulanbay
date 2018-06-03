package cn.mulanbay.business.handler;

import cn.mulanbay.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler管理类
 * 系统初始化时由它来调用各个Handler的初始化工作
 * 系统关闭时由它来调用各个Handler的资源回收工作
 * @see cn.mulanbay.web.servlet.SpringServlet
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class HandlerManager {

	@Autowired
	private List<BaseHandler> handlerList;

	public List<BaseHandler> getHandlerList() {
		return handlerList;
	}

	public void setHandlerList(List<BaseHandler> handlerList) {
		this.handlerList = handlerList;
	}
	
	public HandlerResult handleCmd(int cmdCode,int cmdValue,boolean isSync,Object para){
		HandlerResult hr =new HandlerResult();
		return hr;
	}

	/**
	 * 获取Handler信息列表
	 * @return
	 */
	public List<HandlerInfo> getHanderInfoList(){
		if(StringUtil.isEmpty(handlerList)){
			return new ArrayList<>();
		}else{
			List<HandlerInfo> res = new ArrayList<>();
			for(BaseHandler bh : handlerList){
				res.add(bh.getHanderInfo());
			}
			return res;
		}
	}


}
