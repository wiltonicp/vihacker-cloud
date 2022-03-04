package LateElementProcess;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

// 迟到数据发送到侧输出流中去
public class LateElementProcess1 {
    // 定义侧输出流的名字：侧输出标签
    private static OutputTag<String> lateElement = new OutputTag<String>("late-element"){};
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<String> result = env
                .addSource(new SourceFunction<Tuple2<String, Long>>() {
                    @Override
                    public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
                        // 指定时间戳发送数据
                        ctx.collectWithTimestamp(Tuple2.of("hello world", 1000L), 1000L);
                        //发送水位线
                        ctx.emitWatermark(new Watermark(999L));
                        ctx.collectWithTimestamp(Tuple2.of("hello flink", 2000L), 2000L);
                        ctx.emitWatermark(new Watermark(1999L));
                        ctx.collectWithTimestamp(Tuple2.of("hello late", 1000L), 1000L);
                    }

                    @Override
                    public void cancel() {

                    }
                })
                .process(new ProcessFunction<Tuple2<String, Long>, String>() {
                    @Override
                    public void processElement(Tuple2<String, Long> value, Context ctx, Collector<String> out) throws Exception {
                        if (value.f1 < ctx.timerService().currentWatermark()) {


                            System.out.println(ctx.timerService().currentWatermark());

                            // 发送到侧输出流
                            ctx.output(lateElement, "迟到元素发送到侧输出流：" + value);
                        } else {
                            System.out.println(ctx.timerService().currentWatermark());

                            out.collect("正常到达的元素：" + value);
                        }
                    }
                });

        result.print("主流：");

        // 打印侧输出流
        result.getSideOutput(lateElement).print("侧输出：");

        env.execute();
    }
}
