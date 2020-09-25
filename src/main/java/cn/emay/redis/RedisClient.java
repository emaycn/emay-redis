package cn.emay.redis;

import cn.emay.redis.command.PipelineCommand;
import cn.emay.redis.command.RedisCommand;
import cn.emay.redis.define.*;

import java.io.Closeable;
import java.util.List;

/**
 * Redis 客户端
 *
 * @author Frank
 */
public interface RedisClient extends Closeable, RedisCommon, RedisString, RedisHash, RedisList, RedisSet, RedisSortedSet {

    /**
     * 执行
     *
     * @param command 命令
     * @return 结果
     */
    <T> T execCommand(RedisCommand<T> command);

    /**
     * 执行管道命令
     *
     * @param pipelineCommand 管道命令
     * @return 结果
     */
    List<Object> execPipelineCommand(PipelineCommand pipelineCommand);

    /**
     * 获取日期处理格式
     *
     * @return 日期格式
     */
    String getDatePattern();

}