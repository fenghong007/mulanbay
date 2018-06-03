package cn.mulanbay.web.bind;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

/**
 * json序列化个性化配置
 * 复写默认SpringMVC中json序列化
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class CustomObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3188159062390016860L;

	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public CustomObjectMapper() {
		super();
		this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// // 使Jackson JSON支持Unicode编码非ASCII字符
		// SimpleModule module = new SimpleModule();
		// module.addSerializer(String.class, new StringUnicodeSerializer());
		// this.registerModule(module);
		// 设置null值不参与序列化(字段不被显示)
		this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		this.setDateFormat(simpleDateFormat);
	}
}