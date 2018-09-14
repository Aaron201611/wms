package com.yunkouan.wms.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;
import ionic.Msmq.TransactionType;
//import ionic.Msmq.TransactionType;

/**
 * @function MSMQ 接口，需要把MsmqJava64.dll文件拷贝到c:\windows\system32目录下，并且在服务器上安装微软的消息队列，否则报缺失依赖库错误。
 * 如果你不小心给FormatName这个属性赋值
   MyMachineHostName /private$/yourqname，那就会得到
   MQ_ERROR_ILLEGAL_FORMATNAME (C00E001E)这个错误。
       同样，如果你不小心给PathName这个属性赋值
   direct=tcp:172.xx.xx.xx/private$/YourMQName，那就会得到
   MQ_ERROR_ILLEGAL_PATHNAME (C00E0014)这个错误。
       这两个属性真的不一样啊，不要搞混了。
 * @author tphe06
 */
public class MSMQUtil {
	private static Log log = LogFactory.getLog(MSMQUtil.class);

	/**
	 * 创建消息队列
	 * @param queuePath 队列完整路径和名称
	 * @param queueLabel 消息标题
	 * @param isTransactional 是否事物
	 * @return
	 */
	public static boolean create(String queuePath, String queueLabel, boolean isTransactional) {
		try {
			Queue q = Queue.create(queuePath, queueLabel, isTransactional);
			if(q != null) return true;
		} catch (MessageQueueException e) {
			e.printStackTrace();
			if(log.isErrorEnabled()) log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 发送一条消息到消息队列
	 * @param fullname 队列完整路径和名称
	 * @param label 消息标题
	 * @param body 消息内容
	 * @param correlationId 关联消息id，用作分段消息用？
	 * @return
	 */
	public static boolean send(String fullname, String label, String body, byte[] correlationId) {
		try {
			 //192.32.12.76为本机的IP地址（经测试不能使用127.0.0.1,不然会报错）
			 //private$\\myqueue是队列名字
//			 String fullname = "direct=tcp:192.168.1.115\\private$\\myqueue";
		     Queue queue = new Queue(fullname, 2);
		     String format = queue.getFormatName();
		     if(log.isDebugEnabled()) log.debug("format:"+format);
		     String label1 = queue.getLabel();
		     if(log.isDebugEnabled()) log.debug("label1:"+label1);
		     boolean isTran = queue.isTransactional();
		     if(log.isDebugEnabled()) log.debug("isTran:"+isTran);
		     //消息标题
//		     String label="testmessage";
		     //消息内容
//		     String body= "Hello, World!";
//		     byte[] correlationId = { 0,2,4,6,8,9 };
		     Message msg = new Message(body, label, correlationId);
		     queue.send(msg, true, TransactionType.SINGLE_MESSAGE);
		     return true;
		 } catch (MessageQueueException | UnsupportedEncodingException e) {
			 e.printStackTrace();
			 if(log.isErrorEnabled()) log.error(e.getMessage());
		 }
		 return false;
	}

	/**
	 * 读取消息队列里的一条消息
	 * @param fullname 队列完整路径和名称
	 * @return
	 */
	public static String rec(String fullname) {
		try { 
			//192.32.12.76为本机的IP地址（经测试不能使用127.0.0.1,不然会报错）
			//private$\\myqueue是队列名字
//			String fullname = "direct=tcp:192.32.12.76\\private$\\myqueue";
		     Queue queue = new Queue(fullname);
		     Message message = queue.receive();
		     
		     return message.getBodyAsString();
		 } catch (MessageQueueException | UnsupportedEncodingException e) {
		     e.printStackTrace();
		     if(log.isErrorEnabled()) log.error(e.getMessage());
		 }
		return null;
	}
	
	/**
	 * 读取消息对了的一条消息，根据编码格式返回消息
	 * @param fullname
	 * @param format
	 * @return
	 */
	public static String rec(String fullname,String format){
		 try {
			Queue queue = new Queue(fullname);
			 Message message = queue.receive();
			 return message.getBodyAsString(format);
		} catch (Throwable e) {
		     if(log.isDebugEnabled()) log.debug(e.getMessage());
		}
		 return null;
	}

	public static void main(String[] args) {
//		System.out.println(System.getProperty("java.library.path"));
//		send("direct=tcp:192.168.1.115\\private$\\myqueue", "wms_message", "Hello!", "12345678".getBytes());
		String body = "<HCHX_DATA><TRANSMIT><MESSAGE_ID>35DB6B07DCFC5FADE055000000000222</MESSAGE_ID><MESSAGE_TYPE>4001</MESSAGE_TYPE><EMS_NO>H80131705393</EMS_NO><ORDER_NO /><FUNCTION_CODE>R</FUNCTION_CODE><CHK_RESULT>A000</CHK_RESULT><MESSAGE_DATE>2017-11-15T15:21:02</MESSAGE_DATE><SENDER_ID>5006640002</SENDER_ID><SEND_ADDRESS /><RECEIVER_ID>8000</RECEIVER_ID><RECEIVER_ADDRESS /><MESSAGE_SIGN>5006640002</MESSAGE_SIGN><SEND_TYPE>0</SEND_TYPE></TRANSMIT><RESULT_LIST><MESSAGE_ID>35DB6B07DCFC5FADE055000000000222</MESSAGE_ID><MESSAGE_TYPE>4001</MESSAGE_TYPE><EMS_NO>H80131705393</EMS_NO><COP_G_NO>LH20</COP_G_NO><ORDER_NO /><RESULT_INFO>账册号:H80131705393,进出库类型,校验失败</RESULT_INFO><RESULT_INFO1 /><RESULT_INFO2 /></RESULT_LIST></HCHX_DATA>";
		String label = "wms";
		String messageId = "647D10F3A4C44216A42C08EA6C313788";
		try {
			Message message = new Message(body,label,messageId.getBytes());
			System.out.println("xml ===="+message.getBodyAsString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}