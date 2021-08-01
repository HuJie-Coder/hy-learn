package com.hy.poc.flink.datastream.transform;

import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SocketClientSink;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Duration;

/**
 * HyWatermark
 *  nc -lk 8989
 *  nc -lk 127.0.0.1 9898
 * @author Jie.Hu
 * @date 8/1/21 8:15 AM
 */
public class HyWatermark {

    public static String host = "127.0.0.1";

    public static Integer sourcePort = 8989;

    public static Integer sinkPort = 9898;

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();


        WatermarkStrategy<Tuple3<String, Long, Long>> watermarkStrategy = WatermarkStrategy
                .<Tuple3<String, Long, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner((event, timestamp) -> event.f1)
                .withIdleness(Duration.ofSeconds(5));

        SingleOutputStreamOperator<String> socketStream = streamEnv.socketTextStream(host, sourcePort).returns(Types.STRING);
        SingleOutputStreamOperator<Tuple3<String, Long, Long>> socketStreamTime = socketStream.map(row -> Tuple3.of(row, System.currentTimeMillis(), 1L)).returns(Types.TUPLE(Types.STRING, Types.LONG, Types.LONG));

        socketStreamTime.assignTimestampsAndWatermarks(watermarkStrategy)
                .keyBy(event -> event.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .reduce((tuple1, tuple2) -> Tuple3.of(tuple1.f0, System.currentTimeMillis(), tuple1.f2 + tuple2.f2))
                .map(tuple -> {
                    System.out.println(tuple);
                    return String.join("_", tuple.f0, tuple.f1.toString(), tuple.f2.toString()) + "\n";
                })
                .addSink(new SocketClientSink<String>(host, sinkPort, new SimpleStringSchema(), 100, true))
                .setParallelism(1);

        streamEnv.execute();


    }

}
