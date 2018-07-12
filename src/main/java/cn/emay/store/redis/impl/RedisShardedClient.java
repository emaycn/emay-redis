package cn.emay.store.redis.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.emay.store.redis.RedisClient;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Redis单点客户端
 * 
 * @author Frank
 *
 */
public class RedisShardedClient extends RedisBaseClient implements RedisClient {

	/**
	 * 客户端连接池
	 */
	private ShardedJedisPool jedisPool;

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
	 * @param hosts
	 *            redis分片集群地址
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
	public RedisShardedClient(String hosts,  int timeout, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
		this(hosts, timeout, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
	}

	/**
	 * 实例化并初始化
	 * 
	 * @param hosts
	 *            redis分片集群地址
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
	private RedisShardedClient(String hosts, int timeout, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern) {
		properties = new Properties();
		properties.setProperty("hosts", hosts);
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
	public RedisShardedClient() {

	}

	/**
	 * 初始化
	 */
	public void init() {
		if (jedisPool != null) {
			return;
		}
		datePattern = properties.getProperty("datePattern") == null ? "yyyy-MM-dd HH:mm:ss" : properties.getProperty("datePattern");
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")));
		poolConfig.setMaxTotal(Integer.valueOf(properties.getProperty("maxTotal")));
		poolConfig.setMaxWaitMillis(Long.valueOf(properties.getProperty("maxWaitMillis")));
		poolConfig.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")));
		String hosts = properties.getProperty("hosts");
		int timeout = Integer.valueOf(properties.getProperty("timeout"));
		String[] hostses = hosts.split(",");
		List<JedisShardInfo> infoList = new ArrayList<>();
		for (String host : hostses) {
			host = host.trim();
			if(host.length() == 0) {
				continue;
			}
			String[] ipAndPortArray = host.split(":");
			if (ipAndPortArray.length != 2) {
				throw new RuntimeException("host : " + host + " is error ! ");
			}
			int port;
			String ip = ipAndPortArray[0];
			try {
				port = Integer.valueOf(ipAndPortArray[1]);
			} catch (Exception e) {
				throw new RuntimeException(" port  is must number ! ");
			}
			JedisShardInfo shardInfo = new JedisShardInfo(ip, port, timeout);
			infoList.add(shardInfo);
		}
		jedisPool = new ShardedJedisPool(poolConfig, infoList);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				RedisShardedClient.this.close();
			}
		});
	}

	/**
	 * 获取客户端连接池
	 * 
	 * @return
	 */
	public ShardedJedisPool getClient() {
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
	public <T> T exec(RedisCommand<T> command) {
		ShardedJedis jedis = null;
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
