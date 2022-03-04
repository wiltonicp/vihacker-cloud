package RealityCount;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.util.Collector;

// 实时对账
public class RealityCount01 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> orderStream = env
                .addSource(new SourceFunction<Event>() {
                    @Override
                    public void run(SourceContext<Event> ctx) throws Exception {
                        ctx.collectWithTimestamp(Event.of("order-1", "order", 1000L), 1000L);
                        ctx.emitWatermark(new Watermark(999L));
                        ctx.collectWithTimestamp(Event.of("order-2", "order", 3000L), 3000L);
                        ctx.emitWatermark(new Watermark(8001L));
                    }

                    @Override
                    public void cancel() {

                    }
                });

        SingleOutputStreamOperator<Event> weixinStream = env
                .addSource(new SourceFunction<Event>() {
                    @Override
                    public void run(SourceContext<Event> ctx) throws Exception {
                        ctx.collectWithTimestamp(Event.of("order-1", "weixin", 4000L), 4000L);
                        ctx.emitWatermark(new Watermark(3999L));
                        ctx.emitWatermark(new Watermark(8001L));
                        ctx.collectWithTimestamp(Event.of("order-2", "weixin", 9000L), 9000L);
                    }

                    @Override
                    public void cancel() {

                    }
                });

        orderStream
                .keyBy(r -> r.orderId)
                .connect(weixinStream.keyBy(r -> r.orderId))
                .process(new MatchFunction())
                .print();

        env.execute();
    }

    public static class MatchFunction extends CoProcessFunction<Event, Event, String> {
        private ValueState<Event> orderState;
        private ValueState<Event> weixinState;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            orderState = getRuntimeContext().getState(new ValueStateDescriptor<Event>("order", Types.POJO(Event.class)));
            weixinState = getRuntimeContext().getState(new ValueStateDescriptor<Event>("weixin", Types.POJO(Event.class)));
        }

        @Override
        public void processElement1(Event value, Context ctx, Collector<String> out) throws Exception {
            if (weixinState.value() == null) {
                // 下订单order事件先到达，因为如果weixin事件先到达，那么weixinState就不为空了
                orderState.update(value);
                ctx.timerService().registerEventTimeTimer(value.timestamp + 5000L);
            } else {
                out.collect("订单ID是" + value.orderId + "对账成功，weixin事件先到达");
                weixinState.clear();
            }
        }

        @Override
        public void processElement2(Event value, Context ctx, Collector<String> out) throws Exception {
            if (orderState.value() == null) {
                weixinState.update(value);
                ctx.timerService().registerEventTimeTimer(value.timestamp + 5000L);
            } else {
                out.collect("订单ID" + value.orderId + "对账成功，order事件先到达");
                orderState.clear();
            }
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            super.onTimer(timestamp, ctx, out);
            if (orderState.value() != null) {
                out.collect("订单ID" + orderState.value().orderId + "对账失败，weixin事件5s内未到达");
                orderState.clear();
            }
            if (weixinState.value() != null) {
                out.collect("订单ID" + weixinState.value().orderId + "对账失败，order事件5s内未到达");
                weixinState.clear();
            }
        }
    }

    public static class Event {
        public String orderId;
        public String eventType;
        public Long timestamp;

        public Event() {
        }

        public Event(String orderId, String eventType, Long timestamp) {
            this.orderId = orderId;
            this.eventType = eventType;
            this.timestamp = timestamp;
        }

        public static Event of(String orderId, String eventType, Long timestamp) {
            return new Event(orderId, eventType, timestamp);
        }

        @Override
        public String toString() {
            return "Event{" +
                    "orderId='" + orderId + '\'' +
                    ", eventType='" + eventType + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
}
