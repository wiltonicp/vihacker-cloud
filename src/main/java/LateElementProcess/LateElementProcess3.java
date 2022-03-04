package LateElementProcess;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;

// 使用迟到数据更新窗口计算结果
public class LateElementProcess3 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<String> result = env
                .socketTextStream("localhost", 9999)
                .map(new MapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(String value) throws Exception {
                        String[] arr = value.split(" ");
                        return Tuple2.of(arr[0], Long.parseLong(arr[1]) * 1000L);
                    }
                })
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                                    @Override
                                    public long extractTimestamp(Tuple2<String, Long> element, long recordTimestamp) {
                                        return element.f1;
                                    }
                                })
                )
                .keyBy(r -> r.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .allowedLateness(Time.seconds(5))
                .sideOutputLateData(new OutputTag<Tuple2<String, Long>>("late") {
                })
                .process(new ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<Tuple2<String, Long>> elements, Collector<String> out) throws Exception {
                        // 初始化一个窗口状态变量，注意：窗口状态变量的可见范围是当前窗口
                        ValueState<Boolean> firstCalculate = context.windowState().getState(new ValueStateDescriptor<Boolean>("first", Types.BOOLEAN));
                        if (firstCalculate.value() == null) {
                            out.collect("窗口第一次触发计算了！水位线是：" + context.currentWatermark() + " 窗口中共有 " + elements.spliterator().getExactSizeIfKnown());
                            firstCalculate.update(true); // 第一次触发process执行以后，更新为true
                        } else {
                            out.collect("迟到数据到了，更新以后的计算结果是：" + elements.spliterator().getExactSizeIfKnown());
                        }
                    }
                });

        result.print();

        result.getSideOutput(new OutputTag<Tuple2<String, Long>>("late") {
        }).print();

        env.execute();
    }
}
