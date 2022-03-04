package FunctionRealize;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.util.Random;

// 状态变量
public class StateExample {
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
                            ctx.collect(random.nextInt(10));
                            Thread.sleep(100L);
                        }
                    }

                    @Override
                    public void cancel() {
                        running = false;
                    }
                })
                .keyBy(r -> true)
                .process(new KeyedProcessFunction<Boolean, Integer, Double>() {
                    // 声明一个状态变量作为累加器
                    // 状态变量的可见范围是当前key
                    // 状态变量是单例，只能被实例化一次
                    private ValueState<Tuple2<Integer, Integer>> valueState;
                    // 保存定时器的时间戳
                    private ValueState<Long> timerTs;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        // 实例化状态变量
                        valueState = getRuntimeContext().getState(
                                // 状态描述符
                                new ValueStateDescriptor<Tuple2<Integer, Integer>>("sum-count", Types.TUPLE(Types.INT, Types.INT))
                        );
                        timerTs = getRuntimeContext().getState(
                                new ValueStateDescriptor<Long>("timer", Types.LONG)
                        );
                    }

                    @Override
                    public void processElement(Integer value, Context ctx, Collector<Double> out) throws Exception {
                        // 当第一条数据到来时，状态变量的值为null
                        // 使用.value()方法读取状态变量的值，使用.update()方法更新状态变量的值
                        if (valueState.value() == null) {
                            valueState.update(Tuple2.of(value, 1));
                        } else {
                            Tuple2<Integer, Integer> tmp = valueState.value();
                            valueState.update(Tuple2.of(tmp.f0 + value, tmp.f1 + 1));
                        }

                        if (timerTs.value() == null) {
                            long tenSecLater = ctx.timerService().currentProcessingTime() + 10 * 1000L;
                            ctx.timerService().registerProcessingTimeTimer(tenSecLater);
                            timerTs.update(tenSecLater);
                        }
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Double> out) throws Exception {
                        super.onTimer(timestamp, ctx, out);
                        if (valueState.value() != null) {
                            out.collect((double) valueState.value().f0 / valueState.value().f1);
                            timerTs.clear();
                        }
                    }
                })
                .print();

        env.execute();
    }
}
