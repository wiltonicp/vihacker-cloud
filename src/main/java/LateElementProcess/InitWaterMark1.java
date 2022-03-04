package LateElementProcess;

import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

// 自定义水位线的产生逻辑
public class InitWaterMark1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env
                .socketTextStream("localhost", 9999)
                .map(new MapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(String value) throws Exception {
                        String[] arr = value.split(" ");
                        return Tuple2.of(arr[0], Long.parseLong(arr[1]) * 1000L);
                    }
                })
                .assignTimestampsAndWatermarks(new CustomWatermarkGenerator())
                .print();
    }

    public static class CustomWatermarkGenerator implements WatermarkStrategy<Tuple2<String, Long>> {
        @Override
        public TimestampAssigner<Tuple2<String, Long>> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
            return new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                @Override
                public long extractTimestamp(Tuple2<String, Long> element, long recordTimestamp) {
                    return element.f1;
                }
            };
        }

        @Override
        public WatermarkGenerator<Tuple2<String, Long>> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
            return new WatermarkGenerator<Tuple2<String, Long>>() {
                private Long bound = 5000L; // 最大延迟时间
                private Long maxTs = -Long.MAX_VALUE + bound + 1L; // 防止溢出
                @Override
                public void onEvent(Tuple2<String, Long> event, long eventTimestamp, WatermarkOutput output) {
                    maxTs = Math.max(maxTs, event.f1); // 更新观察到的最大事件时间
                }

                @Override
                public void onPeriodicEmit(WatermarkOutput output) {
                    // 发送水位线，注意水位线的计算公式
                    output.emitWatermark(new Watermark(maxTs - bound - 1L));
                }
            };
        }
    }
}
