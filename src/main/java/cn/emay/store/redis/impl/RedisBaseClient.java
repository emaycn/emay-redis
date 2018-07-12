package cn.emay.store.redis.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.store.redis.RedisClient;
import cn.emay.store.redis.command.impl.common.DelCommand;
import cn.emay.store.redis.command.impl.common.ExistsCommand;
import cn.emay.store.redis.command.impl.common.ExpireCommand;
import cn.emay.store.redis.command.impl.common.PersistCommand;
import cn.emay.store.redis.command.impl.common.TtlCommand;
import cn.emay.store.redis.command.impl.hash.HdelCommand;
import cn.emay.store.redis.command.impl.hash.HexistsCommand;
import cn.emay.store.redis.command.impl.hash.HgetAllCommand;
import cn.emay.store.redis.command.impl.hash.HgetCommand;
import cn.emay.store.redis.command.impl.hash.HincrByCommand;
import cn.emay.store.redis.command.impl.hash.HkeysCommand;
import cn.emay.store.redis.command.impl.hash.HlenCommand;
import cn.emay.store.redis.command.impl.hash.HmgetCommand;
import cn.emay.store.redis.command.impl.hash.HmsetCommand;
import cn.emay.store.redis.command.impl.hash.HsetCommand;
import cn.emay.store.redis.command.impl.hash.HsetnxCommand;
import cn.emay.store.redis.command.impl.list.LlenCommand;
import cn.emay.store.redis.command.impl.list.LpopCommand;
import cn.emay.store.redis.command.impl.list.LpushCommand;
import cn.emay.store.redis.command.impl.list.LrangeCommand;
import cn.emay.store.redis.command.impl.list.RpopCommand;
import cn.emay.store.redis.command.impl.list.RpushCommand;
import cn.emay.store.redis.command.impl.set.SaddCommand;
import cn.emay.store.redis.command.impl.set.ScardCommand;
import cn.emay.store.redis.command.impl.set.SismemberCommand;
import cn.emay.store.redis.command.impl.set.SmembersCommand;
import cn.emay.store.redis.command.impl.set.SpopCommand;
import cn.emay.store.redis.command.impl.set.SrandmemberCommand;
import cn.emay.store.redis.command.impl.set.SremCommand;
import cn.emay.store.redis.command.impl.string.DecrByCommand;
import cn.emay.store.redis.command.impl.string.DecrCommand;
import cn.emay.store.redis.command.impl.string.GetCommand;
import cn.emay.store.redis.command.impl.string.IncrByCommand;
import cn.emay.store.redis.command.impl.string.IncrCommand;
import cn.emay.store.redis.command.impl.string.SetCommand;
import cn.emay.store.redis.command.impl.string.SetnxCommand;

/**
 * 基础客户端逻辑
 * 
 * @author Frank
 *
 */
public abstract class RedisBaseClient implements RedisClient {

	/* COMMON */

	@Override
	public boolean exists(final String key) {
		return this.exec(new ExistsCommand(key));
	}

	@Override
	public void del(String... keys) {
		this.exec(new DelCommand(keys));
	}

	@Override
	public void expire(final String key, final int seconds) {
		this.exec(new ExpireCommand(key, seconds));
	}

	@Override
	public long ttl(String key) {
		return this.exec(new TtlCommand(key));
	}

	@Override
	public void persist(String key) {
		this.exec(new PersistCommand(key));
	}

	/* String */

	@Override
	public void set(String key, Object value, int expireTime) {
		this.exec(new SetCommand(key, value, expireTime, getDatePattern()));
	}

	@Override
	public boolean setnx(String key, Object value, int expireTime) {
		return this.exec(new SetnxCommand(key, value, expireTime, getDatePattern()));
	}

	@Override
	public String get(String key) {
		return this.get(key, String.class);
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		return this.exec(new GetCommand<T>(key, clazz, getDatePattern()));
	}

	@Override
	public long decr(String key) {
		return this.exec(new DecrCommand(key));
	}

	@Override
	public long decrBy(String key, long number) {
		return this.exec(new DecrByCommand(key, number));
	}

	@Override
	public long incr(String key) {
		return this.exec(new IncrCommand(key));
	}

	@Override
	public long incrBy(String key, long number) {
		return this.exec(new IncrByCommand(key, number));
	}

	/* HASH */

	@Override
	public void hdel(String key, String... fieldname) {
		this.exec(new HdelCommand(key, fieldname));
	}

	@Override
	public long hlen(String key) {
		return this.exec(new HlenCommand(key));
	}

