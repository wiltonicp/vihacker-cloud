package FlinkSql;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkSql02 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream = env
                .fromElements(
                        Tuple3.of("Mary", "./home", 12 * 60 * 60 * 1000L),
                        Tuple3.of("Bob", "./cart", 12 * 60 * 60 * 1000L),
                        Tuple3.of("Mary", "./prod?id=1", 12 * 60 * 60 * 1000L + 2 * 60 * 1000L),
                        Tuple3.of("Mary", "./prod?id=4", 12 * 60 * 60 * 1000L + 55 * 60 * 1000L),
                        Tuple3.of("Bob", "./prod?id=5", 13 * 60 * 60 * 1000L + 60 * 1000L),
                        Tuple3.of("Liz", "./home", 13 * 60 * 60 * 1000L + 30 * 60 * 1000L),
                        Tuple3.of("Liz", "./prod?id=7", 13 * 60 * 60 * 1000L + 59 * 60 * 1000L),
                        Tuple3.of("Mary", "./cart", 14 * 60 * 60 * 1000L),
                        Tuple3.of("Liz", "./home", 14 * 60 * 60 * 1000L + 2 * 60 * 1000L),
                        Tuple3.of("Bob", "./prod?id=3", 14 * 60 * 60 * 1000L + 30 * 60 * 1000L),
                        Tuple3.of("Bob", "./home", 14 * 60 * 60 * 1000L + 40 * 60 * 1000L)
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                                    @Override
                                    public long extractTimestamp(Tuple3<String, String, Long> element, long recordTimestamp) {
                                        return element.f2;
                                    }
                                })
                );

        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();

        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, settings);

        Table table = tableEnvironment
                .fromDataStream(
                        stream,
                        $("f0").as("user"),
                        $("f1").as("url"),
                        $("f2").rowtime().as("cTime")   // 将f2指定为事件时间，并命名为cTime
                );

        tableEnvironment.createTemporaryView("clicks", table);

        Table result = tableEnvironment
                .sqlQuery(
                        "SELECT user, COUNT(url) as cnt, TUMBLE_END(cTime, INTERVAL '1' HOUR) as endT " +
                                "FROM clicks GROUP BY user, TUMBLE(cTime, INTERVAL '1' HOUR)"
                );

        tableEnvironment.toChangelogStream(result).print();

        env.execute();

    }
}