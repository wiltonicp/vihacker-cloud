package LateElementProcess;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

// connect来联结两条流
// 1. 只能连结两条流
// 2. 两条流中元素类型可以不同
public class StreamJoin2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Event> clickStream = env.addSource(new ClickSource());

        DataStreamSource<String> queryStream = env.socketTextStream("localhost", 9999).setParallelism(1);

        clickStream
                .keyBy(r -> r.user)
                .connect(queryStream.broadcast())
                .flatMap(new CoFlatMapFunction<Event, String, Event>() {
                    private String query = "";
                    @Override
                    public void flatMap1(Event value, Collector<Event> out) throws Exception {
                        if (value.url.equals(query)) out.collect(value);
                    }

                    @Override
                    public void flatMap2(String value, Collector<Event> out) throws Exception {
                        query = value;
                    }
                })
                .print();

        env.execute();
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
