package FunctionRealize;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.util.Random;

// 使用列表状态变量求平均值
public class StateProcessAverage {
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
                            Thread.sleep(300L);
                        }
                    }

                    @Override
                    public void cancel() {
                        running = false;
                    }
                })
                .keyBy(r -> 1)
                .process(new KeyedProcessFunction<Integer, Integer, Double>() {
                    private ListState<Integer> listState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        listState = getRuntimeContext().getListState(new ListStateDescriptor<Integer>("list-state", Types.INT));
                    }

                    @Override
                    public void processElement(Integer value, Context ctx, Collector<Double> out) throws Exception {
                        listState.add(value);
                        Integer sum = 0;
                        Integer count = 0;
                        for (Integer i : listState.get()) {
                            sum += i;
                            count += 1;
                        }
                        out.collect((double) sum / count);
                    }
                })
                .print();

        env.execute();
    }
}
