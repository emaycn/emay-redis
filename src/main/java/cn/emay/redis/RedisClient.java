package cn.emay.redis;

import cn.emay.redis.command.RedisCommand;
import cn.emay.redis.define.RedisCommon;
import cn.emay.redis.define.RedisHash;
import cn.emay.redis.define.RedisList;
import cn.emay.redis.define.RedisSet;
import cn.emay.redis.define.RedisSortedSet;
import cn.emay.redis.define.RedisString;

/**
 * Redis 客户端
 * 
 * @author Frank
 *
 */
public interface RedisClient extends RedisCommon, RedisString, RedisHash, RedisList, RedisSet, RedisSortedSet {

	/**
	 * 执行
	 * 
	 * @param command
	 * @return
	 */
	public <T> T execCommand(RedisCommand<T> command);

	/**
	 * 获取日期处理格式
	 * 
	 * @return
	 */
	public String getDatePattern();

}