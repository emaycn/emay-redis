package cn.emay.store.redis.impl;

import java.util.Properties;

import cn.emay.store.redis.RedisClient;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis单点客户端
 * 
 * @author Frank
 *
 */
public class RedisSingleClient extends RedisBaseClient implements RedisClient {

	/**
	 * 客户端连接池
	 */
	private JedisPool jedisPool;

	/**
	 * Json转换日期格式
	 */
	private String datePattern;

	/**
	 * 参数
	 */
	private Properties properties;

	/**
	 * 实例化并初始化
	 * 
	 * @param host
	 *            redis地址
	 * @param port
	 *            redis端口
	 * @param timeout
	 *            超时时间
	 * @param maxIdle
	 *            最大空闲线程数
	 * @param maxTotal
	 *            最大线程数
	 * @param minIdle
	 *            最小空闲线程数
	 * @param maxWaitMillis
	 *            最大等待时间
	 */
	public RedisSingleClient(String host, int port, int timeout, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
		this(host, port, timeout, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
	}

	/**
	 * 实例化并初始化
	 * 
	 * @param host
	 *            redis地址
	 * @param port
	 *            redis端口
	 * @param timeout
	 *            超时时间
	 * @param maxIdle
	 *            最大空闲线程数
	 * @param maxTotal
	 *            最大线程数
	 * @param minIdle
	 *            最小空闲线程数
	 * @param maxWaitMillis
	 *            最大等待时间
	 * @param datePattern
	 *            Json转换日期格式
	 */
	private RedisSingleClient(String host, int port, int timeout, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern) {
		properties = new Properties();
		properties.setProperty("host", host);
		properties.setProperty("port", String.valueOf(port));
		properties.setProperty("timeout", String.valueOf(timeout));
		properties.setProperty("maxIdle", String.valueOf(maxIdle));
		properties.setProperty("maxTotal", String.valueOf(maxTotal));
		properties.setProperty("minIdle", String.valueOf(minIdle));
		properties.setProperty("maxWaitMillis", String.valueOf(maxWaitMillis));
		properties.setProperty("datePattern", datePattern == null ? "yyyy-MM-dd HH:mm:ss" : datePattern);
		this.init();
	}

	/**
	 * 实例化并不初始化，需要setproperties()并调用init()主动初始化
	 */
	public RedisSingleClient() {

	}

	/**
	 * 初始化
	 */
	public void init() {
		if (jedisPool != null) {
			return;
		}
		datePattern = properties.getProperty("datePattern") == null ? "yyyy-MM-dd HH:mm:ss" : properties.getProperty("datePattern");
		String host = properties.getProperty("host");
		int port = Integer.valueOf(properties.getProperty("port"));
		int timeout = Integer.valueOf(properties.getProperty("timeout"));
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")));
		poolConfig.setMaxTotal(Integer.valueOf(properties.getProperty("maxTotal")));
		poolConfig.setMaxWaitMillis(Long.valueOf(properties.getProperty("maxWaitMillis")));
		poolConfig.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")));
		jedisPool = new JedisPool(poolConfig, host, port, timeout);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				RedisSingleClient.this.close();
			}
		});
	}

	/**
	 * 获取客户端连接池
	 * 
	 * @return
	 */
	public JedisPool getClient() {
		return jedisPool;
	}

	/**
	 * 关闭客户端
	 */
	public void close() {
		jedisPool.close();
	}
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/* BASE */

	@Override
	public <T> T execCommand(RedisCommand<T> command) {
		Jedis jedis = null;
		try {
			jedis = getClient().getResource();
			return command.commond(jedis);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public String getDatePattern() {
		return datePattern;
	}

}
