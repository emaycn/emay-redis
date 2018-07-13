package cn.emay.store.redis.define;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

public interface RedisSortedSet {
	
	/**
	 * 放入键、成员、评分
	 * 
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public void zadd(String key, double score, String member);

	/**
	 * 放入键、成员、评分
	 * 
	 * @param key
	 * @param scoreMembers
	 * @return
	 */
	public void zadd(String key, Map<String, Double> scoreMembers);

	/**
	 * 集合的长度
	 * 
	 * @param key
	 * @return
	 */
	public long zcard(String key);

	/**
	 * 获取成员的评分
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public double zscore(String key, String member);

	/**
	 * 获取成员的评分排名【从大到小排名】<br/>
	 * 排名从0开始
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public long zrevrank(String key, String member);

	/**
	 * 获取成员的评分排名【从小到大排名】<br/>
	 * 排名从0开始
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public long zrank(String key, String member);

	/**
	 * 获取有序集合成员【从小到大】,包含start,end。start和end从0开始 <br/>
	 * start,end指定取的范围 start=0,end=-1为取全部 <br/>
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrange(String key, long start, long end);

	/**
	 * 获取有序集合成员【从大到小】 ,包含start,end。start和end从0开始<br/>
	 * start,end指定取的范围 start=0,end=-1为取全部 <br/>
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrevrange(String key, long start, long end);

	/**
	 * 按照评分获取有序集合成员,限定min与max之间的评分(从小到大)，包含min和max <br/>
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<String> zrevrangeByScore(String key, double min, double max);

	/**
	 * 按照评分获取有序集合成员及其评分,限定min与max之间的评分(从小到大)，包含min和max  <br/>
	 * getElement = getMember
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double min, double max);

	/**
	 * 按照评分获取有序集合成员,限定min与max之间的评分,从偏移量offset开始，取count个 (从小到大)<br/>
	 * 包含min和max ,offset从0开始
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 */
	public Set<String> zrevrangeByScore(String key, double min, double max, int offset, int count);

	/**
	 * 按照评分获取有序集合成员及其评分,限定min与max之间的评分,从偏移量offset开始，取count个(从小到大) <br/>
	 * 包含min和max ,offset从0开始
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 */
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double min, double max, int offset, int count);

	/**
	 * 按照评分获取有序集合成员,限定min与max之间的评分 (从大到小)<br/>
	 * 包含min和max 
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<String> zrangeByScore(String key, double min, double max);

	/**
	 * 按照评分获取有序集合成员及其评分,限定min与max之间的评分(从大到小)，包含min和max <br/>
	 * getElement = getMember
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);

	/**
	 * 按照评分获取有序集合成员,限定min与max之间的评分,从偏移量offset开始，取count个 (从大到小)<br/>
	 * 包含min和max ,offset从0开始
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 */
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count);

	/**
	 * 按照评分获取有序集合成员及其评分,限定min与max之间的评分,从偏移量offset开始，取count个(从大到小) <br/>
	 * 包含min和max ,offset从0开始
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 */
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count);

	/**
	 * 评分在min与max之间的成员的数量，包含min和max<br/>
	 * 包含min和max
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public long zcount(String key, double min, double max);

	/**
	 * 删除成员
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public void zrem(String key, String... members);

	/**
	 * 删除评分区间的成员，包含min和max
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void zremrangeByScore(String key, double start, double end);

	/**
	 * 删除排序区间的成员，start和end从0开始
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void zremrangeByRank(String key, long start, long end);

}
