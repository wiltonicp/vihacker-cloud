package BaseTransformation;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

// 求整数平均值
public class RealizeAverage {
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
                .map(r -> Tuple2.of(r, 1))
                .returns(Types.TUPLE(Types.INT, Types.INT))
                .keyBy(r -> true)
                .reduce(new ReduceFunction<Tuple2<Integer, Integer>>() {
                    @Override
                    public Tuple2<Integer, Integer> reduce(Tuple2<Integer, Integer> value1, Tuple2<Integer, Integer> value2) throws Exception {
                        return Tuple2.of(value1.f0 + value2.f0, value1.f1 + value2.f1);
                    }
                })
                .map(new MapFunction<Tuple2<Integer, Integer>, Double>() {
                    @Override
                    public Double map(Tuple2<Integer, Integer> value) throws Exception {
                        return (double) value.f0 / value.f1;
                    }
                })
                .print();

        env.execute();
    }
}
