package RealityCount;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

// 实时对账
public class RealityCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> backStream = env
                .fromElements(
                        Event.of("back-1", "back", 1000L),
                        Event.of("back-2", "back", 2000L)
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

        SingleOutputStreamOperator<Event> MobileTerminaStream = env
                .fromElements(
                        Event.of("back-1", "MobileTermina", 30000L),
                        Event.of("back-3", "MobileTermina", 4000L)
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

        backStream
                .keyBy(r -> r.backId)
                .connect(MobileTerminaStream.keyBy(r -> r.backId))
                .process(new MatchFunction())
                .print();

        env.execute();
    }

    public static class MatchFunction extends CoProcessFunction<Event, Event, String> {
        private ValueState<Event> backState;
        private ValueState<Event> MobileTerminaState;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            backState = getRuntimeContext().getState(new ValueStateDescriptor<Event>("back", Types.POJO(Event.class)));
            MobileTerminaState = getRuntimeContext().getState(new ValueStateDescriptor<Event>("MobileTermina", Types.POJO(Event.class)));
        }

        @Override
        public void processElement1(Event value, Context ctx, Collector<String> out) throws Exception {
            if (MobileTerminaState.value() == null) {
                // 下订单back事件先到达，因为如果MobileTermina事件先到达，那么MobileTerminaState就不为空了
                backState.update(value);
                ctx.timerService().registerEventTimeTimer(value.timestamp + 5000L);
            } else {
                out.collect("订单ID是" + value.backId + "对账成功，MobileTermina事件先到达");
                MobileTerminaState.clear();
            }
        }

        @Override
        public void processElement2(Event value, Context ctx, Collector<String> out) throws Exception {
            if (backState.value() == null) {
                MobileTerminaState.update(value);
                ctx.timerService().registerEventTimeTimer(value.timestamp + 5000L);
            } else {
                out.collect("订单ID" + value.backId + "对账成功，back事件先到达");
                backState.clear();
            }
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            super.onTimer(timestamp, ctx, out);
            if (backState.value() != null) {
                out.collect("订单ID" + backState.value().backId + "对账失败，MobileTermina事件5s内未到达");
                backState.clear();
            }
            if (MobileTerminaState.value() != null) {
                out.collect("订单ID" + MobileTerminaState.value().backId + "对账失败，back事件5s内未到达");
                MobileTerminaState.clear();
            }
        }
    }

    public static class Event {
        public String backId;
        public String eventType;
        public Long timestamp;

        public Event() {
        }

        public Event(String backId, String eventType, Long timestamp) {
            this.backId = backId;
            this.eventType = eventType;
            this.timestamp = timestamp;
        }

        public static Event of(String backId, String eventType, Long timestamp) {
            return new Event(backId, eventType, timestamp);
        }

        @Override
        public String toString() {
            return "Event{" +
                    "backId='" + backId + '\'' +
                    ", eventType='" + eventType + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
}
