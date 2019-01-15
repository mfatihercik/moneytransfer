package com.mfatihercik.moneytransfer.core;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allow us to use Inversion of Control(IoC) pattern in the application.
 * 
 * It is ThreadLocal object that  holds application and request wide object.  
 * 
 * it bound to every request thread
 * 
 *  
 * Objects are registered with their class name. 
 * 
 *  
 * @author fatih.ercik
 *
 */
public class ApplicationContext {

	public static ThreadLocal<ApplicationContext> _thread = new ThreadLocal<>();

	Map<String, Object> context = new HashMap<>();

	private ApplicationContext() {

	}

	/**
	 * register object with their class name
	 * @param object
	 */
	public static void register(Object object) {
		ApplicationContext.register(object.getClass(), object);
	}

	public static void register(Class name, Object object) {
		ApplicationContext.getInstance().registerBean(name.getName(), object);
	}
	public static void register(String name, Object object) {
		ApplicationContext.getInstance().registerBean(name, object);
	}

	public void registerBean(String name, Object object) {
		context.put(name, object);
	}

	public static <T> T get(Class<T> clazz) {
		return (T) ApplicationContext.getInstance().getBean(clazz);
	}

	public <T> T getBean(Class<T> clazz) {
		return (T) context.get(clazz.getName());
	}

	public static ApplicationContext getInstance() {
		if (_thread.get() == null) {
			_thread.set(new ApplicationContext());
		}
		return _thread.get();
	}

	public static void setInstance(ApplicationContext instance) {
		_thread.set(instance);
	}

	public static void removetInstance() {
		_thread.remove();
	}

}
