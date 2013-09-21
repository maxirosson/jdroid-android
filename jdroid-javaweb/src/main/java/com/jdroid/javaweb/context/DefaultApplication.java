package com.jdroid.javaweb.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import com.jdroid.javaweb.domain.Entity;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public class DefaultApplication<T extends Entity> implements ApplicationContextAware {
	
	private static DefaultApplication<?> INSTANCE;
	
	public DefaultApplication() {
		INSTANCE = this;
	}
	
	public static DefaultApplication<?> get() {
		return INSTANCE;
	}
	
	private DefaultApplicationContext applicationContext;
	private SecurityContextHolder<T> securityContextHolder;
	private org.springframework.context.ApplicationContext springApplicationContext;
	
	public DefaultApplicationContext getDefaultApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * @return The {@link AbstractSecurityContext} instance
	 */
	public AbstractSecurityContext<T> getSecurityContext() {
		return securityContextHolder != null ? securityContextHolder.getContext() : null;
	}
	
	/**
	 * @param beanName The bean name
	 * @return The spring bean with the bean name
	 */
	public Object getBean(String beanName) {
		return springApplicationContext.getBean(beanName);
	}
	
	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		this.springApplicationContext = applicationContext;
	}
	
	/**
	 * @param securityContextHolder The {@link SecurityContextHolder} to set
	 */
	public void setSecurityContextHolder(SecurityContextHolder<T> securityContextHolder) {
		this.securityContextHolder = securityContextHolder;
	}
	
	/**
	 * @param applicationContext the applicationContext to set
	 */
	public void setApplicationContext(DefaultApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
