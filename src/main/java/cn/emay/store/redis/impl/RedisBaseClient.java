package cn.emay.store.redis.impl;

import java.util.HashSet;
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
import cn.emay.store.redis.command.impl.sortedset.ZaddCommand;
import cn.emay.store.redis.command.impl.sortedset.ZaddCommandAll;
import cn.emay.store.redis.command.impl.sortedset.ZcardCommand;
import cn.emay.store.redis.command.impl.sortedset.ZcountCommand;
import cn.emay.store.redis.command.impl.sortedset.ZrangeByScoreCommand;
import cn.emay.store.redis.command.impl.sortedset.ZrangeByScoreWithOffsetCommand;
import cn.emay.store.redis.command.impl.sortedset.ZrangeCommand;
import cn.emay.store.redis.command.impl.sortedset.ZrankCommand;
import cn.emay.store.redis.command.impl.sortedset.ZremCommand;
import cn.emay.store.redis.command.impl.sortedset.ZremerangeByRankCommand;
import cn.emay.store.redis.command.impl.sortedset.ZremerangeByScoreCommand;
import cn.emay.store.redis.command.impl.sortedset.ZrevrangeByScoreCommand;
import cn.emay.store.redis.command.impl.sortedset.ZrevrangeByScoreWithOffsetCommand;
import cn.emay.store.redis.command.impl.sortedset.ZrevrangeCommand;
import cn.emay.store.redis.command.impl.sortedset.ZrevrankCommand;
import cn.emay.store.redis.command.impl.sortedset.ZscoreCommand;
import cn.emay.store.redis.command.impl.string.DecrByCommand;
import cn.emay.store.redis.command.impl.string.DecrCommand;
import cn.emay.store.redis.command.impl.string.GetCommand;
import cn.emay.store.redis.command.impl.string.IncrByCommand;
import cn.emay.store.redis.command.impl.string.IncrCommand;
import cn.emay.store.redis.command.impl.string.SetCommand;
import cn.emay.store.redis.command.impl.string.SetnxCommand;
import redis.clients.jedis.Tuple;

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
		return this.execCommand(new ExistsCommand(key));
	}

	@Override
	public void del(String... keys) {
		this.execCommand(new DelCommand(keys));
	}

	@Override
	public void expire(final String key, final int seconds) {
		this.execCommand(new ExpireCommand(key, seconds));
	}

	@Override
	public long ttl(String key) {
		return this.execCommand(new TtlCommand(key));
	}

	@Override
	public void persist(String key) {
		this.execCommand(new PersistCommand(key));
	}

	/* String */

	@Override
	public void set(String key, Object value, int expireTime) {
		this.execCommand(new SetCommand(key, value, expireTime, getDatePattern()));
	}

	@Override
	public boolean setnx(String key, Object value, int expireTime) {
		return this.execCommand(new SetnxCommand(key, value, expireTime, getDatePattern()));
	}

	@Override
	public String get(String key) {
		return this.get(key, String.class);
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		return this.execCommand(new GetCommand<T>(key, clazz, getDatePattern()));
	}

	@Override
	public long decr(String key) {
		return this.execCommand(new DecrCommand(key));
	}

	@Override
	public long decrBy(String key, long number) {
		return this.execCommand(new DecrByCommand(key, number));
	}

	@Override
	public long incr(String key) {
		return this.execCommand(new IncrCommand(key));
	}

	@Override
	public long incrBy(String key, long number) {
		return this.execCommand(new IncrByCommand(key, number));
	}

	/* HASH */

	@Override
	public void hdel(String key, String... fieldname) {
		this.execCommand(new HdelCommand(key, fieldname));
	}

	@Override
	public long hlen(String key) {
		return this.execCommand(new HlenCommand(key));
	}

	@Override
	public boolean hexists(String key, String fieldname) {
		return this.execCommand(new HexistsCommand(key, fieldname));
	}

	@Override
	public Set<String> hkeys(String key) {
		return this.execCommand(new HkeysCommand(key));
	}

	@Override
	public long hIncrBy(String key, String fieldname, long number) {
		return this.execCommand(new HincrByCommand(key, fieldname, number));
	}

	@Override
	public void hset(String key, String fieldname, Object value, int expireTime) {
		this.execCommand(new HsetCommand(key, fieldname, value, expireTime, getDatePattern()));
	}

	@Override
	public boolean hsetnx(String key, String fieldname, Object value, int expireTime) {
		return this.execCommand(new HsetnxCommand(key, fieldname, value, expireTime, getDatePattern()));
	}

	@Override
	public void hmset(String key, Map<String, Object> value, int expireTime) {
		this.execCommand(new HmsetCommand(key, value, expireTime, getDatePattern()));
	}

	@Override
	public String hget(String key, String fieldname) {
		return this.hget(key, fieldname, String.class);
	}

	@Override
	public <T> T hget(String key, String fieldname, Class<T> clazz) {
		return this.execCommand(new HgetCommand<T>(key, fieldname, clazz, getDatePattern()));
	}

	@Override
	public List<String> hmget(String key, String... fieldnames) {
		return this.hmget(key, String.class, fieldnames);
	}

	@Override
	public <T> List<T> hmget(String key, Class<T> clazz, String... fieldnames) {
		return this.execCommand(new HmgetCommand<T>(key, clazz, getDatePattern(), fieldnames));
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		return this.hgetAll(key, String.class);
	}

	@Override
	public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
		return this.execCommand(new HgetAllCommand<T>(key, clazz, getDatePattern()));
	}

	/* LIST */

	@Override
	public long llen(String key) {
		return this.execCommand(new LlenCommand(key));
	}

	@Override
	public long lpush(String key, int expireTime, Object... objects) {
		return this.execCommand(new LpushCommand(key, expireTime, getDatePattern(), objects));
	}

	@Override
	public long rpush(String key, int expireTime, Object... objects) {
		return this.execCommand(new RpushCommand(key, expireTime, getDatePattern(), objects));
	}

	@Override
	public String lpop(String key) {
		return this.lpop(key, String.class);
	}

	@Override
	public <T> T lpop(String key, Class<T> clazz) {
		return this.execCommand(new LpopCommand<>(key, clazz, getDatePattern()));
	}

	@Override
	public String rpop(String key) {
		return this.rpop(key, String.class);
	}

	@Override
	public <T> T rpop(String key, Class<T> clazz) {
		return this.execCommand(new RpopCommand<>(key, clazz, getDatePattern()));
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return this.lrange(key, start, end, String.class);
	}

	@Override
	public <T> List<T> lrange(String key, long start, long end, Class<T> clazz) {
		return this.execCommand(new LrangeCommand<T>(key, start, end, clazz, getDatePattern()));
	}

	/* SET */

	@Override
	public long scard(String key) {
		return this.execCommand(new ScardCommand(key));
	}

	@Override
	public void sadd(String key, int expireTime, Object... values) {
		this.execCommand(new SaddCommand(key, expireTime, getDatePattern(), values));
	}

	@Override
	public String spop(String key) {
		return this.spop(key, String.class);
	}

	@Override
	public <T> T spop(String key, Class<T> clazz) {
		return this.execCommand(new SpopCommand<T>(key, clazz, getDatePattern()));
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
		return this.execCommand(new SrandmemberCommand<T>(key, clazz, count, getDatePattern()));
	}

	@Override
	public Set<String> smembers(String key) {
		return this.smembers(key, String.class);
	}

	@Override
	public <T> Set<T> smembers(String key, Class<T> clazz) {
		return this.execCommand(new SmembersCommand<T>(key, clazz, getDatePattern()));
	}

	@Override
	public long srem(String key, Object... members) {
		return this.execCommand(new SremCommand(key, getDatePattern(), members));
	}

	@Override
	public boolean sismember(String key, Object member) {
		return this.execCommand(new SismemberCommand(key, member, getDatePattern()));
	}

	/* SORTED SET */

	@Override
	public void zadd(String key, double score, String member) {
		this.execCommand(new ZaddCommand(key, score, member));
	}

	@Override
	public void zadd(String key, Map<String, Double> scoreMembers) {
		this.execCommand(new ZaddCommandAll(key, scoreMembers));
	}

	@Override
	public long zcard(String key) {
		return this.execCommand(new ZcardCommand(key));
	}

	@Override
	public double zscore(String key, String member) {
		return this.execCommand(new ZscoreCommand(key, member));
	}

	@Override
	public long zrevrank(String key, String member) {
		return this.execCommand(new ZrevrankCommand(key, member));
	}

	@Override
	public long zrank(String key, String member) {
		return this.execCommand(new ZrankCommand(key, member));
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		return this.execCommand(new ZrangeCommand(key, start, end));
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		return this.execCommand(new ZrevrangeCommand(key, start, end));
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		Set<Tuple> ts = this.zrangeByScoreWithScores(key, min, max);
		if (ts == null || ts.isEmpty()) {
			return null;
		}
		Set<String> members = new HashSet<>(ts.size());
		for (Tuple t : ts) {
			members.add(t.getElement());
		}
		return members;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		return this.execCommand(new ZrangeByScoreCommand(key, min, max));
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<Tuple> ts = this.zrangeByScoreWithScores(key, min, max, offset, count);
		if (ts == null || ts.isEmpty()) {
			return null;
		}
		Set<String> members = new HashSet<>(ts.size());
		for (Tuple t : ts) {
			members.add(t.getElement());
		}
		return members;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		return this.execCommand(new ZrangeByScoreWithOffsetCommand(key, min, max, offset, count));
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double min, double max) {
		Set<Tuple> ts = this.zrangeByScoreWithScores(key, min, max);
		if (ts == null || ts.isEmpty()) {
			return null;
		}
		Set<String> members = new HashSet<>(ts.size());
		for (Tuple t : ts) {
			members.add(t.getElement());
		}
		return members;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double min, double max) {
		return this.execCommand(new ZrevrangeByScoreCommand(key, min, max));
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double min, double max, int offset, int count) {
		Set<Tuple> ts = this.zrangeByScoreWithScores(key, min, max, offset, count);
		if (ts == null || ts.isEmpty()) {
			return null;
		}
		Set<String> members = new HashSet<>(ts.size());
		for (Tuple t : ts) {
			members.add(t.getElement());
		}
		return members;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		return this.execCommand(new ZrevrangeByScoreWithOffsetCommand(key, min, max, offset, count));
	}

	@Override
	public long zcount(String key, double min, double max) {
		return this.execCommand(new ZcountCommand(key, min, max));
	}

	@Override
	public void zrem(String key, String... members) {
		this.execCommand(new ZremCommand(key, members));
	}

	@Override
	public void zremrangeByScore(String key, double start, double end) {
		this.execCommand(new ZremerangeByScoreCommand(key, start, end));
	}

	@Override
	public void zremrangeByRank(String key, long start, long end) {
		this.execCommand(new ZremerangeByRankCommand(key, start, end));
	}

}
