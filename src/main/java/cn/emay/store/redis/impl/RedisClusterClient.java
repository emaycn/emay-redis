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
public class RedisClusterClient extends RedisBaseClient<JedisCluster> implements RedisClient {

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
	 *            集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
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
	public RedisClusterClient(String hosts, int timeoutMillis, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
		this(hosts, timeoutMillis, maxRedirections, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
	}

	/**
	 * 实例化并初始化
	 * 
	 * @param hosts
	 *            集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
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
	public RedisClusterClient(String hosts, int timeoutMillis, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern) {
		properties = new Properties();
		properties.setProperty("hosts", hosts);
		properties.setProperty("maxRedirections", String.valueOf(maxRedirections));
		properties.setProperty("timeout", String.valueOf(timeoutMillis));
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

	@Override
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
		for (String hostitem : hostses) {
			String[] hosten = hostitem.split(";");
			for (String host : hosten) {
				host = host.trim();
				if (host.length() == 0) {
					continue;
				}
				String[] ipAndPortArray = host.split(":");
				if (ipAndPortArray.length != 2) {
					throw new RuntimeException("host : " + host + " is error ! ");
				}
				int port;
				String ip = ipAndPortArray[0].trim();
				try {
					port = Integer.valueOf(ipAndPortArray[1].trim());
				} catch (Exception e) {
					throw new RuntimeException(" port  is must number ! ");
				}
				set.add(new HostAndPort(ip, port));
			}
		}
		jedisCluster = new JedisCluster(set, timeout, maxRedirections, poolConfig);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				RedisClusterClient.this.close();
			}
		});
	}

	@Override
	public JedisCluster getClient() {
		return jedisCluster;
	}

	@Override
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

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/* BASE */

	@Override
	public <T> T execCommand(RedisCommand<T> command) {
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
