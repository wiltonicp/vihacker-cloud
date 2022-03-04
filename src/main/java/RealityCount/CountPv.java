package RealityCount;

import FunctionRealize.RealizePV;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CountPv {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStateBackend(new FsStateBackend("file:////src/main/resources/ckpt", false));
        env.enableCheckpointing(10 * 1000L);

        env
                .addSource(new RealizePV.ClickSource())
                .print();

        env.execute();
    }
}
