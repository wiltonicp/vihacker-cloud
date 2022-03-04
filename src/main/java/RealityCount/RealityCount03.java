package RealityCount;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class RealityCount03 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple2<String, Integer>> stream1 = env
                .fromElements(Tuple2.of("a", 1), Tuple2.of("b", 1))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple2<String, Integer>>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Integer>>() {
                            @Override
                            public long extractTimestamp(Tuple2<String, Integer> element, long recordTimestamp) {
                                return element.f1;
                            }
                        }));

        SingleOutputStreamOperator<Tuple2<String, Integer>> stream2 = env
                .fromElements(Tuple2.of("a", 2), Tuple2.of("b", 2), Tuple2.of("b", 3))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple2<String, Integer>>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Integer>>() {
                            @Override
                            public long extractTimestamp(Tuple2<String, Integer> element, long recordTimestamp) {
                                return element.f1;
                            }
                        }));

        stream1
                .join(stream2)
                .where(r -> r.f0)
                .equalTo(r -> r.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new JoinFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, String>() {
                    @Override
                    public String join(Tuple2<String, Integer> first, Tuple2<String, Integer> second) throws Exception {
                        return first + " => " + second;
                    }
                })
                .print();

        env.execute();
    }
}
