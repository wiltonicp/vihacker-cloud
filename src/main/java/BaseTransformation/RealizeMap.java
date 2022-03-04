package BaseTransformation;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.util.Random;

// MAP
public class RealizeMap {
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
                            ctx.collect(random.nextInt(1000));
                            Thread.sleep(1000);
                        }
                    }

                    @Override
                    public void cancel() {
                        running = false;
                    }
                })
                .map(r -> Tuple2.of(r, r))
                // 会被擦除成Tuple2<Object, Object>
                // 需要returns方法来标注一下map函数的输出类型
                .returns(Types.TUPLE(Types.INT, Types.INT))
                .print();

        env
                .addSource(new SourceFunction<Integer>() {
                    private boolean running = true;
                    private Random random = new Random();
                    @Override
                    public void run(SourceContext<Integer> ctx) throws Exception {
                        while (running) {
                            ctx.collect(random.nextInt(1000));
                            Thread.sleep(1000);
                        }
                    }

                    @Override
                    public void cancel() {
                        running = false;
                    }
                })
                .map(new MapFunction<Integer, Tuple2<Integer, Integer>>() {
                    @Override
                    public Tuple2<Integer, Integer> map(Integer value) throws Exception {
                        return Tuple2.of(value, value);
                    }
                })
                .print();

        env
                .addSource(new SourceFunction<Integer>() {
                    private boolean running = true;
                    private Random random = new Random();
                    @Override
                    public void run(SourceContext<Integer> ctx) throws Exception {
                        while (running) {
                            ctx.collect(random.nextInt(1000));
                            Thread.sleep(1000);
                        }
                    }

                    @Override
                    public void cancel() {
                        running = false;
                    }
                })
                .map(new MyMap())
                .print();

        env
                .addSource(new SourceFunction<Integer>() {
                    private boolean running = true;
                    private Random random = new Random();
                    @Override
                    public void run(SourceContext<Integer> ctx) throws Exception {
                        while (running) {
                            ctx.collect(random.nextInt(1000));
                            Thread.sleep(1000);
                        }
                    }

                    @Override
                    public void cancel() {
                        running = false;
                    }
                })
                .flatMap(new FlatMapFunction<Integer, Tuple2<Integer, Integer>>() {
                    @Override
                    public void flatMap(Integer value, Collector<Tuple2<Integer, Integer>> out) throws Exception {
                        out.collect(Tuple2.of(value, value));
                    }
                })
                .print();

        env.execute();
    }

    public static class MyMap implements MapFunction<Integer, Tuple2<Integer, Integer>> {
        @Override
        public Tuple2<Integer, Integer> map(Integer value) throws Exception {
            return Tuple2.of(value, value);
        }
    }
}
