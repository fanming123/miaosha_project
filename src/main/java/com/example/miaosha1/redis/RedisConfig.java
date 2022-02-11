package com.example.miaosha1.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING) //解决注册重复问题
public class RedisConfig {
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.timeout}")
	private int timeout;//秒
	@Value("${spring.redis.lettuce.pool.max-active}")
	private int maxActive;
	@Value("${spring.redis.lettuce.pool.max-idle}")
	private int maxIdle;
	@Value("${spring.redis.lettuce.pool.max-wait}")
	private int maxWait;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	@Override
	public String toString() {
		return "RedisConfig{" +
				"host='" + host + '\'' +
				", port=" + port +
				", timeout=" + timeout +
				", maxActive=" + maxActive +
				", maxIdle=" + maxIdle +
				", maxWait=" + maxWait +
				'}';
	}
}
