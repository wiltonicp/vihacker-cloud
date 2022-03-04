package WaterMark;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;

// 每个窗口中最热门的商品是什么
public class RealizeTopN {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env
                .readTextFile("/Users/jiangshanchen/githubspace/flink0224tutorial/src/main/resources/UserBehavior.csv")
                .map(new MapFunction<String, UserBehavior>() {
                    @Override
                    public UserBehavior map(String value) throws Exception {
                        String[] arr = value.split(",");
                        return new UserBehavior(
                                arr[0],arr[1],arr[2],arr[3],
                                Long.parseLong(arr[4]) * 1000L
                        );
                    }
                })
                .filter(r -> r.behavior.equals("pv"))
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<UserBehavior>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserBehavior>() {
                            @Override
                            public long extractTimestamp(UserBehavior element, long recordTimestamp) {
                                return element.timestamp;
                            }
                        })
                )
                .keyBy(r -> r.itemId)
                .window(SlidingEventTimeWindows.of(Time.hours(1), Time.minutes(5)))
                .aggregate(new CountAgg(), new WindowResult())
                .keyBy(r -> r.windowEnd)
                .process(new TopN(3))
                .print();

        env.execute();
    }

    public static class TopN extends KeyedProcessFunction<Long, ItemViewCount, String> {
        private ListState<ItemViewCount> listState;
        private Integer n;

        public TopN(Integer n) {
            this.n = n;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            // {
            //    'key1': ArrayList,
            //    'key2': ArrayList
            // }
            listState = getRuntimeContext().getListState(
                    new ListStateDescriptor<ItemViewCount>("list-state", Types.POJO(ItemViewCount.class))
            );
        }

        @Override
        public void processElement(ItemViewCount value, Context ctx, Collector<String> out) throws Exception {
            listState.add(value);
            ctx.timerService().registerEventTimeTimer(value.windowEnd + 1L);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            super.onTimer(timestamp, ctx, out);
            ArrayList<ItemViewCount> itemViewCountArrayList = new ArrayList<>();
            for (ItemViewCount ivc : listState.get()) itemViewCountArrayList.add(ivc);
            listState.clear();

            itemViewCountArrayList.sort(new Comparator<ItemViewCount>() {
                @Override
                public int compare(ItemViewCount t2, ItemViewCount t1) {
                    return t1.count.intValue() - t2.count.intValue();
                }
            });

            StringBuilder result = new StringBuilder();
            result
                    .append("==============================================\n")
                    .append("窗口结束时间：" + new Timestamp(timestamp - 1L))
                    .append("\n");
            for (int i = 0; i < n; i++) {
                ItemViewCount curr = itemViewCountArrayList.get(i);
                result
                        .append("第" + (i + 1) + "名的商品id是：" + curr.itemId)
                        .append("，浏览次数是：" + curr.count)
                        .append("\n");
            }
            result
                    .append("===============================================\n\n");
            out.collect(result.toString());
        }
    }

    public static class WindowResult extends ProcessWindowFunction<Long, ItemViewCount, String, TimeWindow> {
        @Override
        public void process(String s, Context context, Iterable<Long> elements, Collector<ItemViewCount> out) throws Exception {
            out.collect(new ItemViewCount(s, elements.iterator().next(), context.window().getStart(), context.window().getEnd()));
        }
    }

    public static class CountAgg implements AggregateFunction<UserBehavior, Long, Long> {
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(UserBehavior value, Long accumulator) {
            return accumulator + 1L;
        }

        @Override
        public Long getResult(Long accumulator) {
            return accumulator;
        }

        @Override
        public Long merge(Long a, Long b) {
            return null;
        }
    }

    // 每个商品在每个窗口中的浏览次数
    public static class ItemViewCount {
        public String itemId;
        public Long count;
        public Long windowStart;
        public Long windowEnd;

        public ItemViewCount() {
        }

        public ItemViewCount(String itemId, Long count, Long windowStart, Long windowEnd) {
            this.itemId = itemId;
            this.count = count;
            this.windowStart = windowStart;
            this.windowEnd = windowEnd;
        }

        @Override
        public String toString() {
            return "ItemViewCount{" +
                    "itemId='" + itemId + '\'' +
                    ", count=" + count +
                    ", windowStart=" + new Timestamp(windowStart) +
                    ", windowEnd=" + new Timestamp(windowEnd) +
                    '}';
        }
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
