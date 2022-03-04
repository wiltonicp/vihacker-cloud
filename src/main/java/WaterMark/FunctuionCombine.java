package WaterMark;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

// 每个用户每5秒钟窗口的pv
// 增量聚合函数和全窗口聚合函数结合使用
public class FunctuionCombine {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env
                .addSource(new ClickSource())
                .keyBy(r -> r.user)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .aggregate(new CountAgg(), new WindowResult())
                .print();

        env.execute();
    }

    // 输入的泛型是增量聚合函数的输出的类型
    public static class WindowResult extends ProcessWindowFunction<Integer, String, String, TimeWindow> {
        @Override
        public void process(String key, Context context, Iterable<Integer> elements, Collector<String> out) throws Exception {
            // 在窗口关闭的时候，触发调用
            // 迭代器参数中只包含一个元素，就是增量聚合函数发送过来的聚合结果
            long windowStart = context.window().getStart();
            long windowEnd   = context.window().getEnd();
            long count       = elements.iterator().next();
            out.collect("用户：" + key + " 在窗口" +
                    "" + new Timestamp(windowStart) + "~" + new Timestamp(windowEnd) + "" +
                    "中的pv次数是：" + count);
        }
    }

    public static class CountAgg implements AggregateFunction<Event, Integer, Integer> {
        // 创建累加器
        @Override
        public Integer createAccumulator() {
            return 0;
        }

        // 定义累加规则
        @Override
        public Integer add(Event value, Integer accumulator) {
            return accumulator + 1;
        }

        // 在窗口关闭时返回结果
        @Override
        public Integer getResult(Integer accumulator) {
            return accumulator;
        }

        @Override
        public Integer merge(Integer a, Integer b) {
            return null;
        }
    }

    // SourceFunction并行度只能为1
    // 自定义并行化版本的数据源，需要使用ParallelSourceFunction
    public static class ClickSource implements SourceFunction<Event> {
        private boolean running = true;
        private String[] userArr = {"Mary", "Bob", "Alice", "Liz"};
        private String[] urlArr = {"./home", "./cart", "./fav", "./prod?id=1", "./prod?id=2"};
        private Random random = new Random();
        @Override
        public void run(SourceContext<Event> ctx) throws Exception {
            while (running) {
                // collect方法，向下游发送数据
                ctx.collect(
                        new Event(
                                userArr[random.nextInt(userArr.length)],
                                urlArr[random.nextInt(urlArr.length)],
                                Calendar.getInstance().getTimeInMillis()
                        )
                );
                Thread.sleep(1000L);
            }
        }

        @Override
        public void cancel() {
            running = false;
        }
    }

    public static class Event {
        public String user;
        public String url;
        public Long timestamp;

        public Event() {
        }

        public Event(String user, String url, Long timestamp) {
            this.user = user;
            this.url = url;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "Event{" +
                    "user='" + user + '\'' +
                    ", url='" + url + '\'' +
                    ", timestamp=" + new Timestamp(timestamp) +
                    '}';
        }
    }
}
