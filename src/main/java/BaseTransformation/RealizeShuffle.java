package BaseTransformation;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

// shuffle
public class RealizeShuffle {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env
                .fromElements(1,2,3,4).setParallelism(1)
                .shuffle()
                .print("shuffle: ").setParallelism(2);

        env
                .fromElements(1,2,3,4).setParallelism(1)
                .rebalance()
                .print("rebalance: ").setParallelism(2);

        env
                .fromElements(1,2,3,4).setParallelism(1)
                .broadcast()
                .print("broadcase: ").setParallelism(2);

        env.execute();
    }
}
