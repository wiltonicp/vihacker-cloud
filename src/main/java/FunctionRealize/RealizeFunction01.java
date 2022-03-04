package FunctionRealize;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

public class RealizeFunction01 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env
                .addSource(new RichParallelSourceFunction<Integer>() {
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        System.out.println("生命周期开始，子任务索引是：" + getRuntimeContext().getIndexOfThisSubtask());
                    }

                    @Override
                    public void run(SourceContext<Integer> ctx) throws Exception {
                        for (int i = 0; i < 10; i++) {
                            if (i % 2 == getRuntimeContext().getIndexOfThisSubtask()) {
                                ctx.collect(i);
                            }
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                })
                .setParallelism(2)
                .print()
                .setParallelism(2);

        env.execute();
    }
}
