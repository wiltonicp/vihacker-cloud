package BaseTransformation;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

// FLATMAP
public class RealizeFlatMap {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> stream = env.fromElements("white", "black", "gray");

        stream
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String value, Collector<String> out) throws Exception {
                        if (value.equals("white")) {
                            out.collect(value);
                        } else if (value.equals("black")) {
                            out.collect(value);
                            out.collect(value);
                        }
                    }
                })
                .print();

        stream
                .flatMap((String value, Collector<String> out) -> {
                    if (value.equals("white")) {
                        out.collect(value);
                    } else if (value.equals("black")) {
                        out.collect(value);
                        out.collect(value);
                    }
                })
                .returns(Types.STRING)
                .print();

        env.execute();
    }
}
