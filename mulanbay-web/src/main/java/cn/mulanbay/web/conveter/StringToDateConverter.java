package cn.mulanbay.web.conveter;

import cn.mulanbay.common.util.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class StringToDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}else{
			if(source.length()==10){
				return DateUtil.getDate(source, DateUtil.FormatDay1);
			}else{
				return DateUtil.getDate(source, DateUtil.Format24Datetime);
			}
			
		}
	}

}
