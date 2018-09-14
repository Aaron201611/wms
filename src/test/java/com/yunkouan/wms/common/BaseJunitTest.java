package com.yunkouan.wms.common;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.util.StringUtil;

@SuppressWarnings("rawtypes")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spr-shiro.xml",
	"classpath*:spring/spr-context.xml",
	"classpath*:spring/spring-mvc.xml",
	"classpath*:spring/spr-datasource.xml" })
public class BaseJunitTest implements ApplicationContextAware {

	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;
	private ApplicationContext applicationContext;

	// 执行测试方法之前初始化模拟request,response
	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
	}

	/**
	 * 使用json输出结果
	 * @param list 实体类集合
	 * @throws Exception
	 * @version 2017年3月8日 下午2:07:58<br/>
	 * @author andy wang<br/>
	 */
	protected void formatResult( List list ) throws Exception {
		if ( list == null ) {
			return ;
		}
		for (Object baseEntity : list) {
			this.formatResult(baseEntity);
		}
	}
	
	/**
	 * 使用json输出结果
	 * @param be 实体类
	 * @version 2017年3月1日 下午2:45:05<br/>
	 * @author andy wang<br/>
	 */
	protected void formatResult( Object be ) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		System.out.println(objectMapper.writeValueAsString(be));
	}
	
	
	/**
	 * 格式化显示返回数据
	 * @param rm 返回数据
	 * @version 2017年2月11日 下午10:19:24<br/>
	 * @author andy wang<br/>
	 */
	protected void formatResult(ResultModel rm) {
		StringBuffer sb = new StringBuffer();
		sb.append("**********************************************************************************************"
				+ "**********************************************************************************************\n");
		sb.append(String.format("访问%s\n", (rm.getStatus() == 0 ? "失败" : "成功")));
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			if (rm.isSuccess()) {
				Page page = rm.getPage();
				if (page != null) {
					sb.append(String.format("当前第【%s】页,共【%s】条数据\n", page.getPageNum(), page.getTotal()));
//					for (int i = 0; i < page.size(); i++) {
//						sb.append(String.format("第%s个数据：%s\n", i+1,objectMapper.writeValueAsString(page.get(i))));
//					}
				}
				
				List list = rm.getList();
				if (list != null && !list.isEmpty()) {
					if ( list instanceof Page ) {
						page = (Page) list;
						sb.append(String.format("当前第【%s】页,共【%s】条数据\n", page.getPageNum(), page.getTotal()));
					}
					for (int i = 0; i < list.size(); i++) {
						sb.append(String.format("第%s个数据：%s\n", i+1, objectMapper.writeValueAsString(list.get(i))));
					}
				}
				Object obj = rm.getObj();
				if (obj != null) {
					sb.append(String.format("返回对象：%s\n", objectMapper.writeValueAsString(obj)));
				}
				String message = rm.getMessage();
				if (message != null && message.length() > 0) {
					sb.append(message);
				}
			} else {
				sb.append(rm.getMessage());
			}
			sb.append("#############################################################################################"
					+ "#############################################################################################\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
	}

	/**
	 * 随机中文
	 * @return 随机中文
	 * @throws Exception
	 * @version 2017年2月18日 上午11:25:04<br/>
	 * @author andy wang<br/>
	 */
	public String getChineseName() throws Exception {
		Random ran = new Random();
		int delta = 0x9fa5 - 0x4e00 + 1;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 3; i++) {
			sb.append((char) (0x4e00 + ran.nextInt(delta)));
		}
		return sb.toString();
	}
	
	/**
	 * 对象HibernateValidator的验证
	 * @param obj 需要验证的对象
	 * @param validClass 验证对象
	 * @return 验证结果
	 * @version 2017年2月18日 上午11:25:16<br/>
	 * @author andy wang<br/>
	 */
	public <T> BeanPropertyBindingResult validateEntity(T obj , Class validClass) {
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(obj, obj.getClass().getName());
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> set = validator.validate(obj, validClass);
		Iterator<ConstraintViolation<T>> iterator = set.iterator();
		while (iterator.hasNext()) {
			ConstraintViolation<T> next = iterator.next();
			String valiMessage = next.getMessage().substring(1,next.getMessage().length()-1);
			String message = applicationContext.getMessage(valiMessage, null, Locale.CHINESE);
			message = this.formatParamMessage(message, next);
			ObjectError objectError = new ObjectError(message, message);
			result.addError(objectError);
		}
		return result;
	}
	
	public String formatParamMessage ( String message , ConstraintViolation violation ) {
		ConstraintDescriptor<?> constraintDescriptor = violation.getConstraintDescriptor();
		Map<String, Object> attributes = constraintDescriptor.getAttributes();
		for (String key : attributes.keySet()) {
			if ( "groups".equals(key) || "message".equals(key) ) {
				continue;
			}
			String replaceKey = "{" + key + "}";
			message = message.replace(replaceKey, String.valueOf(attributes.get(key)));
		}
		return message;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
	/**
	 * 对象转Json字符串
	 * @param obj 要转换的对象
	 * @throws Exception
	 * @version 2017年3月20日 下午2:51:52<br/>
	 * @author andy wang<br/>
	 */
	public void toJson ( Object obj , boolean isFormat) throws Exception {
		String json = StringUtil.toJson(obj, isFormat);
		System.out.println(json);
	}
	public void toJson ( Object obj ) throws Exception {
		this.toJson(obj, false);
	}
	
	public static void main(String[] args) {
		String s = "\u7b7e\u540d\u9519\u8bef";
		System.out.println(s);
	}
}
