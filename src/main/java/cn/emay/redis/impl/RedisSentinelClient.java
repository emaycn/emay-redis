
package cn.emay.redis.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import cn.emay.redis.RedisClient;
import cn.emay.redis.command.PipelineCommand;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;

/**
 * 哨兵客户端
 *
 * @author Hcs
 * @create 2020/3/24
 */
public class RedisSentinelClient extends BaseRedisClient<JedisSentinelPool> implements RedisClient {

	/**
	 * 客户端连接池
	 */
	private JedisSentinelPool jedisPool;

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
	 * @param masterName    主节点名称
	 * @param sentinels     集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
	 * @param maxIdle       最大空闲线程数
	 * @param maxTotal      最大线程数
	 * @param minIdle       最小空闲线程数
	 * @param maxWaitMillis 最大等待时间
	 */
	public RedisSentinelClient(String masterName, String sentinels, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
		this(masterName, sentinels, timeoutMillis, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
	}

	/**
	 * 实例化并初始化
	 * 
	 * @param masterName    主节点名称
	 * @param sentinels     集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
	 * @param maxIdle       最大空闲线程数
	 * @param maxTotal      最大线程数
	 * @param minIdle       最小空闲线程数
	 * @param maxWaitMillis 最大等待时间
	 * @param datePattern   Json转换日期格式
	 */
	public RedisSentinelClient(String masterName, String sentinels, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis,
			String datePattern) {
		this(masterName, sentinels, timeoutMillis, maxIdle, maxTotal, minIdle, maxWaitMillis, datePattern, null);
	}

	/**
	 * 实例化并初始化
	 * 
	 * @param masterName    主节点名称
	 * @param sentinels     集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
	 * @param maxIdle       最大空闲线程数
	 * @param maxTotal      最大线程数
	 * @param minIdle       最小空闲线程数
	 * @param maxWaitMillis 最大等待时间
	 * @param datePattern   Json转换日期格式
	 * @param password      密码(非必填)
	 */
	public RedisSentinelClient(String masterName, String sentinels, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis,
			String datePattern, String password) {
		properties = new Properties();
		properties.setProperty("masterName", masterName);
		properties.setProperty("sentinels", sentinels);
		if (password != null) {
			properties.setProperty("password", password);
		}
		properties.setProperty("timeout", String.valueOf(timeoutMillis));
		properties.setProperty("maxIdle", String.valueOf(maxIdle));
		properties.setProperty("maxTotal", String.valueOf(maxTotal));
		properties.setProperty("minIdle", String.valueOf(minIdle));
		properties.setProperty("maxWaitMillis", String.valueOf(maxWaitMillis));
		properties.setProperty("datePattern", datePattern == null ? "yyyy-MM-dd HH:mm:ss" : datePattern);
		this.init();
	}

	@Override
	public void init() {
		if (jedisPool != null) {
			return;
		}
		datePattern = properties.getProperty("datePattern") == null ? "yyyy-MM-dd HH:mm:ss" : properties.getProperty("datePattern");
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));
		poolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
		poolConfig.setMaxWaitMillis(Long.parseLong(properties.getProperty("maxWaitMillis")));
		poolConfig.setMinIdle(Integer.parseInt(properties.getProperty("minIdle")));

		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTimeMillis(60000);
		poolConfig.setTimeBetweenEvictionRunsMillis(30000);
		poolConfig.setNumTestsPerEvictionRun(-1);

		String masterName = properties.getProperty("masterName");
		String password = properties.getProperty("password");
		String sentinels = properties.getProperty("sentinels");
		int timeout = Integer.parseInt(properties.getProperty("timeout"));

		Set<String> sentinelSet = new HashSet<String>();
		String[] hostses = sentinels.split(",");
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
				sentinelSet.add(ip + ":" + port);
			}
		}

		jedisPool = new JedisSentinelPool(masterName, sentinelSet, poolConfig, timeout, password);
	}

	@Override
	public JedisSentinelPool getClient() {
		return jedisPool;
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

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
	public void close() {
		jedisPool.close();
	}

	@Override
	public String getDatePattern() {
		return datePattern;
	}

	@Override
	public List<Object> execPipelineCommand(PipelineCommand pipelineCommand) {
		Jedis jedis = null;
		Pipeline pipeline = null;
		try {
			jedis = getClient().getResource();
			pipeline = jedis.pipelined();
			pipelineCommand.commond(pipeline);
			return pipelineCommand.getResult(pipeline);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (pipeline != null) {
				try {
					pipeline.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (jedis != null) {
				jedis.close();
			}
		}
	}

}
