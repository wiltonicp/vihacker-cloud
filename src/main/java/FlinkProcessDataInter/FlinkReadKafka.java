package FlinkProcessDataInter;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

// 读取kafka的数据
public class FlinkReadKafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "hadoop101:9092,hadoop102:9092,hadoop103:9092");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset", "latest");

        env
                .addSource(new FlinkKafkaConsumer<String>(
                        "user-behavior-1",
                        new SimpleStringSchema(),
                        properties
                ))
                .map(new MapFunction<String, UserBehavior>() {
                    @Override
                    public UserBehavior map(String value) throws Exception {
                        String[] arr = value.split(",");
                        return new UserBehavior(
                                arr[0], arr[1], arr[2], arr[3],
                                Long.parseLong(arr[4]) * 1000L
                        );
                    }
                })
                .filter(r -> r.behavior.equals("pv"))
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<UserBehavior>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserBehavior>() {
                            @Override
                            public long extractTimestamp(UserBehavior element, long recordTimestamp) {
                                return element.timestamp;
                            }
                        })
                )
                .keyBy(r -> true)
                .window(SlidingEventTimeWindows.of(Time.hours(1), Time.minutes(5)))
                .process(new ProcessWindowFunction<UserBehavior, String, Boolean, TimeWindow>() {
                    @Override
                    public void process(Boolean aBoolean, Context context, Iterable<UserBehavior> elements, Collector<String> out) throws Exception {
                        HashMap<String, Long> hashMap = new HashMap<>();
                        for (UserBehavior e : elements) {
                            if (hashMap.containsKey(e.itemId)) {
                                hashMap.put(e.itemId, hashMap.get(e.itemId) + 1L);
                            } else {
                                hashMap.put(e.itemId, 1L);
                            }
                        }

                        ArrayList<Tuple2<String, Long>> arrayList = new ArrayList<>();
                        for (String key : hashMap.keySet()) {
                            arrayList.add(Tuple2.of(key, hashMap.get(key)));
                        }

                        arrayList.sort(new Comparator<Tuple2<String, Long>>() {
                            @Override
                            public int compare(Tuple2<String, Long> t2, Tuple2<String, Long> t1) {
                                return t1.f1.intValue() - t2.f1.intValue();
                            }
                        });

                        StringBuilder result = new StringBuilder();
                        result
                                .append("========================================\n")
                                .append("窗口：" + new Timestamp(context.window().getStart()) + "~" + new Timestamp(context.window().getEnd()))
                                .append("\n");
                        for (int i = 0; i < 3; i++) {
                            Tuple2<String, Long> currElement = arrayList.get(i);
                            result
                                    .append("第" + (i+1) + "名的商品ID是：" + currElement.f0 + "; 浏览次数是：" + currElement.f1)
                                    .append("\n");
                        }
                        out.collect(result.toString());
                    }
                })
                .print();

        env.execute();
    }

    public static class UserBehavior {
        public String userId;
        public String itemId;
        public String categoryId;
        public String behavior;
        public Long timestamp;

        public UserBehavior() {
        }

        public UserBehavior(String userId, String itemId, String categoryId, String behavior, Long timestamp) {
            this.userId = userId;
            this.itemId = itemId;
            this.categoryId = categoryId;
            this.behavior = behavior;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "UserBehavior{" +
                    "userId='" + userId + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", categoryId='" + categoryId + '\'' +
                    ", behavior='" + behavior + '\'' +
                    ", timestamp=" + new Timestamp(timestamp) +
                    '}';
        }
    }
}
