package FlinkProcessDataInter;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

// 写入kafka
public class FlinkProcessKafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "hadoop101:9092,hadoop102:9092,hadoop103:9092");

        env
                .readTextFile("/Users/jiangshanchen/githubspace/flink0224tutorial/src/main/resources/UserBehavior.csv")
                .addSink(new FlinkKafkaProducer<String>(
                        "user-behavior-1",
                        new SimpleStringSchema(),
                        properties
                ));

        env.execute();
    }
}
