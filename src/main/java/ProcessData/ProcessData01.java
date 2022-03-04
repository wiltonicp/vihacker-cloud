package ProcessData;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

// 从socket读取数据然后处理
// word count
public class ProcessData01 {
    // 记得抛出异常
    public static void main(String[] args) throws Exception {
        // 获取流处理的运行时环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行任务的数量为1
        env.setParallelism(1);

        // 读取数据源
        // 先在终端启动 `nc -lk 9999`
        DataStreamSource<String> stream = env.socketTextStream("localhost", 9999);

        // map操作
        // 这里使用的flatMap方法
        // map: 针对流中的每一个元素，输出一个元素
        // flatMap：针对流中的每一个元素，输出0个，1个或者多个元素
        SingleOutputStreamOperator<WordWithCount> mappedStream = stream
                // 输入泛型：String; 输出泛型：WordWithCount
                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<WordWithCount> out) throws Exception {
                        String[] arr = value.split(" ");
                        // 使用collect方法向下游发送数据
                        for (String e : arr) {
                            out.collect(new WordWithCount(e, 1L));
                        }
                    }
                });

        // 分组：shuffle
        KeyedStream<WordWithCount, String> keyedStream = mappedStream
                // 第一个泛型：流中元素的泛型
                // 第二个泛型：key的泛型
                .keyBy(new KeySelector<WordWithCount, String>() {
                    @Override
                    public String getKey(WordWithCount value) throws Exception {
                        return value.word;
                    }
                });

        // reduce操作
        // reduce会维护一个累加器
        // 第一条数据到来，作为累加器输出
        // 第二条数据到来，和累加器进行聚合操作，然后输出累加器
        // 累加器和流中元素的类型是一样的
        SingleOutputStreamOperator<WordWithCount> result = keyedStream
                .reduce(new ReduceFunction<WordWithCount>() {
                    // 定义了聚合的逻辑
                    @Override
                    public WordWithCount reduce(WordWithCount value1, WordWithCount value2) throws Exception {
                        return new WordWithCount(value1.word, value1.count + value2.count);
                    }
                });

        // 输出
        result.print();


        // 执行程序
        env.execute();
    }

    // POJO类
    // 1. 必须是公有类
    // 2. 所有字段必须是public
    // 3. 必须有空构造器
    // 模拟了case class
    public static class WordWithCount {
        public String word;
        public Long count;

        public WordWithCount() {
        }

        public WordWithCount(String word, Long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return "WordWithCount{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
