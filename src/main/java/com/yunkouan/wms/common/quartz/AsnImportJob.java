package com.yunkouan.wms.common.quartz;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.FileUtil;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.UsernamePasswordToken;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.service.IASNService;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/** 
* @Description: 收货单导入
* @author tphe06
* @date 2018年8月24日 下午3:36:28  
*/
@DisallowConcurrentExecution
public class AsnImportJob extends QuartzJobBean {
	private static Log log = LogFactory.getLog(AsnImportJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			//备份文件目录
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			String backPath = paramService.getKey(CacheName.ASN_IMPORT_BACK);
			//帐号登录
			UsernamePasswordToken token = new UsernamePasswordToken();
			token.setFrom("3");
			token.setLoginType(false);
			token.setOrgId("000001");//企业编号（XML配置）
			token.setPassword("111111".toCharArray());//登录密码（XML配置）
			token.setUsername("000001");//登录帐号（XML配置）
			//读取共享文件夹文件列表
			SmbFile[] files = read();
			if(files == null || files.length == 0) return;
			for(SmbFile file : files) {
				//解析XML报文
				List<RecAsnVO> list = parseXml(file);
				if(list == null || list.isEmpty()) break;
				//导入ASN单列表
				upload(list);
				//文件备份删除
				boolean result = FileUtil.smbGet(file.getPath(), backPath+"/"+file.getName());
				if(result) file.delete();
			}
		} catch (Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
		}
	}

	private static void upload(List<RecAsnVO> listImportAsn) throws Exception {
		//批量导入接口
		IASNService asnService = SpringContextHolder.getBean(IASNService.class);
		asnService.upload(listImportAsn);
	}

	private static SmbFile[] read() throws MalformedURLException, SmbException {
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		//文件夹路径
		String docPath = paramService.getKey(CacheName.ASN_IMPORT_PATH);
		String backPath = paramService.getKey(CacheName.ASN_IMPORT_BACK);
		File doc = new File(backPath);
		if(!doc.exists()) doc.mkdirs();
		//读取文件
		SmbFile[] files = FileUtil.listFiles(docPath, "^[a-zA-Z0-9]*.xml$");
		return files;
	}

	private static List<RecAsnVO> parseXml(SmbFile file) throws IOException {
		//解析文件
		String xml = FileUtil.smbRead(file);
		List<RecAsnVO> list = fromXML(xml);
		return list;
	}

	/** 
	* @Title: fromXML 
	* @Description: 把XML字符串转换成消息对象
	* @auth tphe06
	* @time 2018 2018年7月26日 下午1:38:57
	* @param xml
	* @return
	* RequestMessage
	*/
	@SuppressWarnings("unchecked")
	public static List<RecAsnVO> fromXML(String xml) {
		XStream x = new XStream(new XppDriver(new XmlFriendlyReplacer("-_", "_")));
		x.alias("RecAsnVO", RecAsnVO.class);
		x.alias("RecAsn", RecAsn.class);
		x.alias("RecAsnDetail", RecAsnDetail.class);
		x.alias("MetaMerchant", MetaMerchant.class);
		return (List<RecAsnVO>)x.fromXML(xml);
	}

	/** 
	* @Title: toXML 
	* @Description: 把消息对象转化成XML字符串
	* @auth tphe06
	* @time 2018 2018年7月26日 上午10:33:07
	* @param message
	* @return
	* String
	*/
	public static String toXML(List<RecAsnVO> message) {
		XStream x = new XStream(new XppDriver(new XmlFriendlyReplacer("-_", "_")));
		x.alias("RecAsnVO", RecAsnVO.class);
		x.alias("RecAsn", RecAsn.class);
		x.alias("RecAsnDetail", RecAsnDetail.class);
		x.alias("MetaMerchant", MetaMerchant.class);
		String xml = x.toXML(message);
		return xml;
	}
}