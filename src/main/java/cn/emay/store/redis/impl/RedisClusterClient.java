package cn.emay.store.redis.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import cn.emay.store.redis.RedisClient;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * Redis集群客户端
 * 
 * @author Frank
 *
 */
public class RedisClusterClient extends RedisBaseClient implements RedisClient {

	/**
	 * 集群客户端
	 */
	private JedisCluster jedisCluster;

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
	 *            集群节点地址，ip:port,ip:port,
	 * @param timeout
	 *            超时时间
	 * @param maxRedirections
	 *            最大寻址次数
	 * @param maxIdle
	 *            最大空闲线程数
	 * @param maxTotal
	 *            最大线程数
	 * @param minIdle
	 *            最小空闲线程数
	 * @param maxWaitMillis
	 *            最大等待时间
	 */
	public RedisClusterClient(String hosts, int timeout, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
		this(hosts, timeout, maxRedirections, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
	}

	/**
	 * 实例化并初始化
	 * 
	 * @param hosts
	 *            集群节点地址，ip:port,ip:port,
	 * @param timeout
	 *            超时时间
	 * @param maxRedirections
	 *            最大寻址次数
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
	public RedisClusterClient(String hosts, int timeout, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern) {
		properties = new Properties();
		properties.setProperty("hosts", hosts);
		properties.setProperty("maxRedirections", String.valueOf(maxRedirections));
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
	public RedisClusterClient() {

	}

	/**
	 * 初始化
	 */
	public void init() {
		if (jedisCluster != null) {
			return;
		}
		datePattern = properties.getProperty("datePattern") == null ? "yyyy-MM-dd HH:mm:ss" : properties.getProperty("datePattern");
		String hosts = properties.getProperty("hosts");
		int maxRedirections = Integer.valueOf(properties.getProperty("maxRedirections"));
		int timeout = Integer.valueOf(properties.getProperty("timeout"));
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")));
		poolConfig.setMaxTotal(Integer.valueOf(properties.getProperty("maxTotal")));
		poolConfig.setMaxWaitMillis(Long.valueOf(properties.getProperty("maxWaitMillis")));
		poolConfig.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")));
		Set<HostAndPort> set = new HashSet<HostAndPort>();
		String[] hostses = hosts.split(",");
		for (String host : hostses) {
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
			set.add(new HostAndPort(ip, port));
		}
		jedisCluster = new JedisCluster(set, timeout, maxRedirections, poolConfig);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				RedisClusterClient.this.close();
			}
		});
	}

	/**
	 * 获取集群客户端
	 * 
	 * @return
	 */
	public JedisCluster getClient() {
		return jedisCluster;
	}

	/**
	 * 关闭客户端
	 */
	public void close() {
		try {
			jedisCluster.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		try {
			return command.commond(getClient());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDatePattern() {
		return datePattern;
	}

}
