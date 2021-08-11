package com.hy.poc.flink.transform;

import com.hy.poc.flink.utils.HySocketUtil;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * HyOperatorChain
 *
 * @author Jie.Hu
 * @date 8/8/21 12:55 PM
 */
public class HyOperatorChain {

    private final static String DEFAULT_LISTEN_ADDRESS = "127.0.0.1";

    private final static Integer DEFAULT_BIND_PORT = 8989;

    public static void main(String[] args) throws Exception {

        HySocketUtil.startSocketServer();

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<Tuple2<String, String>> map = environment
                .socketTextStream(DEFAULT_LISTEN_ADDRESS, DEFAULT_BIND_PORT)
                .map(new RichMapFunction<String, Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> map(String event) throws Exception {
                        String[] strings = event.split(",");
                        return Tuple2.of(strings[0], strings[1]);
                    }
                })
                .setParallelism(1);

        map.print("test").setParallelism(1);

        environment.execute();

    }
}
