package FlinkSql;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.sql.Timestamp;

import static org.apache.flink.table.api.Expressions.$;

// 使用flink sql实现实时热门商品
public class FlinkSql01 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<UserBehavior> stream = env
                .readTextFile("/Users/jiangshanchen/githubspace/flink0224tutorial/src/main/resources/UserBehavior.csv")
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
                );

        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, settings);

        Table table = tableEnvironment
                .fromDataStream(
                        stream,
                        $("userId"),
                        $("itemId"),
                        $("categoryId"),
                        $("behavior"),
                        $("timestamp").rowtime().as("ts")
                );

        tableEnvironment.createTemporaryView("userbehavior", table);

        String innerSQL = "SELECT itemId, COUNT(itemId) as cnt, HOP_END(ts, INTERVAL '5' MINUTE, INTERVAL '1' HOUR) as windowEnd " +
                "FROM userbehavior GROUP BY itemId, HOP(ts, INTERVAL '5' MINUTE, INTERVAL '1' HOUR)";

        // 按照窗口结束时间分区，然后按照浏览量降序排列
        String midSQL = "SELECT *, ROW_NUMBER() OVER (PARTITION BY windowEnd ORDER BY cnt DESC) as row_num" +
                " FROM (" + innerSQL + ")";

        // 取出前3名
        String outerSQL = "SELECT * FROM (" + midSQL + ") WHERE row_num <= 3";

        Table result = tableEnvironment.sqlQuery(outerSQL);

        tableEnvironment.toChangelogStream(result).print();

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
