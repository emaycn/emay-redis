package cn.emay.redis.command;

import java.util.Collections;
import java.util.List;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBase;
import redis.clients.jedis.ShardedJedisPipeline;

/**
 * 执行管道命令
 * 
 * @author frank
 *
 */
public interface PipelineCommand {

	/**
	 * 执行命令<br/>
	 * 仅写命令。不要考虑返回结果
	 * 
	 * @param pipeline
	 */
	void commond(PipelineBase pipeline);

	/**
	 * 获取返回结果
	 * 
	 * @param pipeline
	 * @return
	 */
	default List<Object> getResult(Pipeline pipeline) {
		List<Object> list = pipeline.syncAndReturnAll();
		if (!list.isEmpty()) {
			list.removeAll(Collections.singletonList(null));
		}
		return list;
	}

	/**
	 * 获取返回结果
	 * 
	 * @param pipeline
	 * @return
	 */
	default List<Object> getResult(ShardedJedisPipeline pipeline) {
		List<Object> list = pipeline.syncAndReturnAll();
		if (!list.isEmpty()) {
			list.removeAll(Collections.singletonList(null));
		}
		return list;
	}

}
