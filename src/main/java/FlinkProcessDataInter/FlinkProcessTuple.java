package FlinkProcessDataInter;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

// create table tuple(k varchar(10), v int);
public class FlinkProcessTuple {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env
                .fromElements(Tuple2.of("key", 1), Tuple2.of("key", 2))
                .addSink(new RichSinkFunction<Tuple2<String, Integer>>() {
                    private Connection conn;
                    private PreparedStatement insertStmt;
                    private PreparedStatement updateStmt;
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/sensor",
                                "zuoyuan",
                                "zuoyuan"
                        );
                        insertStmt = conn.prepareStatement("INSERT INTO tuple (k, v) VALUES (?, ?)");
                        updateStmt = conn.prepareStatement("UPDATE tuple SET v = ? WHERE k = ?");
                    }

                    @Override
                    public void invoke(Tuple2<String, Integer> value, Context context) throws Exception {
                        super.invoke(value, context);
                        updateStmt.setInt(1, value.f1);
                        updateStmt.setString(2, value.f0);
                        updateStmt.execute();
                        if (updateStmt.getUpdateCount() == 0) {
                            insertStmt.setString(1, value.f0);
                            insertStmt.setInt(2, value.f1);
                            insertStmt.execute();
                        }
                    }

                    @Override
                    public void close() throws Exception {
                        super.close();
                        insertStmt.close();
                        updateStmt.close();
                        conn.close();
                    }
                });

        env.execute();
    }
}
