package com.yunkouan.wms.common.config;

public class RedisConfig {
	public static String host;
	public static String port;
	public static String password;
	public static String db;
	
	public static String maxIdle;
	public static String maxActive;
	public static String maxWait;
	public static String testOnBorrow;
	public static String timeout;

	public static String getHost() {
		return host;
	}
	public static void setHost(String host) {
		RedisConfig.host = host;
	}
	public static String getPort() {
		return port;
	}
	public static void setPort(String port) {
		RedisConfig.port = port;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		RedisConfig.password = password;
	}
	public static String getDb() {
		return db;
	}
	public static void setDb(String db) {
		RedisConfig.db = db;
	}
	public static String getMaxIdle() {
		return maxIdle;
	}
	public static void setMaxIdle(String maxIdle) {
		RedisConfig.maxIdle = maxIdle;
	}
	public static String getMaxActive() {
		return maxActive;
	}
	public static void setMaxActive(String maxActive) {
		RedisConfig.maxActive = maxActive;
	}
	public static String getMaxWait() {
		return maxWait;
	}
	public static void setMaxWait(String maxWait) {
		RedisConfig.maxWait = maxWait;
	}
	public static String getTestOnBorrow() {
		return testOnBorrow;
	}
	public static void setTestOnBorrow(String testOnBorrow) {
		RedisConfig.testOnBorrow = testOnBorrow;
	}
	public static String getTimeout() {
		return timeout;
	}
	public static void setTimeout(String timeout) {
		RedisConfig.timeout = timeout;
	}
}