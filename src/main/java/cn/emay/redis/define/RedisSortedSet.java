package cn.emay.redis.define;

import redis.clients.jedis.Tuple;

import java.util.Map;
import java.util.Set;

/**
 * @author Frank
 */
public interface RedisSortedSet {

    /**
     * 放入键、成员、评分
     *
     * @param key    键
     * @param score  评分
     * @param member 成员
     * @return 是否成功
     */
    boolean zadd(String key, double score, String member);

    /**
     * 成员评分+increment
     *
     * @param key       键
     * @param increment 数
     * @param member    成员
     * @return 评分
     */
    double zincrby(String key, double increment, String member);

    /**
     * 放入键、成员、评分
     *
     * @param key          键
     * @param scoreMembers 成员评分
     * @return 添加的数量
     */
    long zaddAll(String key, Map<String, Double> scoreMembers);

    /**
     * 集合的长度
     *
     * @param key 键
     * @return 长度
     */
    long zcard(String key);

    /**
     * 获取成员的评分
     *
     * @param key    键
     * @param member 成员
     * @return 评分
     */
    double zscore(String key, String member);

    /**
     * 获取成员的评分排名【从大到小排名】<br/>
     * 排名从0开始
     *
     * @param key    键
     * @param member 成员
     * @return 排名
     */
    long zrevrank(String key, String member);

    /**
     * 获取成员的评分排名【从小到大排名】<br/>
     * 排名从0开始
     *
     * @param key    键
     * @param member 成员
     * @return 排名
     */
    long zrank(String key, String member);

    /**
     * 获取有序集合成员【从小到大】,包含start,end。start和end从0开始 <br/>
     * start,end指定取的范围 start=0,end=-1为取全部 <br/>
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 成员
     */
    Set<String> zrange(String key, long start, long end);

    /**
     * 获取有序集合成员【从大到小】 ,包含start,end。start和end从0开始<br/>
     * start,end指定取的范围 start=0,end=-1为取全部 <br/>
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 成员
     */
    Set<String> zrevrange(String key, long start, long end);

    /**
     * 按照评分获取有序集合成员,限定min与max之间的评分(从小到大)，包含min和max <br/>
     *
     * @param key 键
     * @param min 最小评分
     * @param max 最大评分
     * @return 成员
     */
    Set<String> zrevrangeByScore(String key, double min, double max);

    /**
     * 按照评分获取有序集合成员及其评分,限定min与max之间的评分(从小到大)，包含min和max <br/>
     * getElement = getMember
     *
     * @param key 键
     * @param min 最小评分
     * @param max 最大评分
     * @return 成员及评分
     */
    Set<Tuple> zrevrangeByScoreWithScores(String key, double min, double max);

    /**
     * 按照评分获取有序集合成员,限定min与max之间的评分,从偏移量offset开始，取count个 (从小到大)<br/>
     * 包含min和max ,offset从0开始
     *
     * @param key    键
     * @param min    最小评分
     * @param max    最大评分
     * @param offset 偏移量
     * @param count  个数
     * @return 成员
     */
    Set<String> zrevrangeByScore(String key, double min, double max, int offset, int count);

    /**
     * 按照评分获取有序集合成员及其评分,限定min与max之间的评分,从偏移量offset开始，取count个(从小到大) <br/>
     * 包含min和max ,offset从0开始
     *
     * @param key    键
     * @param min    最小评分
     * @param max    最大评分
     * @param offset 偏移量
     * @param count  个数
     * @return 成员及评分
     */
    Set<Tuple> zrevrangeByScoreWithScores(String key, double min, double max, int offset, int count);

    /**
     * 按照评分获取有序集合成员,限定min与max之间的评分 (从大到小)<br/>
     * 包含min和max
     *
     * @param key 键
     * @param min 最小评分
     * @param max 最大评分
     * @return 成员
     */
    Set<String> zrangeByScore(String key, double min, double max);

    /**
     * 按照评分获取有序集合成员及其评分,限定min与max之间的评分(从大到小)，包含min和max <br/>
     * getElement = getMember
     *
     * @param key 键
     * @param min 最小评分
     * @param max 最大评分
     * @return 成员及评分
     */
    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);

    /**
     * 按照评分获取有序集合成员,限定min与max之间的评分,从偏移量offset开始，取count个 (从大到小)<br/>
     * 包含min和max ,offset从0开始
     *
     * @param key    键
     * @param min    最小评分
     * @param max    最大评分
     * @param offset 偏移量
     * @param count  个数
     * @return 成员
     */
    Set<String> zrangeByScore(String key, double min, double max, int offset, int count);

    /**
     * 按照评分获取有序集合成员及其评分,限定min与max之间的评分,从偏移量offset开始，取count个(从大到小) <br/>
     * 包含min和max ,offset从0开始
     *
     * @param key    键
     * @param min    最小评分
     * @param max    最大评分
     * @param offset 偏移量
     * @param count  个数
     * @return 成员及评分
     */
    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count);

    /**
     * 评分在min与max之间的成员的数量，包含min和max<br/>
     * 包含min和max
     *
     * @param key 键
     * @param min 最小评分
     * @param max 最大评分
     * @return 数量
     */
    long zcount(String key, double min, double max);

    /**
     * 删除成员
     *
     * @param key     键
     * @param members 成员
     * @return 删除的个数
     */
    long zrem(String key, String... members);

    /**
     * 删除评分区间的成员，包含min和max
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 删除的个数
     */
    long zremrangeByScore(String key, double start, double end);

    /**
     * 删除排序区间的成员，start和end从0开始
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 删除的个数
     */
    long zremrangeByRank(String key, long start, long end);

}
