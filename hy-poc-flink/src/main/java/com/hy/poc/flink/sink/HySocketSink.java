package com.hy.poc.flink.sink;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SocketClientSink;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * HySocketSink
 *
 * @author Jie.Hu
 * @date 7/31/21 10:12 PM
 */
public class HySocketSink {

    public static String host = "127.0.0.1";

    public static Integer sourcePort = 8989;

    public static Integer sinkPort = 9898;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = streamEnv.socketTextStream(host, sourcePort);
        SingleOutputStreamOperator<String> flapMapStream = source.returns(Types.STRING)
                .flatMap(((String value, Collector<String> collector) -> {
                    String[] strings = value.split(",");
                    for (String string : strings) {
                        collector.collect(string);
                    }
                }))
                .returns(Types.STRING).setParallelism(1);
        flapMapStream.print().setParallelism(1);
        flapMapStream.addSink(new SocketClientSink<String>(host,
                sinkPort,
                new SimpleStringSchema(),
                100,
                true))
                .setParallelism(1);
        streamEnv.execute();
    }

}
