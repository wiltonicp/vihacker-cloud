package FunctionRealize;

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
public class RealizePV {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env
                .addSource(new ClickSource())
                .keyBy(r -> r.user)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .process(new WindowResult())
                .print();

        env.execute();
    }

    public static class WindowResult extends ProcessWindowFunction<Event, String, String, TimeWindow> {
        @Override
        public void process(String key, Context context, Iterable<Event> elements, Collector<String> out) throws Exception {
            // 在窗口关闭的时候，触发调用
            // 迭代器参数中包含了窗口中所有的元素
            long windowStart = context.window().getStart();
            long windowEnd   = context.window().getEnd();
            long count       = elements.spliterator().getExactSizeIfKnown(); // 迭代器里面共多少条元素
            out.collect("用户：" + key + " 在窗口" +
                    "" + new Timestamp(windowStart) + "~" + new Timestamp(windowEnd) + "" +
                    "中的pv次数是：" + count);
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
