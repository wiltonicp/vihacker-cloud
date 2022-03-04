package LateElementProcess;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

// union
// 1. 多条流的合并
// 2. 所有流中的事件类型必须是一样的
public class StreamJoin {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Integer> stream1 = env.fromElements(1, 2);

        DataStreamSource<Integer> stream2 = env.fromElements(3, 4);

        DataStreamSource<Integer> stream3 = env.fromElements(5, 6);

        DataStream<Integer> result = stream1.union(stream2, stream3);

        result.print();

        env.execute();
    }
}
