package FunctionRealize;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.util.Random;

// 整数连续1s上升
public class MonitorData {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env
                .addSource(new SourceFunction<Integer>() {
                    private boolean running = true;
                    private Random random = new Random();
                    @Override
                    public void run(SourceContext<Integer> ctx) throws Exception {
                        while (running) {
                            ctx.collect(random.nextInt());
                            Thread.sleep(300L);
                        }
                    }

                    @Override
                    public void cancel() {
                        running = false;
                    }
                })
                .keyBy(r -> 1)
                .process(new IntIncreaseAlert())
                .print();

        env.execute();
    }

    public static class IntIncreaseAlert extends KeyedProcessFunction<Integer, Integer, String> {
        private ValueState<Integer> lastInt;
        private ValueState<Long> timerTs;
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            lastInt = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("last-integer", Types.INT));
            timerTs = getRuntimeContext().getState(new ValueStateDescriptor<Long>("timer", Types.LONG));
        }

        @Override
        public void processElement(Integer value, Context ctx, Collector<String> out) throws Exception {
            Integer prevInt = null;
            if (lastInt.value() != null) {
                prevInt = lastInt.value();
            }
            lastInt.update(value);

            Long ts = null;
            if (timerTs.value() != null) {
                ts = timerTs.value();
            }

            if (prevInt == null || value < prevInt) {
                if (ts != null) {
                    ctx.timerService().deleteProcessingTimeTimer(ts);
                    timerTs.clear();
                }
            } else if (value > prevInt && ts == null) {
                long oneSecLater = ctx.timerService().currentProcessingTime() + 1000L;
                ctx.timerService().registerProcessingTimeTimer(oneSecLater);
                timerTs.update(oneSecLater);
            }
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            super.onTimer(timestamp, ctx, out);
            out.collect("整数连续1s上升了！");
            timerTs.clear();
        }
    }
}
