package cn.emay.store.redis;

import cn.emay.store.redis.command.RedisCommand;
import cn.emay.store.redis.define.RedisCommon;
import cn.emay.store.redis.define.RedisHash;
import cn.emay.store.redis.define.RedisList;
import cn.emay.store.redis.define.RedisSet;
import cn.emay.store.redis.define.RedisSortedSet;
import cn.emay.store.redis.define.RedisString;

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