	@Override
	public boolean hexists(String key, String fieldname) {
		return this.exec(new HexistsCommand(key, fieldname));
	}

	@Override
	public Set<String> hkeys(String key) {
		return this.exec(new HkeysCommand(key));
	}

	@Override
	public long hIncrBy(String key, String fieldname, long number) {
		return this.exec(new HincrByCommand(key, fieldname, number));
	}

	@Override
	public void hset(String key, String fieldname, Object value, int expireTime) {
		this.exec(new HsetCommand(key, fieldname, value, expireTime, getDatePattern()));
	}

	@Override
	public boolean hsetnx(String key, String fieldname, Object value, int expireTime) {
		return this.exec(new HsetnxCommand(key, fieldname, value, expireTime, getDatePattern()));
	}

	@Override
	public void hmset(String key, Map<String, Object> value, int expireTime) {
		this.exec(new HmsetCommand(key, value, expireTime, getDatePattern()));
	}

	@Override
	public String hget(String key, String fieldname) {
		return this.hget(key, fieldname, String.class);
	}

	@Override
	public <T> T hget(String key, String fieldname, Class<T> clazz) {
		return this.exec(new HgetCommand<T>(key, fieldname, clazz, getDatePattern()));
	}

	@Override
	public List<String> hmget(String key, String... fieldnames) {
		return this.hmget(key, String.class, fieldnames);
	}

	@Override
	public <T> List<T> hmget(String key, Class<T> clazz, String... fieldnames) {
		return this.exec(new HmgetCommand<T>(key, clazz, getDatePattern(), fieldnames));
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		return this.hgetAll(key, String.class);
	}

	@Override
	public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
		return this.exec(new HgetAllCommand<T>(key, clazz, getDatePattern()));
	}

	/* LIST */

	@Override
	public long llen(String key) {
		return this.exec(new LlenCommand(key));
	}

	@Override
	public long lpush(String key, int expireTime, Object... objects) {
		return this.exec(new LpushCommand(key, expireTime, getDatePattern(), objects));
	}

	@Override
	public long rpush(String key, int expireTime, Object... objects) {
		return this.exec(new RpushCommand(key, expireTime, getDatePattern(), objects));
	}

	@Override
	public String lpop(String key) {
		return this.lpop(key, String.class);
	}

	@Override
	public <T> T lpop(String key, Class<T> clazz) {
		return this.exec(new LpopCommand<>(key, clazz, getDatePattern()));
	}

	@Override
	public String rpop(String key) {
		return this.rpop(key, String.class);
	}

	@Override
	public <T> T rpop(String key, Class<T> clazz) {
		return this.exec(new RpopCommand<>(key, clazz, getDatePattern()));
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return this.lrange(key, start, end, String.class);
	}

	@Override
	public <T> List<T> lrange(String key, long start, long end, Class<T> clazz) {
		return this.exec(new LrangeCommand<T>(key, start, end, clazz, getDatePattern()));
	}

	/* SET */

	@Override
	public long scard(String key) {
		return this.exec(new ScardCommand(key));
	}

	@Override
	public long sadd(String key, int expireTime, Object... values) {
		return this.exec(new SaddCommand(key, expireTime, getDatePattern(), values));
	}

	@Override
	public String spop(String key) {
		return this.spop(key, String.class);
	}

	@Override
	public <T> T spop(String key, Class<T> clazz) {
		return this.exec(new SpopCommand<T>(key, clazz, getDatePattern()));
	}

	@Override
	public String srandmember(String key) {
		List<String> list = this.srandmember(key, 1, String.class);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	@Override
	public <T> T srandmember(String key, Class<T> clazz) {
		List<T> list = this.srandmember(key, 1, clazz);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<String> srandmember(String key, int count) {
		return this.srandmember(key, count, String.class);
	}

	@Override
	public <T> List<T> srandmember(String key, int count, Class<T> clazz) {
		return this.exec(new SrandmemberCommand<T>(key, clazz, count, getDatePattern()));
	}

	@Override
	public Set<String> smembers(String key) {
		return this.smembers(key, String.class);
	}

	@Override
	public <T> Set<T> smembers(String key, Class<T> clazz) {
		return this.exec(new SmembersCommand<T>(key, clazz, getDatePattern()));
	}

	@Override
	public long srem(String key, Object... members) {
		return this.exec(new SremCommand(key, getDatePattern(), members));
	}

	@Override
	public boolean sismember(String key, Object member) {
		return this.exec(new SismemberCommand(key, member, getDatePattern()));
	}

}
