package RealityCount;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;

// 基于间隔的join
public class WaterMarkJoin {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> orderStream = env
                .fromElements(
                        Event.of("user-1", "order", 20 * 60 * 1000L)
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forMonotonousTimestamps()
                                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                                    @Override
                                    public long extractTimestamp(Event element, long recordTimestamp) {
                                        return element.timestamp;
                                    }
                                })
                );

        SingleOutputStreamOperator<Event> pvStream = env
                .fromElements(
                        Event.of("user-1", "pv", 5 * 60 * 1000L),
                        Event.of("user-1", "pv", 10 * 60 * 1000L),
                        Event.of("user-1", "pv", 12 * 60 * 1000L),
                        Event.of("user-1", "pv", 22 * 60 * 1000L)
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forMonotonousTimestamps()
                                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                                    @Override
                                    public long extractTimestamp(Event element, long recordTimestamp) {
                                        return element.timestamp;
                                    }
                                })
                );

        // a.ts + low < b.ts < a.ts + high
        // a.ts < b.ts + (-low)
        // a.ts > b.ts + (-high)
        // b.ts + (-high) < a.ts < b.ts + (-low)
        orderStream.keyBy(r -> r.userId)
                .intervalJoin(pvStream.keyBy(r -> r.userId))
                .between(Time.minutes(-10), Time.minutes(5))
                .process(new ProcessJoinFunction<Event, Event, String>() {
                    @Override
                    public void processElement(Event left, Event right, Context ctx, Collector<String> out) throws Exception {
                        out.collect(left + " => " + right);
                    }
                })
                .print("orderStream JOIN pvStream: ");

        pvStream.keyBy(r -> r.userId)
                .intervalJoin(orderStream.keyBy(r -> r.userId))
                .between(Time.minutes(-5), Time.minutes(10))
                .process(new ProcessJoinFunction<Event, Event, String>() {
                    @Override
                    public void processElement(Event left, Event right, Context ctx, Collector<String> out) throws Exception {
                        out.collect(right + " => " + left);
                    }
                })
                .print("pvStream JOIN orderStream: ");

        env.execute();
    }

    public static class Event {
        public String userId;
        public String eventType;
        public Long timestamp;

        public Event() {
        }

        public Event(String userId, String eventType, Long timestamp) {
            this.userId = userId;
            this.eventType = eventType;
            this.timestamp = timestamp;
        }

        public static Event of(String userId, String eventType, Long timestamp) {
            return new Event(userId, eventType, timestamp);
        }

        @Override
        public String toString() {
            return "Event{" +
                    "userId='" + userId + '\'' +
                    ", eventType='" + eventType + '\'' +
                    ", timestamp=" + new Timestamp(timestamp) +
                    '}';
        }
    }
}
