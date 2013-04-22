package cn.ls.integrator.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.integration.Message;
import org.springframework.integration.router.AbstractMessageRouter;

import cn.ls.integrator.core.utils.StringUtility;

public class TestRouter extends AbstractMessageRouter{

	private volatile String filterListStr;
	
	@Override
	protected List<Object> getChannelIdentifiers(Message<?> message) {
		List<Object> list = new ArrayList<Object>();
		if(!StringUtility.isBlank(filterListStr)){
			String[] strArr = filterListStr.split(",");
			for(String str: strArr){
				list.add(str);
			}
		}
		return list;
	}

	public String getFilterListStr() {
		return filterListStr;
	}

	public void setFilterListStr(String filterListStr) {
		this.filterListStr = filterListStr;
	}

}
