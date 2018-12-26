package com.zhuxun.dc.apirunner.config;


import com.zhuxun.dc.apirunner.entity.TokenEntity;
import com.zhuxun.dc.apirunner.shiro.SessionStorageEvaluator;
import com.zhuxun.dc.apirunner.shiro.StatelessDefaultSubjectFactory;
import com.zhuxun.dc.apirunner.shiro.filter.StatelessAuthcFilter;
import com.zhuxun.dc.apirunner.shiro.realm.JwtAuthenticationRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.Filter;
import java.util.Map;

/**
 * 权限相关配置
 *
 * @author 周涛
 */
@Configuration
@Slf4j
public class ShiroConfig {

  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  @DependsOn("lifecycleBeanPostProcessor")
  public JwtAuthenticationRealm jwtAuthenticationRealm() {
    JwtAuthenticationRealm realm = new JwtAuthenticationRealm();
    // TODO: WORKAROUND: see Application.dataSource
    // realm.setSecret(this.dcConfig.getJwtSecret());
    realm.setCacheManager(cacheManager());
    return realm;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager());
    return advisor;
  }

  @Bean
  public SubjectFactory subjectFactory() {
    return new StatelessDefaultSubjectFactory();
  }

  @Bean
  @RequestScope
  public Subject subject() {
    return SecurityUtils.getSubject();
  }

  @Bean
  @RequestScope
  public TokenEntity tokenEntity() {
    Subject subject = this.subject();
    if (subject != null) {
      return (TokenEntity) subject.getPrincipal();
    } else {
      return null;
    }
  }

  @Bean
  public SessionManager sessionManager() {
    DefaultSessionManager sessionManager = new DefaultSessionManager();
    sessionManager.setSessionValidationSchedulerEnabled(false);
    return sessionManager;
  }

  @Bean
  public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(jwtAuthenticationRealm());
    securityManager.setSubjectFactory(subjectFactory());
    securityManager.setSessionManager(sessionManager());
    securityManager.setCacheManager(cacheManager());

    DefaultSubjectDAO subjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
    subjectDAO.setSessionStorageEvaluator(new SessionStorageEvaluator());

    return securityManager;
  }

  @Bean
  public MethodInvokingFactoryBean methodInvokingFactoryBean() {
    MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
    methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
    methodInvokingFactoryBean.setArguments(new Object[] {securityManager()});
    return methodInvokingFactoryBean;
  }

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean() {
    ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
    factoryBean.setSecurityManager(securityManager());

    Map<String, Filter> filterMap = new HashedMap();
    filterMap.put("statelessAuthc", statelessAuthcFilter());
    factoryBean.setFilters(filterMap);

    Map<String, String> definitionMap = new HashedMap();
    definitionMap.put("/**", "statelessAuthc");

    factoryBean.setFilterChainDefinitionMap(definitionMap);
    return factoryBean;
  }

  @Bean
  public CacheManager cacheManager() {
    return new MemoryConstrainedCacheManager();
  }

  @Bean
  public Filter statelessAuthcFilter() {
    StatelessAuthcFilter statelessAuthcFilter = new StatelessAuthcFilter();
    return statelessAuthcFilter;
  }
}
