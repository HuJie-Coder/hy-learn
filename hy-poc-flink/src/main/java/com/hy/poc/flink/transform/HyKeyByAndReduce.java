package com.hy.poc.flink.transform;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SocketClientSink;

/**
 * HyKeyByAndReduce
 *
 * @author Jie.Hu
 * @date 8/1/21 5:58 PM
 */
public class HyKeyByAndReduce {

    public static String host = "127.0.0.1";

    public static Integer sourcePort = 8989;

    public static Integer sinkPort = 9898;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<String> source = streamEnv.socketTextStream(host, sourcePort).returns(Types.STRING);

        SingleOutputStreamOperator<String> operator = source.map(row -> Tuple2.of(row, 1))
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(row -> row.f0)
                .reduce((row1, row2) -> Tuple2.of(row1.f0, row1.f1 + row2.f1))
                .map(row -> StringUtils.join("(", row.f0, ",", row.f1, ")", "\n"));
        operator.addSink(new SocketClientSink<String>(host,
                sinkPort,
                new SimpleStringSchema(),
                100,
                true))
                .setParallelism(1);
        streamEnv.execute();
    }
}