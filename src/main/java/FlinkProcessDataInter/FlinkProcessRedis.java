package FlinkProcessDataInter;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

// 写入redis
public class FlinkProcessRedis {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Tuple2<String, Integer>> stream = env
                .fromElements(
                        Tuple2.of("key", 1),
                        Tuple2.of("key", 2)
                );

        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("localhost").build();

        stream.addSink(new RedisSink<Tuple2<String, Integer>>(conf, new MyRedisMapper()));

        env.execute();
    }

    public static class MyRedisMapper implements RedisMapper<Tuple2<String, Integer>> {
        @Override
        public String getKeyFromData(Tuple2<String, Integer> in) {
            return in.f0;
        }

        @Override
        public String getValueFromData(Tuple2<String, Integer> in) {
            return in.f1.toString();
        }

        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET, "tuple");
        }
    }
}
