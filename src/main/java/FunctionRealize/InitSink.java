package FunctionRealize;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

// 自定义输出
public class InitSink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env
                .fromElements(1,2,3)
                .addSink(new SinkFunction<Integer>() {
                    @Override
                    public void invoke(Integer value, Context context) throws Exception {
                        SinkFunction.super.invoke(value, context);
                        System.out.println(value);
                    }
                });

        env.execute();
    }
}
