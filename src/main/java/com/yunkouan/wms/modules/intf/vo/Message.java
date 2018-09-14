package com.yunkouan.wms.modules.intf.vo;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.yunkouan.base.BaseObj;
import com.yunkouan.valid.ValidSearch;

public class Message extends BaseObj {
	/**serialVersionUID:**/
	private static final long serialVersionUID = 1113278236468076914L;

	/**sign:签名数据**/
	@Length(min=16, max=64, message="{valid_extintf_sign_length}", groups={ValidSearch.class})
	@NotNull(message="{valid_extintf_sign_notnull}", groups={ValidSearch.class})
	private String sign;

	/**notify_type:接口类型**/
	@Length(min=2, max=2, message="{valid_extintf_notify_type_length}",groups={ValidSearch.class})
	@NotNull(message="{valid_extintf_notify_type_notnull}",groups={ValidSearch.class})
	private String notify_type;

	/**notify_id:请求唯一标识**/
	private String notify_id;
	/**notify_time:请求时间**/
	private String notify_time;

	/**data:报文内容，JSON格式**/
	private String data;

	public String getSign() {
		return sign;
	}
	public String getNotify_type() {
		return notify_type;
	}
	public String getNotify_id() {
		return notify_id;
	}
	public String getNotify_time() {
		return notify_time;
	}
	public String getData() {
		return data;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}
	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}
	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}
	public void setData(String data) {
		this.data = data;
	}
}