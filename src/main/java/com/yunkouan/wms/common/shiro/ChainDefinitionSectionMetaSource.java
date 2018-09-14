package com.yunkouan.wms.common.shiro;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.saas.modules.sys.service.IAuthService;
import com.yunkouan.wms.common.constant.Constant;

/**
* @Description: 系统启动时候加载所有权限到系统内存（WMS限2,3级权限）
* @author tphe06
* @date 2017年3月10日
*/
public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {
	protected static Log log = LogFactory.getLog(ChainDefinitionSectionMetaSource.class);

    /**@Fields 权限服务接口 */
	@Autowired
    private IAuthService service;
	/**@Fields 系统根目录，如http://localhost:8080/wms/console */
	private String basePath;
    /**@Fields 加载spr-shiro.xml配置文件中的系统默认值，如/static/** = anon */
    private String filterChainDefinitions;
    /**@Fields 权限正则表达式格式，如${adminPath}/bill/search = perms[bill:search] */
    private static final String PREMISSION_STRING="perms[\"{0}\"]";

    /**
    * @Description: 权限初始化方法【系统只在启动时候初始化一次】
    * @return
    * @throws BeansException
     * @throws ServiceException 
     * @throws DaoException 
     */
    public Section getObject() throws BeansException, DaoException, ServiceException {
    	if(log.isInfoEnabled()) log.info("进入权限初始化方法");
        /**加载默认的url**/
    	Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        /**从数据库查询所有权限【菜单数据，除平台管理级权限外】*/
        List<SysAuth> list = service.query(new SysAuth().setAuthStatus(Constant.STATUS_ACTIVE));
        SysAuth resource;
        if(list != null) for(Iterator<SysAuth> it = list.iterator(); it.hasNext();) {
        	resource = it.next();
            if(resource.getAuthLevel() != null && SysAuth.AUTH_LEVEL_PLATFORM_ADMIN != resource.getAuthLevel() 
            	&& StringUtils.isNotEmpty(resource.getAuthUrl()) && StringUtils.isNotEmpty(resource.getAuthNo())) {
                /**所有【权限URL地址】与 【权限编号】的对应关系
                ${adminPath}/bill/search = perms[bill:search]**/
                section.put(resource.getAuthUrl(),  MessageFormat.format(PREMISSION_STRING, resource.getAuthNo()));
            }
        }
        //Shiro验证URL时,URL匹配成功便不再继续匹配查找(所以要注意配置文件中的URL顺序,尤其在使用通配符时)
        //故filterChainDefinitions的配置顺序为自上而下,以最上面的为准
        /**把项目根目录下的URL地址都受控，如http://localhost:8080/wms/console/ **/
        section.put("/", "anon");
        section.put("/\\#\\!/login", "anon");
        section.put(new StringBuffer("/static/js").append("/**").toString(), "authc");
        section.put(new StringBuffer(basePath).append("/**").toString(), "authc");
//        section.put(new StringBuffer().append("/**").toString(), "authc");
        if(log.isDebugEnabled()) log.debug(section.keySet());
        return section;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public Class<?> getObjectType() {
        return this.getClass();
    }

    public boolean isSingleton() {
        return true;
    }
}